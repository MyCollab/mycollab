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
package com.esofthead.mycollab.mobile.module.crm.view.opportunity;

import java.util.Set;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.mobile.module.crm.events.ContactEvent;
import com.esofthead.mycollab.mobile.module.crm.events.LeadEvent;
import com.esofthead.mycollab.mobile.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmGenericPresenter;
import com.esofthead.mycollab.mobile.ui.ConfirmDialog;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.SimpleCall;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleMeeting;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.SimpleTask;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.RelatedListHandler;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class OpportunityReadPresenter extends
		CrmGenericPresenter<OpportunityReadView> {
	private static final long serialVersionUID = -1981281467054000670L;

	public OpportunityReadPresenter() {
		super(OpportunityReadView.class);
	}

	@Override
	protected void postInitView() {
		view.getPreviewFormHandlers().addFormHandler(
				new DefaultPreviewFormHandler<SimpleOpportunity>() {
					@Override
					public void onEdit(SimpleOpportunity data) {
						EventBusFactory.getInstance().post(
								new OpportunityEvent.GotoEdit(this, data));
					}

					@Override
					public void onAdd(SimpleOpportunity data) {
						EventBusFactory.getInstance().post(
								new OpportunityEvent.GotoAdd(this, null));
					}

					@Override
					public void onDelete(final SimpleOpportunity data) {
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
											OpportunityService OpportunityService = ApplicationContextUtil
													.getSpringBean(OpportunityService.class);
											OpportunityService.removeWithSession(
													data.getId(),
													AppContext.getUsername(),
													AppContext.getAccountId());
											EventBusFactory
													.getInstance()
													.post(new OpportunityEvent.GotoList(
															this, null));
										}
									}
								});
					}

					@Override
					public void onClone(SimpleOpportunity data) {
						SimpleOpportunity cloneData = (SimpleOpportunity) data
								.copy();
						cloneData.setId(null);
						EventBusFactory.getInstance().post(
								new OpportunityEvent.GotoEdit(this, cloneData));
					}

					@Override
					public void onCancel() {
						EventBusFactory.getInstance().post(
								new OpportunityEvent.GotoList(this, null));
					}

					@Override
					public void gotoNext(SimpleOpportunity data) {
						OpportunityService opportunityService = ApplicationContextUtil
								.getSpringBean(OpportunityService.class);
						OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
						criteria.setSaccountid(new NumberSearchField(AppContext
								.getAccountId()));
						criteria.setId(new NumberSearchField(data.getId(),
								NumberSearchField.GREATER));
						Integer nextId = opportunityService
								.getNextItemKey(criteria);
						if (nextId != null) {
							EventBusFactory.getInstance()
									.post(new OpportunityEvent.GotoRead(this,
											nextId));
						} else {
							NotificationUtil.showGotoLastRecordNotification();
						}

					}

					@Override
					public void gotoPrevious(SimpleOpportunity data) {
						OpportunityService opportunityService = ApplicationContextUtil
								.getSpringBean(OpportunityService.class);
						OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
						criteria.setSaccountid(new NumberSearchField(AppContext
								.getAccountId()));
						criteria.setId(new NumberSearchField(data.getId(),
								NumberSearchField.LESSTHAN));
						Integer nextId = opportunityService
								.getPreviousItemKey(criteria);
						if (nextId != null) {
							EventBusFactory.getInstance()
									.post(new OpportunityEvent.GotoRead(this,
											nextId));
						} else {
							NotificationUtil.showGotoFirstRecordNotification();
						}
					}
				});
		view.getRelatedContactHandlers().addRelatedListHandler(
				new RelatedListHandler<SimpleContact>() {

					@Override
					public void selectAssociateItems(Set<SimpleContact> items) {
						// TODO Auto-generated method stub

					}

					@Override
					public void createNewRelatedItem(String itemId) {
						SimpleContact contact = new SimpleContact();
						contact.setExtraData(view.getItem());
						EventBusFactory
								.getInstance()
								.post(new ContactEvent.GotoEdit(
										OpportunityReadPresenter.this, contact));
					}
				});
		view.getRelatedLeadHandlers().addRelatedListHandler(
				new RelatedListHandler<SimpleLead>() {

					@Override
					public void selectAssociateItems(Set<SimpleLead> items) {
						// TODO Auto-generated method stub

					}

					@Override
					public void createNewRelatedItem(String itemId) {
						SimpleLead lead = new SimpleLead();
						lead.setExtraData(view.getItem());
						EventBusFactory.getInstance().post(
								new LeadEvent.GotoEdit(
										OpportunityReadPresenter.this, lead));
					}
				});
		view.getRelatedActivityHandlers().addRelatedListHandler(
				new RelatedListHandler<SimpleActivity>() {

					@Override
					public void selectAssociateItems(Set<SimpleActivity> items) {
						// TODO Auto-generated method stub

					}

					@Override
					public void createNewRelatedItem(String itemId) {
						if (itemId.equals(CrmTypeConstants.TASK)) {
							final SimpleTask task = new SimpleTask();
							task.setType(CrmTypeConstants.ACCOUNT);
							task.setTypeid(view.getItem().getId());
							EventBusFactory
									.getInstance()
									.post(new ActivityEvent.TaskEdit(
											OpportunityReadPresenter.this, task));
						} else if (itemId.equals(CrmTypeConstants.MEETING)) {
							final SimpleMeeting meeting = new SimpleMeeting();
							meeting.setType(CrmTypeConstants.ACCOUNT);
							meeting.setTypeid(view.getItem().getId());
							EventBusFactory.getInstance().post(
									new ActivityEvent.MeetingEdit(
											OpportunityReadPresenter.this,
											meeting));
						} else if (itemId.equals(CrmTypeConstants.CALL)) {
							final SimpleCall call = new SimpleCall();
							call.setType(CrmTypeConstants.ACCOUNT);
							call.setTypeid(view.getItem().getId());
							EventBusFactory
									.getInstance()
									.post(new ActivityEvent.CallEdit(
											OpportunityReadPresenter.this, call));
						}
					}
				});
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (AppContext.canRead(RolePermissionCollections.CRM_OPPORTUNITY)) {

			if (data.getParams() instanceof Integer) {
				OpportunityService opportunityService = ApplicationContextUtil
						.getSpringBean(OpportunityService.class);
				SimpleOpportunity opportunity = opportunityService.findById(
						(Integer) data.getParams(), AppContext.getAccountId());
				if (opportunity != null) {
					super.onGo(container, data);
					view.previewItem(opportunity);

					AppContext.addFragment(
							CrmLinkGenerator
									.generateOpportunityPreviewLink(opportunity
											.getId()), AppContext.getMessage(
									GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
									"Opportunity",
									opportunity.getOpportunityname()));
				} else {
					NotificationUtil.showRecordNotExistNotification();
					return;
				}
			}
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}
}
