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
package com.esofthead.mycollab.module.project.view.assignments.gantt;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.*;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.events.GanttEvent;
import com.esofthead.mycollab.module.project.events.MilestoneEvent;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.service.GanttAssignmentService;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.ui.components.HumanTimeConverter;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.web.ui.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.web.ui.field.converter.LocalDateConverter;
import com.google.common.eventbus.Subscribe;
import com.vaadin.addon.contextmenu.ContextMenu;
import com.vaadin.addon.contextmenu.MenuItem;
import com.vaadin.data.Container;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

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

    private ApplicationEventListener<GanttEvent.UpdateGanttItem> updateTaskInfoHandler = new
            ApplicationEventListener<GanttEvent.UpdateGanttItem>() {
                @Subscribe
                @Override
                public void handle(GanttEvent.UpdateGanttItem event) {
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
                "predecessors", "assignUser");
        this.setColumnHeader("ganttIndex", "");
        this.setColumnWidth("ganttIndex", 25);
        this.setColumnHeader("name", "Task");
        this.setColumnExpandRatio("name", 1.0f);
        this.setHierarchyColumn("name");
        this.setColumnHeader("startDate", "Start");
        this.setColumnWidth("startDate", 90);
        this.setColumnHeader("endDate", "End");
        this.setColumnWidth("endDate", 90);
        this.setColumnHeader("duration", "Duration");
        this.setColumnWidth("duration", 65);
        this.setColumnHeader("predecessors", "Predecessors");
        this.setColumnWidth("predecessors", 100);
        this.setColumnHeader("percentageComplete", "% Complete");
        this.setColumnWidth("percentageComplete", 90);
        this.setColumnHeader("assignUser", "Assignee");
        this.setColumnWidth("assignUser", 80);
        this.setColumnCollapsingAllowed(true);
        this.setColumnCollapsed("assignUser", true);
        this.setEditable(true);
        this.setNullSelectionAllowed(false);

        this.addGeneratedColumn("ganttIndex", new ColumnGenerator() {
            @Override
            public Object generateCell(Table table, Object itemId, Object columnId) {
                GanttItemWrapper item = (GanttItemWrapper) itemId;
                return new ELabel("" + item.getGanttIndex()).withStyleName(ValoTheme.LABEL_SMALL);
            }
        });

        this.setTableFieldFactory(new TableFieldFactory() {
            @Override
            public Field<?> createField(Container container, Object itemId, final Object propertyId, Component uiContext) {
                Field field = null;
                final GanttItemWrapper ganttItem = (GanttItemWrapper) itemId;
                if ("name".equals(propertyId)) {
                    field = new AssignmentNameCellField(ganttItem.getType());
                } else if ("percentageComplete".equals(propertyId)) {
                    field = new TextField();
                    ((TextField) field).setNullRepresentation("0");
                    ((TextField) field).setImmediate(true);
                    field.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                    if (ganttItem.hasSubTasks() || ganttItem.isMilestone()) {
                        field.setEnabled(false);
                        ((TextField) field).setDescription("Because this row has sub-tasks, this cell " +
                                "is a summary value and can not be edited directly. You can edit cells " +
                                "beneath this row to change its value");
                    }
                } else if ("startDate".equals(propertyId) || "endDate".equals(propertyId)) {
                    field = new DateField();
                    field.addStyleName(ValoTheme.DATEFIELD_SMALL);
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
                    field.addStyleName(ValoTheme.TEXTFIELD_SMALL);
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
                    } else if (ganttItem.isBug()) {
                        if (!CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS)) {
                            field.setEnabled(false);
                            ((AbstractComponent) field).setDescription(AppContext.getMessage(GenericI18Enum.NOTIFICATION_NO_PERMISSION_DO_TASK));
                        }
                    } else {
                        throw new MyCollabException("Do not support gantt item type " + ganttItem.getTask().getType());
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
                    return "root";
                } else if (item.isTask()) {
                    return "";
                }
                return "";
            }
        });

        final GanttContextMenu contextMenu = new GanttContextMenu();

        gantt.setVerticalScrollDelegateTarget(this);
        int currentPageLength = 50;
        this.setPageLength(currentPageLength);
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
        try {
            GanttAssignmentService ganttAssignmentService = ApplicationContextUtil.getSpringBean(GanttAssignmentService.class);
            final List<AssignWithPredecessors> assignments = ganttAssignmentService.getTaskWithPredecessors(Collections.singletonList(CurrentProjectVariables.getProjectId()), AppContext.getAccountId());
            if (assignments.size() == 1) {
                ProjectGanttItem projectGanttItem = (ProjectGanttItem) assignments.get(0);
                List<MilestoneGanttItem> milestoneGanttItems = projectGanttItem.getMilestones();
                for (MilestoneGanttItem milestoneGanttItem : milestoneGanttItems) {
                    GanttItemWrapper itemWrapper = new GanttItemWrapper(gantt, milestoneGanttItem);
                    this.addRootAssignments(itemWrapper);
                }

                List<TaskGanttItem> taskGanttItems = projectGanttItem.getTasksWithNoMilestones();
                for (TaskGanttItem taskGanttItem : taskGanttItems) {
                    GanttItemWrapper itemWrapper = new GanttItemWrapper(gantt, taskGanttItem);
                    this.addRootAssignments(itemWrapper);
                }
                this.updateWholeGanttIndexes();
            } else {
                LOG.error("Error to query multiple value " + CurrentProjectVariables.getProjectId());
            }
            isStartedGanttChart = true;
        } catch (Exception e) {
            LOG.error("Error", e);
        }
    }

    private void updateTaskTree(GanttItemWrapper ganttItemWrapper) {
        if (ganttItemWrapper.hasSubTasks()) {
            boolean collapsed = this.isCollapsed(ganttItemWrapper);
            this.setCollapsed(ganttItemWrapper, !collapsed);
            this.setCollapsed(ganttItemWrapper, collapsed);
        }
        this.markAsDirtyRecursive();
    }

    public void addRootAssignments(GanttItemWrapper itemWrapper) {
        int ganttIndex = beanContainer.size() + 1;
        if (itemWrapper.getGanttIndex() == null || ganttIndex != itemWrapper.getGanttIndex()) {
            itemWrapper.setGanttIndex(ganttIndex);
            ganttIndexIsChanged = true;
        }

        beanContainer.addItem(itemWrapper);
        gantt.addTask(itemWrapper);

        if (itemWrapper.hasSubTasks()) {
            beanContainer.setChildrenAllowed(itemWrapper, true);
            this.setCollapsed(itemWrapper, false);
        } else {
            beanContainer.setChildrenAllowed(itemWrapper, false);
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
                    beanContainer.addItem(child);

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

                gantt.removeStep(child.getStep());
                beanContainer.removeItem(child);
                this.setCollapsed(child, true);
            }
        }
    }

    public void updateWholeGanttIndexes() {
        if (ganttIndexIsChanged) {
            Collection items = beanContainer.getItemIds();
            for (Object item : items) {
                EventBusFactory.getInstance().post(new GanttEvent.AddGanttItemUpdateToQueue(GanttTreeTable.this, (GanttItemWrapper) item));
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
        List items = beanContainer.getItemIds();
        for (Object itemId : items) {
            GanttItemWrapper item = (GanttItemWrapper) itemId;
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
        List items = beanContainer.getItemIds();
        for (Object itemId : items) {
            GanttItemWrapper item = (GanttItemWrapper) itemId;
            if (assignmentId.intValue() == item.getId().intValue() && assignmentType.equals(item.getType())) {
                return item;
            }
        }
        return null;
    }

    private class GanttContextMenu extends ContextMenu {
        GanttContextMenu() {
            super(GanttTreeTable.this, true);
            this.addContextMenuOpenListener(new ContextMenuOpenListener() {
                @Override
                public void onContextMenuOpen(ContextMenuOpenEvent event) {
                    TableContextClickEvent tableContextClickEvent = (TableContextClickEvent) event.getContextClickEvent();
                    GanttItemWrapper itemId = (GanttItemWrapper) tableContextClickEvent.getItemId();
                    if (itemId != null) {
                        displayContextMenu(itemId);
                    }
                }
            });
        }

        void displayContextMenu(final GanttItemWrapper ganttItemWrapper) {
            this.removeItems();
            MenuItem detailMenuItem = this.addItem("Detail", new Command() {
                @Override
                public void menuSelected(MenuItem menuItem) {
                    if (ganttItemWrapper.isTask()) {
                        if (ganttItemWrapper.getId() == null) {
                            //New task, save then go to the task detail view
                            Task newTask = ganttItemWrapper.buildNewTask();
                            ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                            taskService.saveWithSession(newTask, AppContext.getUsername());
                            ganttItemWrapper.setId(newTask.getId());
                            EventBusFactory.getInstance().post(new TaskEvent.GotoRead(GanttTreeTable.this, newTask.getId()));
                        } else {
                            EventBusFactory.getInstance().post(new TaskEvent.GotoRead(GanttTreeTable.this, ganttItemWrapper.getId()));
                        }
                    } else if (ganttItemWrapper.isMilestone()) {
                        EventBusFactory.getInstance().post(new MilestoneEvent.GotoRead(GanttTreeTable.this, ganttItemWrapper.getId()));
                    } else if (ganttItemWrapper.isBug()) {
                        EventBusFactory.getInstance().post(new BugEvent.GotoRead(GanttTreeTable.this,
                                ganttItemWrapper.getId()));
                    }
                }
            });
            detailMenuItem.setIcon(FontAwesome.BARS);

            MenuItem predecessorMenuItem = this.addItem("Predecessors", new Command() {
                @Override
                public void menuSelected(MenuItem menuItem) {
                    if (ganttItemWrapper.isTask()) {
                        UI.getCurrent().addWindow(new PredecessorWindow(GanttTreeTable.this, ganttItemWrapper));
                    } else {
                        NotificationUtil.showWarningNotification("Can not edit predecessors for milestone");
                    }
                }
            });
            predecessorMenuItem.setIcon(FontAwesome.MAP_MARKER);
            predecessorMenuItem.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));

            MenuItem indentMenuItem = this.addItem("Indent", new Command() {
                @Override
                public void menuSelected(MenuItem menuItem) {
                    GanttItemWrapper preItemWrapper = beanContainer.prevItemId(ganttItemWrapper);
                    if (preItemWrapper != null && preItemWrapper != ganttItemWrapper.getParent()) {
                        ganttItemWrapper.updateParentRelationship(preItemWrapper);
                        preItemWrapper.calculateDatesByChildTasks();
                        beanContainer.setChildrenAllowed(preItemWrapper, true);
                        beanContainer.setParent(ganttItemWrapper, preItemWrapper);
                        GanttTreeTable.this.setCollapsed(preItemWrapper, false);
                        GanttTreeTable.this.refreshRowCache();
                        EventBusFactory.getInstance().post(new GanttEvent.AddGanttItemUpdateToQueue
                                (GanttTreeTable.this, ganttItemWrapper));
                    }
                }
            });
            indentMenuItem.setIcon(FontAwesome.INDENT);
            indentMenuItem.setEnabled(ganttItemWrapper.isIndentable() && CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));

            MenuItem outdentMenuItem = this.addItem("Outdent", new Command() {
                @Override
                public void menuSelected(MenuItem menuItem) {
                    GanttItemWrapper parent = ganttItemWrapper.getParent();
                    if (parent != null) {
                        GanttTreeTable.this.setParent(ganttItemWrapper, parent.getParent());
                        ganttItemWrapper.updateParentRelationship(parent.getParent());
                        GanttTreeTable.this.setCollapsed(ganttItemWrapper, false);
                        // Set all below tasks of taskWrapper have parent is taskWrapper
                        GanttItemWrapper nextItem = (GanttItemWrapper) beanContainer.nextItemId(ganttItemWrapper);
                        while (nextItem != null && nextItem.getParent() == parent) {
                            nextItem.updateParentRelationship(ganttItemWrapper);
                            beanContainer.setChildrenAllowed(ganttItemWrapper, true);
                            beanContainer.setParent(nextItem, ganttItemWrapper);
                            EventBusFactory.getInstance().post(new GanttEvent.AddGanttItemUpdateToQueue(GanttTreeTable.this, nextItem));
                        }

                        if (ganttItemWrapper.hasSubTasks()) {
                            ganttItemWrapper.calculateDatesByChildTasks();
                        }
                        beanContainer.setChildrenAllowed(ganttItemWrapper, ganttItemWrapper.hasSubTasks());
                        parent.calculateDatesByChildTasks();
                        beanContainer.setChildrenAllowed(parent, parent.hasSubTasks());

                        if (parent.getParent() != null) {
                            parent.getParent().calculateDatesByChildTasks();
                        }
                        GanttTreeTable.this.refreshRowCache();
                        EventBusFactory.getInstance().post(new GanttEvent.AddGanttItemUpdateToQueue
                                (GanttTreeTable.this, ganttItemWrapper));
                    }
                }
            });
            outdentMenuItem.setIcon(FontAwesome.OUTDENT);
            outdentMenuItem.setEnabled(ganttItemWrapper.isOutdentable() && CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));

            if (beanContainer.indexOfId(ganttItemWrapper) > 0) {
                MenuItem inserRowBeforeMenuItem = this.addItem("Insert row before", new Command() {
                    @Override
                    public void menuSelected(MenuItem menuItem) {
                        int index = beanContainer.indexOfId(ganttItemWrapper);
                        if (index > 0) {
                            TaskGanttItem newTask = new TaskGanttItem();
                            newTask.setType(ProjectTypeConstants.TASK);
                            newTask.setPrjId(ganttItemWrapper.getTask().getPrjId());
                            newTask.setName("New Task");
                            newTask.setProgress(0d);
                            newTask.setsAccountId(AppContext.getAccountId());
                            GanttItemWrapper newGanttItem = new GanttItemWrapper(gantt, newTask);
                            newGanttItem.setGanttIndex(index + 1);
                            GanttItemWrapper prevItem = beanContainer.prevItemId(ganttItemWrapper);
                            beanContainer.addItemAfter(prevItem, newGanttItem);
                            gantt.addTask(index, newGanttItem);
                            beanContainer.setChildrenAllowed(newGanttItem, newGanttItem.hasSubTasks());

                            if (ganttItemWrapper.getParent() != null) {
                                GanttItemWrapper parentTask = ganttItemWrapper.getParent();
                                beanContainer.setParent(newGanttItem, parentTask);
                                newGanttItem.updateParentRelationship(parentTask);
                                parentTask.calculateDatesByChildTasks();
                            }
                        }

                        calculateWholeGanttIndexes();
                        GanttTreeTable.this.refreshRowCache();
                    }
                });
                inserRowBeforeMenuItem.setIcon(FontAwesome.PLUS_CIRCLE);
                inserRowBeforeMenuItem.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));
            }

            MenuItem insertRowAfterMenuItem = this.addItem("Insert row after", new Command() {
                @Override
                public void menuSelected(MenuItem menuItem) {
                    int index = beanContainer.indexOfId(ganttItemWrapper) + 1;
                    TaskGanttItem newTask = new TaskGanttItem();
                    newTask.setType(ProjectTypeConstants.TASK);
                    newTask.setPrjId(ganttItemWrapper.getTask().getPrjId());
                    newTask.setName("New Task");
                    newTask.setProgress(0d);
                    newTask.setsAccountId(AppContext.getAccountId());
                    GanttItemWrapper newGanttItem = new GanttItemWrapper(gantt, newTask);
                    newGanttItem.setGanttIndex(index);
                    gantt.addTask(index, newGanttItem);

                    if (ganttItemWrapper.hasSubTasks()) {
                        newGanttItem.updateParentRelationship(ganttItemWrapper);
                        beanContainer.setParent(newGanttItem, ganttItemWrapper);
                        beanContainer.addItemAfter(ganttItemWrapper, newGanttItem);
                        ganttItemWrapper.calculateDatesByChildTasks();
                    } else if (ganttItemWrapper.getParent() != null) {
                        GanttItemWrapper parentTask = ganttItemWrapper.getParent();
                        newGanttItem.updateParentRelationship(parentTask);
                        beanContainer.setParent(newGanttItem, parentTask);
                        beanContainer.addItemAfter(ganttItemWrapper, newGanttItem);
                        parentTask.calculateDatesByChildTasks();
                    }
                    beanContainer.setChildrenAllowed(newGanttItem, newGanttItem.hasSubTasks());
                    calculateWholeGanttIndexes();
                    refreshRowCache();
                }
            });
            insertRowAfterMenuItem.setIcon(FontAwesome.PLUS_CIRCLE);
            insertRowAfterMenuItem.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS));

            MenuItem deleteRowMenuItem = this.addItem("Delete row", new Command() {
                @Override
                public void menuSelected(MenuItem menuItem) {
                    ConfirmDialogExt.show(UI.getCurrent(),
                            AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                            AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_MULTIPLE_ITEMS_MESSAGE),
                            AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                            AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                            new ConfirmDialog.Listener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void onClose(ConfirmDialog dialog) {
                                    if (dialog.isConfirmed()) {
                                        removeAssignments(ganttItemWrapper);
                                    }
                                }
                            });
                }
            });
            deleteRowMenuItem.setIcon(FontAwesome.TRASH_O);
            deleteRowMenuItem.setEnabled(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.TASKS));
        }

        private void removeAssignments(GanttItemWrapper task) {
            EventBusFactory.getInstance().post(new GanttEvent.DeleteGanttItemUpdateToQueue(GanttTreeTable.this, task));
            gantt.removeStep(task.getStep());
            gantt.markAsDirtyRecursive();

            GanttItemWrapper parentTask = task.getParent();
            if (parentTask != null) {
                parentTask.removeSubTask(task);
                beanContainer.setChildrenAllowed(parentTask, parentTask.hasSubTasks());
            }

            if (task.hasSubTasks()) {
                Iterator<GanttItemWrapper> iter = task.subTasks().iterator();
                while (iter.hasNext()) {
                    GanttItemWrapper subTask = iter.next();
                    iter.remove();
                    removeAssignments(subTask);
                }
            }
            beanContainer.removeItem(task);
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
