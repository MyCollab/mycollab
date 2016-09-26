/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.view.task;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.project.events.TaskEvent;
import com.mycollab.mobile.module.project.view.AbstractProjectPresenter;
import com.mycollab.mobile.shell.events.ShellEvent;
import com.mycollab.mobile.ui.ConfirmDialog;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public class TaskReadPresenter extends AbstractProjectPresenter<TaskReadView> {
    private static final long serialVersionUID = 1L;

    public TaskReadPresenter() {
        super(TaskReadView.class);
    }

    @Override
    protected void postInitView() {
        this.view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleTask>() {

            @Override
            public void onEdit(final SimpleTask data) {
                EventBusFactory.getInstance().post(new TaskEvent.GotoEdit(this, data));
            }

            @Override
            public void onDelete(final SimpleTask data) {
                ConfirmDialog.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                        dialog -> {
                            if (dialog.isConfirmed()) {
                                ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                                taskService.removeWithSession(data, UserUIContext.getUsername(), MyCollabUI.getAccountId());
                                EventBusFactory.getInstance().post(new ShellEvent.NavigateBack(this, null));
                            }
                        });
            }

            @Override
            public void onClone(final SimpleTask data) {
                final Task cloneData = (Task) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new TaskEvent.GotoEdit(this, cloneData));
            }
        });
    }

    @Override
    protected void onGo(final ComponentContainer container, final ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.TASKS)) {
            if (data.getParams() instanceof Integer) {
                ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                SimpleTask task = taskService.findById((Integer) data.getParams(), MyCollabUI.getAccountId());

                if (task != null) {
                    this.view.previewItem(task);
                    super.onGo(container, data);

                    MyCollabUI.addFragment(ProjectLinkGenerator.generateTaskPreviewLink(task.getTaskkey(),
                            task.getProjectShortname()), task.getName());
                } else {
                    NotificationUtil.showRecordNotExistNotification();
                }
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
