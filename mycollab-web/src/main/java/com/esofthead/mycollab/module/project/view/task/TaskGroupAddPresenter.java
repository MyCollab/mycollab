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

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.TaskList;
import com.esofthead.mycollab.module.project.events.TaskListEvent;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.*;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TaskGroupAddPresenter extends AbstractPresenter<TaskGroupAddView> {
    private static final long serialVersionUID = 1L;

    public TaskGroupAddPresenter() {
        super(TaskGroupAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(
                new EditFormHandler<TaskList>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onSave(final TaskList item) {
                        int tasklistId = save(item);
                        ViewState viewState = HistoryViewManager.back();
                        if (viewState instanceof NullViewState) {
                            EventBusFactory.getInstance().post(
                                    new TaskListEvent.GotoRead(this,
                                            tasklistId));
                        }
                    }

                    @Override
                    public void onCancel() {
                        ViewState viewState = HistoryViewManager.back();
                        if (viewState instanceof NullViewState) {
                            EventBusFactory.getInstance().post(
                                    new TaskListEvent.GotoTaskListScreen(this,
                                            null));
                        }
                    }

                    @Override
                    public void onSaveAndNew(final TaskList item) {
                        save(item);
                        EventBusFactory.getInstance().post(
                                new TaskListEvent.GotoAdd(this, null));
                    }
                });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.TASKS)) {
            TaskContainer taskContainer = (TaskContainer) container;
            taskContainer.removeAllComponents();

            taskContainer.addComponent(view.getWidget());
            TaskList taskList = (TaskList) data.getParams();
            view.editItem(taskList);

            ProjectBreadcrumb breadCrumb = ViewManager
                    .getCacheComponent(ProjectBreadcrumb.class);
            if (taskList.getId() == null) {
                breadCrumb.gotoTaskGroupAdd();
            } else {
                breadCrumb.gotoTaskGroupEdit(taskList);
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    private int save(TaskList item) {
        ProjectTaskListService taskService = ApplicationContextUtil
                .getSpringBean(ProjectTaskListService.class);

        item.setSaccountid(AppContext.getAccountId());

        if (item.getId() == null) {
            item.setCreateduser(AppContext.getUsername());
            taskService.saveWithSession(item, AppContext.getUsername());
        } else {
            taskService.updateWithSession(item, AppContext.getUsername());
        }
        return item.getId();
    }
}