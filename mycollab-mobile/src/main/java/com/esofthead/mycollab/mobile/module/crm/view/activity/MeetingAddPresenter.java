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
package com.esofthead.mycollab.mobile.module.crm.view.activity;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmGenericPresenter;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.module.crm.domain.MeetingWithBLOBs;
import com.esofthead.mycollab.module.crm.service.MeetingService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class MeetingAddPresenter extends CrmGenericPresenter<MeetingAddView> {
	private static final long serialVersionUID = -3427352135962459534L;

	public MeetingAddPresenter() {
		super(MeetingAddView.class);
	}

	@Override
	protected void postInitView() {
		view.getEditFormHandlers().addFormHandler(
				new EditFormHandler<MeetingWithBLOBs>() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onSave(final MeetingWithBLOBs item) {
						save(item);
						EventBusFactory.getInstance().post(
								new ShellEvent.NavigateBack(this, null));
					}

					@Override
					public void onCancel() {
					}

					@Override
					public void onSaveAndNew(final MeetingWithBLOBs item) {
						save(item);
						EventBusFactory.getInstance().post(
								new ActivityEvent.MeetingAdd(this, null));
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
				MeetingService meetingService = ApplicationContextUtil
						.getSpringBean(MeetingService.class);
				meeting = meetingService.findByPrimaryKey(
						(Integer) data.getParams(), AppContext.getAccountId());
				if (meeting == null) {
					NotificationUtil.showRecordNotExistNotification();
					return;
				}
			}

			super.onGo(container, data);

			view.editItem(meeting);

			if (meeting.getId() == null) {
				AppContext.addFragment("crm/activity/meeting/add/", AppContext
						.getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE,
								"Meeting"));
			} else {
				AppContext.addFragment("crm/activity/meeting/edit/"
						+ UrlEncodeDecoder.encode(meeting.getId()), AppContext
						.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
								"Meeting", meeting.getSubject()));
			}
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

	public void save(MeetingWithBLOBs item) {
		MeetingService meetingService = ApplicationContextUtil
				.getSpringBean(MeetingService.class);

		item.setSaccountid(AppContext.getAccountId());
		if (item.getId() == null) {
			meetingService.saveWithSession(item, AppContext.getUsername());
		} else {
			meetingService.updateWithSession(item, AppContext.getUsername());
		}
	}
}
