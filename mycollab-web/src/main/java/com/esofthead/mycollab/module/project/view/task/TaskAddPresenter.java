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

import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.core.SecureAccessException;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.project.view.ProjectGenericPresenter;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.module.project.view.parameters.TaskScreenData;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.IEditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.*;
import com.esofthead.mycollab.vaadin.web.ui.field.AttachmentUploadField;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class TaskAddPresenter extends ProjectGenericPresenter<TaskAddView> {
    private static final long serialVersionUID = 1L;

    public TaskAddPresenter() {
        super(TaskAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<SimpleTask>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(SimpleTask item) {
                int taskId = save(item);
                EventBusFactory.getInstance().post(new TaskEvent.GotoRead(this, taskId));
            }

            @Override
            public void onCancel() {
                PageActionChain chain = new PageActionChain(new ProjectScreenData.Goto
                        (CurrentProjectVariables.getProjectId()), new TaskScreenData.GotoDashboard());
                EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain));
            }

            @Override
            public void onSaveAndNew(final SimpleTask item) {
                save(item);
                EventBusFactory.getInstance().post(new TaskEvent.GotoAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
            TaskContainer taskContainer = (TaskContainer) container;
            taskContainer.navigateToContainer(ProjectTypeConstants.TASK);
            taskContainer.removeAllComponents();
            taskContainer.addComponent(view.getWidget());
            SimpleTask task = (SimpleTask) data.getParams();

            ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            if (task.getId() == null) {
                breadCrumb.gotoTaskAdd();
                task.setPriority(OptionI18nEnum.TaskPriority.Medium.name());
            } else {
                breadCrumb.gotoTaskEdit(task);
            }
            view.editItem(task);
        } else {
            throw new SecureAccessException();
        }
    }

    private int save(Task item) {
        ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);

        item.setSaccountid(AppContext.getAccountId());
        item.setProjectid(CurrentProjectVariables.getProjectId());
        if (item.getPercentagecomplete() == null) {
            item.setPercentagecomplete(new Double(0));
        } else if (item.getPercentagecomplete().doubleValue() == 100d) {
            item.setStatus(StatusI18nEnum.Closed.name());
        }

        if (item.getStatus() == null) {
            item.setStatus(StatusI18nEnum.Open.name());
        }

        if (item.getId() == null) {
            item.setLogby(AppContext.getUsername());
            int taskId = taskService.saveWithSession(item, AppContext.getUsername());
            AttachmentUploadField uploadField = view.getAttachUploadField();
            String attachPath = AttachmentUtils.getProjectEntityAttachmentPath(
                    AppContext.getAccountId(), CurrentProjectVariables.getProjectId(),
                    ProjectTypeConstants.TASK, "" + taskId);
            uploadField.saveContentsToRepo(attachPath);
        } else {
            taskService.updateWithSession(item, AppContext.getUsername());
            AttachmentUploadField uploadField = view.getAttachUploadField();
            String attachPath = AttachmentUtils.getProjectEntityAttachmentPath(
                    AppContext.getAccountId(), CurrentProjectVariables.getProjectId(),
                    ProjectTypeConstants.TASK, "" + item.getId());
            uploadField.saveContentsToRepo(attachPath);
        }
        return item.getId();
    }
}
