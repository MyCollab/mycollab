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
package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.AssignWithPredecessors;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.domain.TaskPredecessor;
import com.esofthead.mycollab.module.project.events.GanttEvent;
import com.esofthead.mycollab.module.project.service.GanttAssignmentService;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.project.view.task.gantt.GanttItemWrapper;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.LoadPolicy;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.web.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.google.common.eventbus.Subscribe;
import com.vaadin.ui.ComponentContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class GanttChartViewPresenter extends AbstractPresenter<GanttChartView> {
    private static final long serialVersionUID = 1L;

    private static Logger LOG = LoggerFactory.getLogger(GanttChartViewPresenter.class);

    private GanttAssignmentService ganttAssignmentService = ApplicationContextUtil.getSpringBean(GanttAssignmentService.class);

    private Set<AssignWithPredecessors> queueSetTasksUpdate;
    private Set<AssignWithPredecessors> queueSetTasksDelete;
    private ApplicationEventListener<GanttEvent.ClearGanttItemsNeedUpdate> massUpdateGanttItemsUpdateHandler = new
            ApplicationEventListener<GanttEvent.ClearGanttItemsNeedUpdate>() {
                @Override
                @Subscribe
                public void handle(GanttEvent.ClearGanttItemsNeedUpdate event) {
                    massUpdateTasksInfoInQueue();
                }
            };

    private ApplicationEventListener<GanttEvent.AddGanttItemUpdateToQueue> addTaskToQueueHandler = new
            ApplicationEventListener<GanttEvent.AddGanttItemUpdateToQueue>() {
                @Override
                @Subscribe
                public void handle(GanttEvent.AddGanttItemUpdateToQueue event) {
                    GanttItemWrapper item = (GanttItemWrapper) event.getData();
                    if (item.getId() == null) {
                        if (item.isTask()) {
                            Task newTask = item.buildNewTask();
                            ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                            taskService.saveWithSession(newTask, AppContext.getUsername());
                            item.setId(newTask.getId());
                        } else {
                            LOG.error("Milestone with id is null");
                        }
                        return;
                    }
                    if (!queueSetTasksDelete.contains(item.getTask())) {
                        queueSetTasksUpdate.add(item.getTask());
                    }
                }
            };

    private ApplicationEventListener<GanttEvent.DeleteGanttItemUpdateToQueue> deleteTaskToQueueHandler = new
            ApplicationEventListener<GanttEvent.DeleteGanttItemUpdateToQueue>() {
                @Subscribe
                @Override
                public void handle(GanttEvent.DeleteGanttItemUpdateToQueue event) {
                    GanttItemWrapper item = (GanttItemWrapper) event.getData();
                    if (queueSetTasksUpdate.contains(item.getTask())) {
                        queueSetTasksUpdate.remove(item.getTask());
                    }
                    queueSetTasksDelete.add(item.getTask());
                }
            };

    private ApplicationEventListener<GanttEvent.ModifyPredecessors> predecessorsModifyHandler = new
            ApplicationEventListener<GanttEvent.ModifyPredecessors>() {
                @Override
                @Subscribe
                public void handle(GanttEvent.ModifyPredecessors event) {
                    GanttItemWrapper ganttItemWrapper = (GanttItemWrapper) event.getSource();
                    List<TaskPredecessor> predecessors = (List<TaskPredecessor>) event.getData();
                    ganttItemWrapper.adjustTaskDatesByPredecessors(predecessors);
                    ganttAssignmentService.massUpdatePredecessors(ganttItemWrapper.getId(), predecessors, AppContext.getAccountId());
                    ganttItemWrapper.getTask().setPredecessors(predecessors);
                    view.getTaskTable().refreshRowCache();
                }
            };

    public GanttChartViewPresenter() {
        super(GanttChartView.class);
    }

    @Override
    protected void viewAttached() {
        queueSetTasksUpdate = new HashSet<>();
        queueSetTasksDelete = new HashSet<>();
        EventBusFactory.getInstance().register(addTaskToQueueHandler);
        EventBusFactory.getInstance().register(massUpdateGanttItemsUpdateHandler);
        EventBusFactory.getInstance().register(predecessorsModifyHandler);
        EventBusFactory.getInstance().register(deleteTaskToQueueHandler);
    }

    @Override
    protected void viewDetached() {
        EventBusFactory.getInstance().unregister(addTaskToQueueHandler);
        EventBusFactory.getInstance().unregister(massUpdateGanttItemsUpdateHandler);
        EventBusFactory.getInstance().unregister(predecessorsModifyHandler);
        EventBusFactory.getInstance().unregister(deleteTaskToQueueHandler);
        massUpdateTasksInfoInQueue();
        massDeleteTasksInQueue();
    }

    private void massUpdateTasksInfoInQueue() {
        if (queueSetTasksUpdate.size() > 0) {
            try {
                ganttAssignmentService.massUpdateGanttItems(new ArrayList<>(queueSetTasksUpdate), AppContext.getAccountId());
            } finally {
                queueSetTasksUpdate.clear();
            }
        }
    }

    private void massDeleteTasksInQueue() {
        if (queueSetTasksDelete.size() > 0) {
            try {
                ganttAssignmentService.massDeleteGanttItems(new ArrayList<>(queueSetTasksDelete), AppContext.getAccountId());
            } finally {
                queueSetTasksDelete.clear();
            }
        }
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.TASKS)) {
            TaskContainer taskContainer = (TaskContainer) container;
            taskContainer.navigateToContainer(ProjectTypeConstants.TASK);
            taskContainer.removeAllComponents();
            taskContainer.addComponent(view.getWidget());
            view.lazyLoadView();

            ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            breadCrumb.gotoGanttView();
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}

