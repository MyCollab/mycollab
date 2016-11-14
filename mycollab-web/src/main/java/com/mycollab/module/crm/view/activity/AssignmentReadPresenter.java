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
package com.mycollab.module.crm.view.activity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.domain.CrmTask;
import com.mycollab.module.crm.domain.SimpleCrmTask;
import com.mycollab.module.crm.domain.criteria.CrmTaskSearchCriteria;
import com.mycollab.module.crm.event.ActivityEvent;
import com.mycollab.module.crm.i18n.TaskI18nEnum;
import com.mycollab.module.crm.service.TaskService;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class AssignmentReadPresenter extends CrmGenericPresenter<AssignmentReadView> {
    private static final long serialVersionUID = 1L;

    public AssignmentReadPresenter() {
        super(AssignmentReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleCrmTask>() {
            @Override
            public void onEdit(SimpleCrmTask data) {
                EventBusFactory.getInstance().post(new ActivityEvent.TaskEdit(this, data));
            }

            @Override
            public void onAdd(SimpleCrmTask data) {
                EventBusFactory.getInstance().post(new ActivityEvent.TaskAdd(this, null));
            }

            @Override
            public void onDelete(final SimpleCrmTask data) {
                ConfirmDialogExt.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, MyCollabUI.getSiteName()),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                TaskService taskService = AppContextUtil.getSpringBean(TaskService.class);
                                taskService.removeWithSession(data,
                                        UserUIContext.getUsername(), MyCollabUI.getAccountId());
                                EventBusFactory.getInstance().post(new ActivityEvent.GotoTodoList(this, null));
                            }
                        });
            }

            @Override
            public void onClone(SimpleCrmTask data) {
                CrmTask cloneData = (CrmTask) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new ActivityEvent.TaskEdit(this, cloneData));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new ActivityEvent.GotoTodoList(this, null));
            }

            @Override
            public void gotoNext(SimpleCrmTask data) {
                TaskService taskService = AppContextUtil.getSpringBean(TaskService.class);
                CrmTaskSearchCriteria criteria = new CrmTaskSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.GREATER()));
                Integer nextId = taskService.getNextItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new ActivityEvent.TaskRead(this, nextId));
                } else {
                    NotificationUtil.showGotoLastRecordNotification();
                }

            }

            @Override
            public void gotoPrevious(SimpleCrmTask data) {
                TaskService taskService = AppContextUtil.getSpringBean(TaskService.class);
                CrmTaskSearchCriteria criteria = new CrmTaskSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                criteria.setId(new NumberSearchField(data.getId(), NumberSearchField.LESS_THAN()));
                Integer nextId = taskService.getPreviousItemKey(criteria);
                if (nextId != null) {
                    EventBusFactory.getInstance().post(new ActivityEvent.TaskRead(this, nextId));
                } else {
                    NotificationUtil.showGotoFirstRecordNotification();
                }
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (UserUIContext.canRead(RolePermissionCollections.CRM_TASK)) {
            SimpleCrmTask task;
            if (data.getParams() instanceof Integer) {
                TaskService taskService = AppContextUtil.getSpringBean(TaskService.class);
                task = taskService.findById((Integer) data.getParams(), MyCollabUI.getAccountId());
                if (task == null) {
                    NotificationUtil.showRecordNotExistNotification();
                    return;
                }
            } else {
                throw new MyCollabException("Invalid data " + data);
            }

            super.onGo(container, data);
            view.previewItem(task);
            MyCollabUI.addFragment(CrmLinkGenerator.generateTaskPreviewLink(task.getId()),
                    UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                            UserUIContext.getMessage(TaskI18nEnum.SINGLE), task.getSubject()));

        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
