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

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.domain.TaskList;
import com.esofthead.mycollab.module.project.domain.criteria.TaskListSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskListEvent;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.LoadPolicy;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class TaskGroupReadPresenter extends AbstractPresenter<TaskGroupReadView> {

    public TaskGroupReadPresenter() {
        super(TaskGroupReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(
                new DefaultPreviewFormHandler<SimpleTaskList>() {

                    @Override
                    public void onAssign(SimpleTaskList data) {
                        UI.getCurrent().addWindow(new AssignTaskGroupWindow(data));
                    }

                    @Override
                    public void onAdd(SimpleTaskList data) {
                        EventBusFactory.getInstance().post(new TaskListEvent.GotoAdd(this, null));
                    }

                    @Override
                    public void onEdit(SimpleTaskList data) {
                        EventBusFactory.getInstance().post(new TaskListEvent.GotoEdit(this, data));
                    }

                    @Override
                    public void onDelete(SimpleTaskList data) {
                        ProjectTaskListService taskListService = ApplicationContextUtil.getSpringBean(ProjectTaskListService.class);
                        taskListService.removeWithSession(data,
                                AppContext.getUsername(), AppContext.getAccountId());
                        EventBusFactory.getInstance().post(new TaskListEvent.GotoTaskListScreen(this, null));
                    }

                    @Override
                    public void onClone(SimpleTaskList data) {
                        TaskList cloneData = (TaskList) data.copy();
                        cloneData.setId(null);
                        EventBusFactory.getInstance().post(new TaskListEvent.GotoEdit(this, cloneData));
                    }

                    @Override
                    public void onCancel() {
                        EventBusFactory.getInstance().post(new TaskListEvent.GotoTaskListScreen(this, null));
                    }

                    @Override
                    public void gotoNext(SimpleTaskList data) {
                        ProjectTaskListService tasklistService = ApplicationContextUtil
                                .getSpringBean(ProjectTaskListService.class);
                        TaskListSearchCriteria criteria = new TaskListSearchCriteria();
                        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                        criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER));
                        Integer nextId = tasklistService.getNextItemKey(criteria);
                        if (nextId != null) {
                            EventBusFactory.getInstance().post(new TaskListEvent.GotoRead(this, nextId));
                        } else {
                            NotificationUtil.showGotoLastRecordNotification();
                        }

                    }

                    @Override
                    public void gotoPrevious(SimpleTaskList data) {
                        ProjectTaskListService tasklistService = ApplicationContextUtil.getSpringBean(ProjectTaskListService.class);
                        TaskListSearchCriteria criteria = new TaskListSearchCriteria();
                        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                        criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESSTHAN));
                        Integer nextId = tasklistService.getPreviousItemKey(criteria);
                        if (nextId != null) {
                            EventBusFactory.getInstance().post(new TaskListEvent.GotoRead(this, nextId));
                        } else {
                            NotificationUtil.showGotoFirstRecordNotification();
                        }
                    }
                });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables
                .canRead(ProjectRolePermissionCollections.TASKS)) {
            TaskContainer taskContainer = (TaskContainer) container;
            taskContainer.removeAllComponents();

            taskContainer.addComponent(view.getWidget());

            if (data.getParams() instanceof Integer) {
                ProjectTaskListService taskService = ApplicationContextUtil
                        .getSpringBean(ProjectTaskListService.class);
                SimpleTaskList taskgroup = taskService.findById(
                        (Integer) data.getParams(), AppContext.getAccountId());
                view.previewItem(taskgroup);

                ProjectBreadcrumb breadCrumb = ViewManager
                        .getCacheComponent(ProjectBreadcrumb.class);
                breadCrumb.gotoTaskGroupRead(taskgroup);
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
