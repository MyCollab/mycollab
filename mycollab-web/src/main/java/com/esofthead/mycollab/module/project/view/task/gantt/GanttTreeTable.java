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

import com.esofthead.mycollab.configuration.Storage;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TreeTable;

import java.util.List;
import java.util.UUID;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class GanttTreeTable extends TreeTable {
    private GanttExt gantt;
    private BeanItemContainer<GanttItemWrapper> beanContainer;

    private String sortField = "createdTime";

    private ApplicationEventListener<TaskEvent.GanttTaskUpdate> taskUpdateHandler = new
            ApplicationEventListener<TaskEvent.GanttTaskUpdate>() {
                @Override
                @Subscribe
                public void handle(TaskEvent.GanttTaskUpdate event) {
                    GanttItemWrapper ganttItemWrapper = (GanttItemWrapper) event.getData();
                    updateTaskTree(ganttItemWrapper);
                }
            };

    public GanttTreeTable(final GanttExt gantt) {
        super();
        this.gantt = gantt;
        this.setWidth("800px");
        gantt.setVerticalScrollDelegateTarget(this);
        beanContainer = new BeanItemContainer<>(GanttItemWrapper.class);
        this.setContainerDataSource(beanContainer);
        this.setVisibleColumns("name", "startDate", "endDate", "duration", "actualStartDate", "actualEndDate");
        this.setColumnHeader("name", "Task");
        this.setColumnExpandRatio("name", 1.0f);
        this.setColumnHeader("startDate", "Start");
        this.setColumnWidth("startDate", 75);
        this.setColumnHeader("endDate", "End");
        this.setColumnWidth("endDate", 75);
        this.setColumnHeader("duration", "Duration");
        this.setColumnWidth("duration", 80);
        this.setColumnHeader("actualStartDate", "Actual Start");
        this.setColumnWidth("actualStartDate", 80);
        this.setColumnHeader("actualEndDate", "Actual End");
        this.setColumnWidth("actualEndDate", 80);
        this.setColumnCollapsingAllowed(true);
        this.setColumnCollapsed("actualStartDate", true);
        this.setColumnCollapsed("actualEndDate", true);
        this.setSelectable(true);
        this.setSortEnabled(true);

        this.addGeneratedColumn("name", new ColumnGenerator() {
            @Override
            public Object generateCell(Table table, Object itemId, Object columnId) {
                GanttItemWrapper item = (GanttItemWrapper) itemId;
                SimpleTask task = item.getTask();

                String taskLinkContent;
                String uid = UUID.randomUUID().toString();
                String taskPriority = task.getPriority();
                Img priorityLink = new Img(taskPriority, ProjectResources.getIconResourceLink12ByTaskPriority
                        (taskPriority)).setTitle(taskPriority);

                String linkName = String.format("[#%d] - %s", task.getTaskkey(), task.getTaskname());
                A taskLink = new A().setId("tag" + uid).appendText(linkName).setStyle("display:inline");

                taskLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(uid, ProjectTypeConstants.TASK, task.getId() + ""));
                taskLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));

                String avatarLink = Storage.getAvatarPath(task.getAssignUserAvatarId(), 16);
                Img avatarImg = new Img(task.getAssignUserFullName(), avatarLink).setTitle(task.getAssignUserFullName());

                Div resultDiv = new DivLessFormatter().appendChild(priorityLink, DivLessFormatter.EMPTY_SPACE(),
                        avatarImg, DivLessFormatter.EMPTY_SPACE(), taskLink, DivLessFormatter.EMPTY_SPACE(),
                        TooltipHelper.buildDivTooltipEnable(uid));
                taskLinkContent = resultDiv.write();

                Label taskLbl = new Label(taskLinkContent, ContentMode.HTML);
                if (task.isCompleted()) {
                    taskLbl.addStyleName("completed");
                } else if (task.isOverdue()) {
                    taskLbl.addStyleName("overdue");
                }
                return taskLbl;
            }
        });

        this.addGeneratedColumn("startDate", new ColumnGenerator() {
            @Override
            public Object generateCell(Table table, Object itemId, Object columnId) {
                GanttItemWrapper item = (GanttItemWrapper) itemId;
                return new Label(AppContext.formatDate(item.getStartDate()));
            }
        });

        this.addGeneratedColumn("endDate", new ColumnGenerator() {
            @Override
            public Object generateCell(Table table, Object itemId, Object columnId) {
                GanttItemWrapper item = (GanttItemWrapper) itemId;
                return new Label(AppContext.formatDate(item.getEndDate()));
            }
        });

        this.addGeneratedColumn("actualEndDate", new ColumnGenerator() {
            @Override
            public Object generateCell(Table table, Object itemId, Object columnId) {
                GanttItemWrapper item = (GanttItemWrapper) itemId;
                return new Label(AppContext.formatDate(item.getActualEndDate()));
            }
        });

        this.addGeneratedColumn("actualStartDate", new ColumnGenerator() {
            @Override
            public Object generateCell(Table table, Object itemId, Object columnId) {
                GanttItemWrapper item = (GanttItemWrapper) itemId;
                return new Label(AppContext.formatDate(item.getActualStartDate()));
            }
        });

        this.addGeneratedColumn("duration", new ColumnGenerator() {
            @Override
            public Object generateCell(Table table, Object itemId, Object columnId) {
                GanttItemWrapper item = (GanttItemWrapper) itemId;
                double dur = item.getDuration();
                return new Label(dur + " d");
            }
        });

        this.addExpandListener(new Tree.ExpandListener() {
            @Override
            public void nodeExpand(Tree.ExpandEvent expandEvent) {
                GanttItemWrapper item = (GanttItemWrapper) expandEvent.getItemId();
                List<GanttItemWrapper> subTasks = item.subTasks(new SearchCriteria.OrderField(sortField, SearchCriteria.ASC));
                insertSteps(item, subTasks);
            }
        });

        this.addCollapseListener(new Tree.CollapseListener() {
            @Override
            public void nodeCollapse(Tree.CollapseEvent collapseEvent) {
                GanttItemWrapper item = (GanttItemWrapper) collapseEvent.getItemId();
                List<GanttItemWrapper> subTasks = item.subTasks(new SearchCriteria.OrderField(sortField, SearchCriteria.ASC));
                removeSubSteps(item, subTasks);
            }
        });

        this.addHeaderClickListener(new HeaderClickListener() {
            @Override
            public void headerClick(HeaderClickEvent event) {
                String propertyId = (String) event.getPropertyId();
                sortField = propertyId;
                beanContainer.sort(new String[]{propertyId}, new boolean[]{true});
                List<GanttItemWrapper> items = beanContainer.getItemIds();
                gantt.removeSteps();
                for (GanttItemWrapper task : items) {
                    gantt.addTask(task);
                }
            }
        });
    }

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(taskUpdateHandler);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(taskUpdateHandler);
        super.detach();
    }

    private void updateTaskTree(GanttItemWrapper ganttItemWrapper) {
        this.markAsDirtyRecursive();
    }

    public void addTask(GanttItemWrapper itemWrapper) {
        beanContainer.addBean(itemWrapper);
        this.setChildrenAllowed(itemWrapper, itemWrapper.hasSubTasks());
    }

    void insertSteps(final GanttItemWrapper parent, final List<GanttItemWrapper> childs) {
        final int stepIndex = gantt.getStepIndex(parent.getStep());
        int count = 0;
        if (stepIndex != -1) {
            for (GanttItemWrapper child : childs) {
                this.addItem(child);
                this.setParent(child, parent);
                this.setChildrenAllowed(child, child.hasSubTasks());
                gantt.addTask(stepIndex + count + 1, child);
                count++;
            }
        }
    }

    void removeSubSteps(final GanttItemWrapper parent, final List<GanttItemWrapper> childs) {
        final int stepIndex = gantt.getStepIndex(parent.getStep());
        if (stepIndex != -1) {
            for (GanttItemWrapper child : childs) {
                this.removeItem(child);
                gantt.removeStep(child.getStep());
            }
        }
    }
}
