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

import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.domain.MeetingWithBLOBs;
import com.mycollab.module.crm.events.ActivityEvent;
import com.mycollab.module.crm.i18n.MeetingI18nEnum;
import com.mycollab.module.crm.service.MeetingService;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.events.IEditFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MeetingAddPresenter extends CrmGenericPresenter<MeetingAddView> {
    private static final long serialVersionUID = 1L;

    public MeetingAddPresenter() {
        super(MeetingAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<MeetingWithBLOBs>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final MeetingWithBLOBs item) {
                save(item);
                EventBusFactory.getInstance().post(new ActivityEvent.MeetingRead(this, item.getId()));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new ActivityEvent.GotoTodoList(this, null));
            }

            @Override
            public void onSaveAndNew(final MeetingWithBLOBs item) {
                save(item);
                EventBusFactory.getInstance().post(new ActivityEvent.MeetingAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (AppContext.canWrite(RolePermissionCollections.CRM_MEETING)) {
            MeetingWithBLOBs meeting = null;
            if (data.getParams() instanceof MeetingWithBLOBs) {
                meeting = (MeetingWithBLOBs) data.getParams();
            } else if (data.getParams() instanceof Integer) {
                MeetingService meetingService = AppContextUtil.getSpringBean(MeetingService.class);
                meeting = meetingService.findByPrimaryKey((Integer) data.getParams(), AppContext.getAccountId());
            }
            if (meeting == null) {
                NotificationUtil.showRecordNotExistNotification();
                return;
            }
            super.onGo(container, data);

            view.editItem(meeting);

            if (meeting.getId() == null) {
                AppContext.addFragment("crm/activity/meeting/add/", AppContext.getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE,
                        AppContext.getMessage(MeetingI18nEnum.SINGLE)));
            } else {
                AppContext.addFragment("crm/activity/meeting/edit/" + UrlEncodeDecoder.encode(meeting.getId()),
                        AppContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                                AppContext.getMessage(MeetingI18nEnum.SINGLE), meeting.getSubject()));
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    public void save(MeetingWithBLOBs item) {
        MeetingService meetingService = AppContextUtil.getSpringBean(MeetingService.class);
        item.setSaccountid(AppContext.getAccountId());
        if (item.getId() == null) {
            meetingService.saveWithSession(item, AppContext.getUsername());
        } else {
            meetingService.updateWithSession(item, AppContext.getUsername());
        }
    }
}
