/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.view.task.gantt;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.*;
import com.esofthead.mycollab.module.project.events.GanttEvent;
import com.esofthead.mycollab.module.project.events.MilestoneEvent;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.service.GanttAssignmentService;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.ui.components.HumanTimeConverter;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.converter.LocalDateConverter;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Container;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tltv.gantt.client.shared.Step;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.peter.contextmenu.ContextMenu;

import java.util.*;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class GanttTreeTable extends TreeTable {
    private static Logger LOG = LoggerFactory.getLogger(GanttTreeTable.class);

    private GanttExt gantt;
    private GanttItemContainer beanContainer;

    private boolean ganttIndexIsChanged = false;
    protected List<Field> fields = new ArrayList<>();
    private boolean isStartedGanttChart = false;

    private ApplicationEventListener<GanttEvent.UpdateGanttItemDates> updateTaskInfoHandler = new
            ApplicationEventListener<GanttEvent.UpdateGanttItemDates>() {
                @Subscribe
                @Override
                public void handle(GanttEvent.UpdateGanttItemDates event) {
                    GanttItemWrapper item = (GanttItemWrapper) event.getData();
                    updateTaskTree(item);
                }
            };

    public GanttTreeTable(final GanttExt gantt) {
        super();
        this.gantt = gantt;
        this.setWidth("800px");
        this.setBuffered(true);
        beanContainer = gantt.getBeanContainer();
        this.setContainerDataSource(beanContainer);
        this.setVisibleColumns("ganttIndex", "name", "startDate", "endDate", "duration", "percentageComplete",
                "predecessors", "assignUser", "actualStartDate", "actualEndDate");
        this.setColumnHeader("ganttIndex", "");
        this.setColumnWidth("ganttIndex", 35);
        this.setColumnHeader("name", "Task");
        this.setColumnExpandRatio("name", 1.0f);
        this.setHierarchyColumn("name");
        this.setColumnHeader("startDate", "Start");
        this.setColumnWidth("startDate", 80);
        this.setColumnHeader("endDate", "End");
        this.setColumnWidth("endDate", 80);
        this.setColumnHeader("duration", "Duration");
        this.setColumnWidth("duration", 75);
        this.setColumnHeader("predecessors", "Predecessors");
        this.setColumnWidth("predecessors", 80);
        this.setColumnHeader("actualStartDate", "Actual Start");
        this.setColumnWidth("actualStartDate", 80);
        this.setColumnHeader("actualEndDate", "Actual End");
        this.setColumnWidth("actualEndDate", 80);
        this.setColumnHeader("percentageComplete", "% Complete");
        this.setColumnWidth("percentageComplete", 80);
        this.setColumnHeader("assignUser", "Assignee");
        this.setColumnWidth("assignUser", 80);
        this.setColumnCollapsingAllowed(true);
        this.setColumnCollapsed("actualStartDate", true);
        this.setColumnCollapsed("actualEndDate", true);
        this.setColumnCollapsed("assignUser", true);
        this.setEditable(true);

        this.addGeneratedColumn("ganttIndex", new ColumnGenerator() {
            @Override
            public Object generateCell(Table table, Object itemId, Object columnId) {
                GanttItemWrapper item = (GanttItemWrapper) itemId;
                return new Label("" + item.getGanttIndex());
            }
        });


        this.setTableFieldFactory(new TableFieldFactory() {
            @Override
            public Field<?> createField(Container container, Object itemId, final Object propertyId, Component uiContext) {
                Field field = null;
                final GanttItemWrapper ganttItem = (GanttItemWrapper) itemId;
                if ("name".equals(propertyId)) {
                    if (ganttItem.isMilestone()) {
                        field = new MilestoneNameCellField();
                    } else {
                        field = new TextField();
                        ((TextField) field).setDescription(ganttItem.getName());
                    }

                } else if ("percentageComplete".equals(propertyId)) {
                    field = new TextField();
                    ((TextField) field).setNullRepresentation("0");
                    ((TextField) field).setImmediate(true);
                    if (ganttItem.hasSubTasks() || ganttItem.isMilestone()) {
                        field.setEnabled(false);
                        ((TextField) field).setDescription("Because this row has sub-tasks, this cell " +
                                "is a summary value and can not be edited directly. You can edit cells " +
                                "beneath this row to change its value");
                    }
                } else if ("startDate".equals(propertyId) || "endDate".equals(propertyId) ||
                        "actualStartDate".equals(propertyId) || "actualEndDate".equals(propertyId)) {
                    field = new DateField();
                    ((DateField) field).setConverter(new LocalDateConverter());
                    ((DateField) field).setImmediate(true);
                    if (ganttItem.hasSubTasks()) {
                        field.setEnabled(false);
                        ((DateField) field).setDescription("Because this row has sub-tasks, this cell " +
                                "is a summary value and can not be edited directly. You can edit cells " +
                                "beneath this row to change its value");
                    }
                } else if ("assignUser".equals(propertyId)) {
                    field = new ProjectMemberSelectionField();
                } else if ("predecessors".equals(propertyId)) {
                    field = new DefaultViewField("");
                    ((DefaultViewField) field).setConverter(new PredecessorConverter());
                    return field;
                } else if ("duration".equals(propertyId)) {
                    field = new TextField();
                    ((TextField) field).setConverter(new HumanTimeConverter());
                    if (ganttItem.hasSubTasks()) {
                        field.setEnabled(false);
                        ((TextField) field).setDescription("Because this row has sub-tasks, this cell " +
                                "is a summary value and can not be edited directly. You can edit cells " +
                                "beneath this row to change its value");
                    }
                }

                if (field != null) {
                    field.setBuffered(true);
                    field.setWidth("100%");
                    if (ganttItem.isMilestone()) {
                        if (!CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES)) {
                            field.setEnabled(false);
                            ((AbstractComponent) field).setDescription(AppContext.getMessage(GenericI18Enum.NOTIFICATION_NO_PERMISSION_DO_TASK));
                        }
                    } else if (ganttItem.isTask()) {
                        if (!CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
                            field.setEnabled(false);
                            ((AbstractComponent) field).setDescription(AppContext.getMessage(GenericI18Enum.NOTIFICATION_NO_PERMISSION_DO_TASK));
                        }
                    } else {
                        throw new MyCollabException("Do not support gantt item type " + ganttItem.getTask());
                    }

                    if (field instanceof FieldEvents.BlurNotifier) {
                        ((FieldEvents.BlurNotifier) field).addBlurListener(new FieldEvents.BlurListener() {
                            @Override
                            public void blur(FieldEvents.BlurEvent event) {
                                Object o = event.getSource();
                                if (o instanceof Field) {
                                    Field f = (Field) o;
                                    if (f.isModified()) {
                                        f.commit();
                                        EventBusFactory.getInstance().post(new GanttEvent.AddGanttItemUpdateToQueue
                                                (GanttTreeTable.this, ganttItem));
                                        GanttTreeTable.this.refreshRowCache();
                                    }
                                }
                            }
                        });
                    }
                }
                return field;
            }
        });

        this.addExpandListener(new Tree.ExpandListener() {
            @Override
            public void nodeExpand(Tree.ExpandEvent expandEvent) {
                GanttItemWrapper item = (GanttItemWrapper) expandEvent.getItemId();
                List<GanttItemWrapper> subTasks = item.subTasks();
                insertSubSteps(item, subTasks);
            }
        });

        this.addCollapseListener(new Tree.CollapseListener() {
            @Override
            public void nodeCollapse(Tree.CollapseEvent collapseEvent) {
                GanttItemWrapper item = (GanttItemWrapper) collapseEvent.getItemId();
                List<GanttItemWrapper> subTasks = item.subTasks();
                removeSubSteps(item, subTasks);
            }
        });

        this.setCellStyleGenerator(new CellStyleGenerator() {
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                GanttItemWrapper item = (GanttItemWrapper) itemId;
                if (item.isMilestone()) {
                    return "milestone";
                } else if (item.isTask()) {
                    return "task";
                }
                return null;
            }
        });

        final GanttContextMenu contextMenu = new GanttContextMenu();
        contextMenu.setAsContextMenuOf(this);
        contextMenu.setOpenAutomatically(false);

        ContextMenu.ContextMenuOpenedListener.TableListener tableListener = new ContextMenu.ContextMenuOpenedListener.TableListener() {
            public void onContextMenuOpenFromRow(ContextMenu.ContextMenuOpenedOnTableRowEvent event) {
                GanttItemWrapper item = (GanttItemWrapper) event.getItemId();
                contextMenu.displayContextMenu(item);
                contextMenu.open(GanttTreeTable.this);
            }

            public void onContextMenuOpenFromHeader(ContextMenu.ContextMenuOpenedOnTableHeaderEvent event) {
            }

            public void onContextMenuOpenFromFooter(ContextMenu.ContextMenuOpenedOnTableFooterEvent event) {
            }
        };

        contextMenu.addContextMenuTableListener(tableListener);
        gantt.setVerticalScrollDelegateTarget(this);
    }

    GanttItemContainer getRawContainer() {
        return beanContainer;
    }

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(updateTaskInfoHandler);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(updateTaskInfoHandler);
        super.detach();
    }

    public void loadAssignments() {
        GanttAssignmentService ganttAssignmentService = ApplicationContextUtil.getSpringBean(GanttAssignmentService.class);
        final List<AssignWithPredecessors> assignments = ganttAssignmentService.getTaskWithPredecessors(Arrays.asList
                (CurrentProjectVariables.getProjectId()), AppContext.getAccountId());
        if (assignments.size() == 1) {
            ProjectGanttItem projectGanttItem = (ProjectGanttItem) assignments.get(0);
            List<MilestoneGanttItem> milestoneGanttItems = projectGanttItem.getMilestones();
            for (MilestoneGanttItem milestoneGanttItem : milestoneGanttItems) {
                GanttItemWrapper itemWrapper = new GanttItemWrapper(gantt, milestoneGanttItem);
                this.addTask(itemWrapper);
            }

            List<TaskGanttItem> taskGanttItems = projectGanttItem.getTasksWithNoMilestones();
            for (TaskGanttItem taskGanttItem : taskGanttItems) {
                GanttItemWrapper itemWrapper = new GanttItemWrapper(gantt, taskGanttItem);
                this.addTask(itemWrapper);
            }
            this.updateWholeGanttIndexes();
            gantt.addStep(new Step());
        } else {
            LOG.error("Error to query multiple value " + CurrentProjectVariables.getProjectId());
        }
        isStartedGanttChart = true;
    }

    private void updateTaskTree(GanttItemWrapper ganttItemWrapper) {
        this.markAsDirtyRecursive();
    }

    public void addTask(GanttItemWrapper itemWrapper) {
        int ganttIndex = beanContainer.size() + 1;
        if (itemWrapper.getGanttIndex() == null || ganttIndex != itemWrapper.getGanttIndex()) {
            itemWrapper.setGanttIndex(ganttIndex);
            ganttIndexIsChanged = true;
        }

        beanContainer.addBean(itemWrapper);
        gantt.addTask(itemWrapper);

        if (itemWrapper.hasSubTasks()) {
            this.setChildrenAllowed(itemWrapper, true);
            this.setCollapsed(itemWrapper, false);
        } else {
            this.setChildrenAllowed(itemWrapper, false);
        }
    }

    private void insertSubSteps(final GanttItemWrapper parent, final List<GanttItemWrapper> children) {
        final int stepIndex = gantt.getStepIndex(parent.getStep());
        int count = 0;
        if (stepIndex != -1) {
            LocalDate startDate = parent.getStartDate();
            LocalDate endDate = parent.getEndDate();

            for (GanttItemWrapper child : children) {
                if (!beanContainer.containsId(child)) {
                    beanContainer.addBean(child);

                    int ganttIndex = beanContainer.indexOfId(child) + 1;
                    if (child.getGanttIndex() == null || (child.getGanttIndex() != ganttIndex && !isStartedGanttChart)) {
                        child.setGanttIndex(ganttIndex);
                        ganttIndexIsChanged = true;
                    }

                    this.setParent(child, parent);
                    if (!isStartedGanttChart) {
                        gantt.addTask(child);
                    } else {
                        gantt.addTask(stepIndex + count + 1, child);
                    }

                    if (child.hasSubTasks()) {
                        this.setChildrenAllowed(child, true);
                        this.setCollapsed(child, false);
                    } else {
                        this.setChildrenAllowed(child, false);
                    }
                    count++;
                    startDate = DateTimeUtils.min(startDate, child.getStartDate());
                    endDate = DateTimeUtils.max(endDate, child.getEndDate());
                }

            }
            parent.setStartAndEndDate(startDate, endDate, false, false);
            gantt.markStepDirty(parent.getStep());
        }
    }

    void removeSubSteps(final GanttItemWrapper parent, final List<GanttItemWrapper> childs) {
        final int stepIndex = gantt.getStepIndex(parent.getStep());
        if (stepIndex != -1) {
            for (GanttItemWrapper child : childs) {
                if (child.hasSubTasks()) {
                    removeSubSteps(child, child.subTasks());
                }
                this.removeItem(child);
                gantt.removeStep(child.getStep());
            }
        }
    }

    public void updateWholeGanttIndexes() {
        if (ganttIndexIsChanged) {
            Collection<GanttItemWrapper> items = beanContainer.getItemIds();
            for (GanttItemWrapper item : items) {
                EventBusFactory.getInstance().post(new GanttEvent.AddGanttItemUpdateToQueue(GanttTreeTable.this, item));
            }
        }
    }

    private void calculateWholeGanttIndexes() {
        GanttItemWrapper item = beanContainer.firstItemId();
        int index = 1;
        while (item != null) {
            if (item.getGanttIndex() != index) {
                item.setGanttIndex(index);
                ganttIndexIsChanged = true;
            }

            item = beanContainer.nextItemId(item);
            index++;
        }

        if (ganttIndexIsChanged) {
            calculateGanttIndexOfPredecessors();
        }
        this.refreshRowCache();
    }

    private void calculateGanttIndexOfPredecessors() {
        List<GanttItemWrapper> items = beanContainer.getItemIds();
        for (GanttItemWrapper item : items) {
            List<TaskPredecessor> predecessors = item.getPredecessors();
            if (CollectionUtils.isNotEmpty(predecessors)) {
                for (TaskPredecessor predecessor : predecessors) {
                    Integer descId = predecessor.getDescid();
                    String descType = predecessor.getDesctype();
                    GanttItemWrapper predecessorGanttItem = findGanttItem(descId, descType);
                    if (predecessorGanttItem != null) {
                        predecessor.setGanttIndex(predecessorGanttItem.getGanttIndex());
                    }
                }
            }
        }
    }

    private GanttItemWrapper findGanttItem(Integer assignmentId, String assignmentType) {
        List<GanttItemWrapper> items = beanContainer.getItemIds();
        for (GanttItemWrapper item : items) {
            if (assignmentId.intValue() == item.getId().intValue() && assignmentType.equals(item.getType())) {
                return item;
            }
        }
        return null;
    }

    private class GanttContextMenu extends ContextMenu {

        void displayContextMenu(final GanttItemWrapper taskWrapper) {
            this.removeAllItems();
            ContextMenuItem detailMenuItem = this.addItem("Detail", FontAwesome.BARS);
            detailMenuItem.addItemClickListener(new ContextMenuItemClickListener() {
                @Override
                public void contextMenuItemClicked(ContextMenuItemClickEvent event) {
                    if (taskWrapper.isTask()) {
                        if (taskWrapper.getId() == null) {
                            //New task, save then go to the task detail view
                            Task newTask = taskWrapper.buildNewTask();
                            ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                            taskService.saveWithSession(newTask, AppContext.getUsername());
                            taskWrapper.setId(newTask.getId());
                            EventBusFactory.getInstance().post(new TaskEvent.GotoRead(GanttTreeTable.this, newTask.getId()));
                        } else {
                            EventBusFactory.getInstance().post(new TaskEvent.GotoRead(GanttTreeTable.this, taskWrapper.getId()));
                        }
                    } else if (taskWrapper.isMilestone()) {
                        EventBusFactory.getInstance().post(new MilestoneEvent.GotoRead(GanttTreeTable.this, taskWrapper.getId()));
                    }
                }
            });

            ContextMenuItem predecessorMenuItem = this.addItem("Predecessors", FontAwesome.MAP_MARKER);
            predecessorMenuItem.addItemClickListener(new ContextMenuItemClickListener() {
                @Override
                public void contextMenuItemClicked(ContextMenuItemClickEvent contextMenuItemClickEvent) {
                    if (taskWrapper.isTask()) {
                        UI.getCurrent().addWindow(new PredecessorWindow(GanttTreeTable.this, taskWrapper));
                    } else {
                        NotificationUtil.showWarningNotification("Can not edit predecessors for milestone");
                    }
                }
            });

            ContextMenuItem indentMenuItem = this.addItem("Indent", FontAwesome.INDENT);
            indentMenuItem.setEnabled(taskWrapper.isIndentable());
            indentMenuItem.addItemClickListener(new ContextMenuItemClickListener() {
                @Override
                public void contextMenuItemClicked(ContextMenuItemClickEvent contextMenuItemClickEvent) {
                    GanttItemWrapper preItemWrapper = beanContainer.prevItemId(taskWrapper);
                    if (preItemWrapper != null && preItemWrapper != taskWrapper.getParent()) {
                        taskWrapper.updateParentRelationship(preItemWrapper);
                        GanttTreeTable.this.setChildrenAllowed(preItemWrapper, true);
                        GanttTreeTable.this.setParent(taskWrapper, preItemWrapper);
                        preItemWrapper.calculateDatesByChildTasks();
                        GanttTreeTable.this.setCollapsed(preItemWrapper, false);
                        GanttTreeTable.this.refreshRowCache();
                        EventBusFactory.getInstance().post(new GanttEvent.AddGanttItemUpdateToQueue
                                (GanttTreeTable.this, taskWrapper));
                    }
                }
            });

            ContextMenuItem outdentMenuItem = this.addItem("Outdent", FontAwesome.OUTDENT);
            outdentMenuItem.setEnabled(taskWrapper.isOutdentable());
            outdentMenuItem.addItemClickListener(new ContextMenuItemClickListener() {
                @Override
                public void contextMenuItemClicked(ContextMenuItemClickEvent contextMenuItemClickEvent) {
                    GanttItemWrapper parent = taskWrapper.getParent();
                    if (parent != null) {
                        GanttTreeTable.this.setParent(taskWrapper, parent.getParent());
                        taskWrapper.updateParentRelationship(parent.getParent());
                        GanttTreeTable.this.setCollapsed(taskWrapper, false);
                        // Set all below tasks of taskWrapper have parent is taskWrapper
                        GanttItemWrapper nextItem = beanContainer.nextItemId(taskWrapper);
                        while (nextItem != null && nextItem.getParent() == parent) {
                            GanttTreeTable.this.setChildrenAllowed(taskWrapper, true);
                            nextItem.updateParentRelationship(taskWrapper);
                            GanttTreeTable.this.setParent(nextItem, taskWrapper);
                            EventBusFactory.getInstance().post(new GanttEvent.AddGanttItemUpdateToQueue
                                    (GanttTreeTable.this, nextItem));
                        }

                        if (taskWrapper.hasSubTasks()) {
                            taskWrapper.calculateDatesByChildTasks();
                        }
                        GanttTreeTable.this.setChildrenAllowed(taskWrapper, taskWrapper.hasSubTasks());
                        parent.calculateDatesByChildTasks();
                        GanttTreeTable.this.setChildrenAllowed(parent, parent.hasSubTasks());

                        if (parent.getParent() != null) {
                            parent.getParent().calculateDatesByChildTasks();
                        }
                        GanttTreeTable.this.refreshRowCache();
                        EventBusFactory.getInstance().post(new GanttEvent.AddGanttItemUpdateToQueue
                                (GanttTreeTable.this, taskWrapper));
                    }
                }
            });

            if (beanContainer.indexOfId(taskWrapper) > 0) {
                ContextMenuItem inserRowBeforeMenuItem = this.addItem("Insert row before", FontAwesome.PLUS_CIRCLE);
                inserRowBeforeMenuItem.addItemClickListener(new ContextMenuItemClickListener() {
                    @Override
                    public void contextMenuItemClicked(ContextMenuItemClickEvent contextMenuItemClickEvent) {
                        int index = beanContainer.indexOfId(taskWrapper);
                        if (index > 0) {
                            TaskGanttItem newTask = new TaskGanttItem();
                            newTask.setType(ProjectTypeConstants.TASK);
                            newTask.setPrjId(taskWrapper.getTask().getPrjId());
                            newTask.setName("New Task");
                            newTask.setProgress(0d);
                            newTask.setsAccountId(AppContext.getAccountId());
                            GanttItemWrapper newGanttItem = new GanttItemWrapper(gantt, newTask);
                            newGanttItem.setGanttIndex(index + 1);
                            GanttItemWrapper prevItem = beanContainer.prevItemId(taskWrapper);
                            beanContainer.addItemAfter(prevItem, newGanttItem);
                            gantt.addTask(index, newGanttItem);
                            GanttTreeTable.this.setChildrenAllowed(newGanttItem, newGanttItem.hasSubTasks());

                            if (taskWrapper.getParent() != null) {
                                GanttItemWrapper parentTask = taskWrapper.getParent();
                                GanttTreeTable.this.setParent(newGanttItem, parentTask);
                                newGanttItem.updateParentRelationship(parentTask);
                                parentTask.calculateDatesByChildTasks();
                            }
                        }

                        calculateWholeGanttIndexes();
                        GanttTreeTable.this.refreshRowCache();
                    }
                });
            }

            ContextMenuItem insertRowAfterMenuItem = this.addItem("Insert row after", FontAwesome.PLUS_CIRCLE);
            insertRowAfterMenuItem.addItemClickListener(new ContextMenuItemClickListener() {
                @Override
                public void contextMenuItemClicked(ContextMenuItemClickEvent contextMenuItemClickEvent) {
                    int index = beanContainer.indexOfId(taskWrapper) + 1;
                    TaskGanttItem newTask = new TaskGanttItem();
                    newTask.setType(ProjectTypeConstants.TASK);
                    newTask.setPrjId(taskWrapper.getTask().getPrjId());
                    newTask.setName("New Task");
                    newTask.setProgress(0d);
                    newTask.setsAccountId(AppContext.getAccountId());
                    GanttItemWrapper newGanttItem = new GanttItemWrapper(gantt, newTask);
                    newGanttItem.setGanttIndex(index);
                    beanContainer.addItemAfter(taskWrapper, newGanttItem);
                    gantt.addTask(index, newGanttItem);

                    if (taskWrapper.hasSubTasks()) {
                        GanttTreeTable.this.setParent(newGanttItem, taskWrapper);
                        newGanttItem.updateParentRelationship(taskWrapper);
                        taskWrapper.calculateDatesByChildTasks();
                    } else if (taskWrapper.getParent() != null) {
                        GanttItemWrapper parentTask = taskWrapper.getParent();
                        GanttTreeTable.this.setParent(newGanttItem, parentTask);
                        newGanttItem.updateParentRelationship(parentTask);
                        parentTask.calculateDatesByChildTasks();
                    }
                    GanttTreeTable.this.setChildrenAllowed(newGanttItem, newGanttItem.hasSubTasks());
                    calculateWholeGanttIndexes();
                    GanttTreeTable.this.refreshRowCache();
                }
            });

            ContextMenuItem deleteRowMenuItem = this.addItem("Delete row", FontAwesome.TRASH_O);
            deleteRowMenuItem.addItemClickListener(new ContextMenuItemClickListener() {
                @Override
                public void contextMenuItemClicked(ContextMenuItemClickEvent contextMenuItemClickEvent) {
                    ConfirmDialogExt.show(UI.getCurrent(),
                            AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE,
                                    AppContext.getSiteName()),
                            AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_MULTIPLE_ITEMS_MESSAGE),
                            AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                            AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                            new ConfirmDialog.Listener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void onClose(ConfirmDialog dialog) {
                                    if (dialog.isConfirmed()) {
                                        removeTask(taskWrapper);
                                    }
                                }
                            });
                }
            });
        }

        private void removeTask(GanttItemWrapper task) {
            EventBusFactory.getInstance().post(new GanttEvent.DeleteGanttItemUpdateToQueue(GanttTreeTable.this, task));
            beanContainer.removeItem(task);
            gantt.removeStep(task.getStep());
            gantt.markAsDirtyRecursive();

            GanttItemWrapper parentTask = task.getParent();
            if (parentTask != null) {
                parentTask.removeSubTask(task);
                GanttTreeTable.this.setChildrenAllowed(parentTask, parentTask.hasSubTasks());
            }

            if (task.hasSubTasks()) {
                Iterator<GanttItemWrapper> iter = task.subTasks().iterator();
                while (iter.hasNext()) {
                    GanttItemWrapper subTask = iter.next();
                    iter.remove();
                    removeTask(subTask);
                }
            }

            if (parentTask != null) {
                parentTask.calculateDatesByChildTasks();
            }
            calculateWholeGanttIndexes();
        }
    }

    private static class PredecessorConverter implements Converter<String, List> {
        @Override
        public List convertToModel(String value, Class<? extends List> targetType, Locale locale) throws ConversionException {
            return null;
        }

        @Override
        public String convertToPresentation(List predecessors, Class<? extends String> targetType, Locale locale) throws ConversionException {
            if (CollectionUtils.isNotEmpty(predecessors)) {
                StringBuilder builder = new StringBuilder();
                for (Object item : predecessors) {
                    TaskPredecessor predecessor = (TaskPredecessor) item;
                    if (predecessor.getLagday() == 0) {
                        if (TaskPredecessor.FS.equals(predecessor.getPredestype())) {
                            builder.append(predecessor.getGanttIndex());
                        } else {
                            builder.append(predecessor.getGanttIndex() + predecessor.getPredestype());
                        }
                    } else {
                        builder.append(predecessor.getGanttIndex() + predecessor.getPredestype());
                        if (predecessor.getLagday() > 0) {
                            builder.append("+" + predecessor.getLagday() + "d");
                        } else {
                            builder.append(predecessor.getLagday() + "d");
                        }
                    }

                    builder.append(",");
                }
                if (builder.charAt(builder.length() - 1) == ',') {
                    builder.deleteCharAt(builder.length() - 1);
                }
                return builder.toString();
            } else {
                return "";
            }
        }

        @Override
        public Class<List> getModelType() {
            return List.class;
        }

        @Override
        public Class<String> getPresentationType() {
            return String.class;
        }
    }
}
