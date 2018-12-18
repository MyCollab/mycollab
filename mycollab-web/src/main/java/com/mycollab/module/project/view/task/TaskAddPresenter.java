/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.task;

import com.mycollab.common.domain.MonitorItem;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.common.service.MonitorItemService;
import com.mycollab.core.SecureAccessException;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.event.TaskEvent;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.project.view.ProjectGenericPresenter;
import com.mycollab.module.project.view.ticket.TicketContainer;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.IEditFormHandler;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField;
import com.vaadin.ui.HasComponents;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
                EventBusFactory.getInstance().post(new TicketEvent.GotoDashboard(this, null));
            }

            @Override
            public void onSaveAndNew(final SimpleTask item) {
                save(item);
                EventBusFactory.getInstance().post(new TaskEvent.GotoAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
            TicketContainer ticketContainer = (TicketContainer) container;
            ticketContainer.navigateToContainer(ProjectTypeConstants.TASK);
            ticketContainer.setContent(view);
            SimpleTask task = (SimpleTask) data.getParams();

            ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            if (task.getId() == null) {
                breadCrumb.gotoTaskAdd();
            } else {
                breadCrumb.gotoTaskEdit(task);
            }
            view.editItem(task);
        } else {
            throw new SecureAccessException();
        }
    }

    private int save(Task item) {
        ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);

        item.setSaccountid(AppUI.getAccountId());
        item.setProjectid(CurrentProjectVariables.getProjectId());
        if (item.getPercentagecomplete() == null) {
            item.setPercentagecomplete(0d);
        } else if (item.getPercentagecomplete() == 100d) {
            item.setStatus(StatusI18nEnum.Closed.name());
        }

        if (item.getStatus() == null) {
            item.setStatus(StatusI18nEnum.Open.name());
        }

        if (item.getId() == null) {
            item.setCreateduser(UserUIContext.getUsername());
            int taskId = taskService.saveWithSession(item, UserUIContext.getUsername());

            List<String> followers = view.getFollowers();
            if (followers.size() > 0) {
                List<MonitorItem> monitorItems = new ArrayList<>();
                for (String follower : followers) {
                    MonitorItem monitorItem = new MonitorItem();
                    monitorItem.setMonitorDate(LocalDateTime.now());
                    monitorItem.setSaccountid(AppUI.getAccountId());
                    monitorItem.setType(ProjectTypeConstants.TASK);
                    monitorItem.setTypeid(taskId);
                    monitorItem.setUser(follower);
                    monitorItem.setExtratypeid(CurrentProjectVariables.getProjectId());
                    monitorItems.add(monitorItem);
                }
                MonitorItemService monitorItemService = AppContextUtil.getSpringBean(MonitorItemService.class);
                monitorItemService.saveMonitorItems(monitorItems);
            }
        } else {
            taskService.updateWithSession(item, UserUIContext.getUsername());
        }
        AttachmentUploadField uploadField = view.getAttachUploadField();
        String attachPath = AttachmentUtils.getProjectEntityAttachmentPath(AppUI.getAccountId(), item.getProjectid(),
                ProjectTypeConstants.TASK, "" + item.getId());
        uploadField.saveContentsToRepo(attachPath);
        return item.getId();
    }
}
