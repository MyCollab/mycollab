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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmGenericPresenter;
import com.esofthead.mycollab.mobile.ui.ConfirmDialog;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.domain.MeetingWithBLOBs;
import com.esofthead.mycollab.module.crm.domain.SimpleMeeting;
import com.esofthead.mycollab.module.crm.domain.criteria.MeetingSearchCriteria;
import com.esofthead.mycollab.module.crm.service.MeetingService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class MeetingReadPresenter extends CrmGenericPresenter<MeetingReadView> {
	private static final long serialVersionUID = 464611840929906889L;

	public MeetingReadPresenter() {
		super(MeetingReadView.class);
	}

	@Override
	protected void postInitView() {
		view.getPreviewFormHandlers().addFormHandler(
				new DefaultPreviewFormHandler<SimpleMeeting>() {
					@Override
					public void onEdit(SimpleMeeting data) {
						EventBusFactory.getInstance().post(
								new ActivityEvent.MeetingEdit(this, data));
					}

					@Override
					public void onDelete(final SimpleMeeting data) {
						ConfirmDialog.show(
								UI.getCurrent(),
								AppContext
										.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
								AppContext
										.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
								AppContext
										.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
								new ConfirmDialog.CloseListener() {
									private static final long serialVersionUID = 1L;

									@Override
									public void onClose(ConfirmDialog dialog) {
										if (dialog.isConfirmed()) {
											MeetingService campaignService = ApplicationContextUtil
													.getSpringBean(MeetingService.class);
											campaignService.removeWithSession(
													data.getId(),
													AppContext.getUsername(),
													AppContext.getAccountId());
											EventBusFactory.getInstance().post(
													new ActivityEvent.GotoList(
															this, null));
										}
									}
								});
					}

					@Override
					public void onClone(SimpleMeeting data) {
						MeetingWithBLOBs cloneData = (MeetingWithBLOBs) data
								.copy();
						cloneData.setId(null);
						EventBusFactory.getInstance().post(
								new ActivityEvent.MeetingEdit(this, cloneData));
					}

					@Override
					public void onCancel() {
						EventBusFactory.getInstance().post(
								new ActivityEvent.GotoList(this, null));
					}

					@Override
					public void gotoNext(SimpleMeeting data) {
						MeetingService accountService = ApplicationContextUtil
								.getSpringBean(MeetingService.class);
						MeetingSearchCriteria criteria = new MeetingSearchCriteria();
						criteria.setSaccountid(new NumberSearchField(AppContext
								.getAccountId()));
						criteria.setId(new NumberSearchField(data.getId(),
								NumberSearchField.GREATER));
						Integer nextId = accountService
								.getNextItemKey(criteria);
						if (nextId != null) {
							EventBusFactory.getInstance()
									.post(
											new ActivityEvent.MeetingRead(this,
													nextId));
						} else {
							NotificationUtil.showGotoLastRecordNotification();
						}

					}

					@Override
					public void gotoPrevious(SimpleMeeting data) {
						MeetingService accountService = ApplicationContextUtil
								.getSpringBean(MeetingService.class);
						MeetingSearchCriteria criteria = new MeetingSearchCriteria();
						criteria.setSaccountid(new NumberSearchField(AppContext
								.getAccountId()));
						criteria.setId(new NumberSearchField(data.getId(),
								NumberSearchField.LESSTHAN));
						Integer nextId = accountService
								.getPreviousItemKey(criteria);
						if (nextId != null) {
							EventBusFactory.getInstance()
									.post(
											new ActivityEvent.MeetingRead(this,
													nextId));
						} else {
							NotificationUtil.showGotoFirstRecordNotification();
						}
					}
				});
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (AppContext.canRead(RolePermissionCollections.CRM_MEETING)) {

			SimpleMeeting meeting = null;
			if (data.getParams() instanceof Integer) {
				MeetingService meetingService = ApplicationContextUtil
						.getSpringBean(MeetingService.class);
				meeting = meetingService.findById((Integer) data.getParams(),
						AppContext.getAccountId());
				if (meeting == null) {
					NotificationUtil.showRecordNotExistNotification();
					return;
				}
			} else {
				throw new MyCollabException("Invalid data: " + data);
			}

			super.onGo(container, data);

			view.previewItem(meeting);

			AppContext.addFragment(CrmLinkGenerator
					.generateMeetingPreviewLink(meeting.getId()), AppContext
					.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
							"Meeting", meeting.getSubject()));
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}
}
