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
package com.mycollab.module.project.view.task;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.query.NumberParam;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.mycollab.module.project.events.TaskEvent;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.reporting.FormReportLayout;
import com.mycollab.reporting.PrintButton;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class TaskReadPresenter extends AbstractPresenter<TaskReadView> {
    private static final long serialVersionUID = 1L;

    public TaskReadPresenter() {
        super(TaskReadView.class);
    }

    @Override
    protected void postInitView() {
        this.view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleTask>() {

            @Override
            public void onAssign(SimpleTask data) {
                UI.getCurrent().addWindow(new AssignTaskWindow(data));
            }

            @Override
            public void onEdit(SimpleTask data) {
                EventBusFactory.getInstance().post(new TaskEvent.GotoEdit(this, data));
            }

            @Override
            public void onAdd(SimpleTask data) {
                EventBusFactory.getInstance().post(new TaskEvent.GotoAdd(this, null));
            }

            @Override
            public void onPrint(Object source, SimpleTask data) {
                PrintButton btn = (PrintButton) source;
                btn.doPrint(data, new FormReportLayout(ProjectTypeConstants.TASK, Task.Field.taskname.name(),
                        TaskDefaultFormLayoutFactory.getForm(), Task.Field.taskname.name(), Task.Field.id.name(),
                        Task.Field.parenttaskid.name(), "selected"));
            }

            @Override
            public void onDelete(final SimpleTask data) {
                ConfirmDialogExt.show(UI.getCurrent(),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                                taskService.removeWithSession(data, AppContext.getUsername(), AppContext.getAccountId());
                                EventBusFactory.getInstance().post(new TaskEvent.GotoDashboard(this, null));
                            }
                        });
            }

            @Override
            public void onClone(SimpleTask data) {
                Task cloneData = (Task) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new TaskEvent.GotoEdit(this, cloneData));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new TaskEvent.GotoDashboard(this, null));
            }

            @Override
            public void gotoNext(SimpleTask task) {
                ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);

                TaskSearchCriteria criteria = new TaskSearchCriteria();
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                criteria.addExtraField(TaskSearchCriteria.p_taskkey.buildSearchField(SearchField.AND, NumberParam
                        .GREATER_THAN, task.getTaskkey()));
                Integer nextId = taskService.getNextItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new TaskEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }
            }

            @Override
            public void gotoPrevious(final SimpleTask task) {
                ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);

                TaskSearchCriteria criteria = new TaskSearchCriteria();
                criteria.setProjectId(new NumberSearchField(task.getProjectid()));
                criteria.addExtraField(TaskSearchCriteria.p_taskkey.buildSearchField(SearchField.AND, NumberParam.LESS_THAN,
                        task.getTaskkey()));
                Integer nextId = taskService.getNextItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new TaskEvent.GotoRead(this, nextId));
                } else {
                    NotificationUtil.showGotoFirstRecordNotification();
                }
            }
        });
    }

    @Override
    protected void onGo(final ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.TASKS)) {
            TaskContainer taskContainer = (TaskContainer) container;
            taskContainer.removeAllComponents();

            taskContainer.addComponent(view);
            if (data.getParams() instanceof Integer) {
                ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                SimpleTask task = taskService.findById((Integer) data.getParams(), AppContext.getAccountId());

                if (task != null) {
                    view.previewItem(task);
                    ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
                    breadCrumb.gotoTaskRead(task);
                } else {
                    NotificationUtil.showRecordNotExistNotification();
                }
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
