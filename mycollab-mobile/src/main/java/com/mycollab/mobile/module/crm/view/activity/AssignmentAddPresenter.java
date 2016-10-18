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
package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.MyCollabException;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.crm.events.ActivityEvent;
import com.mycollab.mobile.module.crm.view.AbstractCrmPresenter;
import com.mycollab.mobile.shell.events.ShellEvent;
import com.mycollab.module.crm.domain.CrmTask;
import com.mycollab.module.crm.i18n.TaskI18nEnum;
import com.mycollab.module.crm.service.TaskService;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.DefaultEditFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class AssignmentAddPresenter extends AbstractCrmPresenter<AssignmentAddView> {
    private static final long serialVersionUID = -8546619959063314947L;

    public AssignmentAddPresenter() {
        super(AssignmentAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new DefaultEditFormHandler<CrmTask>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final CrmTask item) {
                save(item);
                EventBusFactory.getInstance().post(new ShellEvent.NavigateBack(this, null));
            }

            @Override
            public void onSaveAndNew(final CrmTask item) {
                save(item);
                EventBusFactory.getInstance().post(new ActivityEvent.TaskAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (UserUIContext.canWrite(RolePermissionCollections.CRM_TASK)) {

            CrmTask task;
            if (data.getParams() instanceof CrmTask) {
                task = (CrmTask) data.getParams();
            } else if (data.getParams() instanceof Integer) {
                TaskService taskService = AppContextUtil.getSpringBean(TaskService.class);
                task = taskService.findByPrimaryKey((Integer) data.getParams(), MyCollabUI.getAccountId());
                if (task == null) {
                    NotificationUtil.showRecordNotExistNotification();
                    return;
                }
            } else {
                throw new MyCollabException("Do not support param data: " + data);
            }

            super.onGo(container, data);
            view.editItem(task);

            if (task.getId() == null) {
                MyCollabUI.addFragment("crm/activity/task/add/", UserUIContext.getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE,
                        UserUIContext.getMessage(TaskI18nEnum.SINGLE)));
            } else {
                MyCollabUI.addFragment("crm/activity/task/edit/" + UrlEncodeDecoder.encode(task.getId()),
                        UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                                UserUIContext.getMessage(TaskI18nEnum.SINGLE), task.getSubject()));
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    public void save(CrmTask item) {
        TaskService taskService = AppContextUtil.getSpringBean(TaskService.class);
        item.setSaccountid(MyCollabUI.getAccountId());
        if (item.getId() == null) {
            taskService.saveWithSession(item, UserUIContext.getUsername());
        } else {
            taskService.updateWithSession(item, UserUIContext.getUsername());
        }

    }
}
