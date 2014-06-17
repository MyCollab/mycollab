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
package com.esofthead.mycollab.module.crm.view.opportunity;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import org.vaadin.dialogs.ConfirmDialog;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.ContactOpportunity;
import com.esofthead.mycollab.module.crm.domain.OpportunityLead;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.SimpleCall;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleContactOpportunityRel;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleMeeting;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.SimpleTask;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.module.crm.events.ContactEvent;
import com.esofthead.mycollab.module.crm.events.LeadEvent;
import com.esofthead.mycollab.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.module.crm.view.CrmGenericPresenter;
import com.esofthead.mycollab.module.crm.view.CrmToolbar;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.AbstractRelatedListHandler;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class OpportunityReadPresenter extends
		CrmGenericPresenter<OpportunityReadView> {

	private static final long serialVersionUID = 1L;

	public OpportunityReadPresenter() {
		super(OpportunityReadView.class);
	}

	@Override
	protected void postInitView() {
		view.getPreviewFormHandlers().addFormHandler(
				new DefaultPreviewFormHandler<SimpleOpportunity>() {
					@Override
					public void onEdit(SimpleOpportunity data) {
						EventBus.getInstance().fireEvent(
								new OpportunityEvent.GotoEdit(this, data));
					}

					@Override
					public void onAdd(SimpleOpportunity data) {
						EventBus.getInstance().fireEvent(
								new OpportunityEvent.GotoAdd(this, null));
					}

					@Override
					public void onDelete(final SimpleOpportunity data) {
						ConfirmDialogExt.show(
								UI.getCurrent(),
								AppContext.getMessage(
										GenericI18Enum.DELETE_DIALOG_TITLE,
										SiteConfiguration.getSiteName()),
								AppContext
										.getMessage(GenericI18Enum.CONFIRM_DELETE_RECORD_DIALOG_MESSAGE),
								AppContext
										.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
								AppContext
										.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
								new ConfirmDialog.Listener() {
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
											EventBus.getInstance()
													.fireEvent(
															new OpportunityEvent.GotoList(
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
						EventBus.getInstance().fireEvent(
								new OpportunityEvent.GotoEdit(this, cloneData));
					}

					@Override
					public void onCancel() {
						EventBus.getInstance().fireEvent(
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
							EventBus.getInstance()
									.fireEvent(
											new OpportunityEvent.GotoRead(this,
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
							EventBus.getInstance()
									.fireEvent(
											new OpportunityEvent.GotoRead(this,
													nextId));
						} else {
							NotificationUtil.showGotoFirstRecordNotification();
						}
					}
				});

		view.getRelatedActivityHandlers().addRelatedListHandler(
				new AbstractRelatedListHandler<SimpleActivity>() {
					@Override
					public void createNewRelatedItem(String itemId) {
						if (itemId.equals("task")) {
							SimpleTask task = new SimpleTask();
							task.setType(CrmTypeConstants.OPPORTUNITY);
							task.setTypeid(view.getItem().getId());
							EventBus.getInstance()
									.fireEvent(
											new ActivityEvent.TaskEdit(
													OpportunityReadPresenter.this,
													task));
						} else if (itemId.equals("meeting")) {
							SimpleMeeting meeting = new SimpleMeeting();
							meeting.setType(CrmTypeConstants.OPPORTUNITY);
							meeting.setTypeid(view.getItem().getId());
							EventBus.getInstance().fireEvent(
									new ActivityEvent.MeetingEdit(
											OpportunityReadPresenter.this,
											meeting));
						} else if (itemId.equals("call")) {
							SimpleCall call = new SimpleCall();
							call.setType(CrmTypeConstants.OPPORTUNITY);
							call.setTypeid(view.getItem().getId());
							EventBus.getInstance()
									.fireEvent(
											new ActivityEvent.CallEdit(
													OpportunityReadPresenter.this,
													call));
						}
					}
				});

		view.getRelatedContactHandlers().addRelatedListHandler(
				new AbstractRelatedListHandler<SimpleContactOpportunityRel>() {

					@Override
					public void createNewRelatedItem(String itemId) {
						SimpleContact contact = new SimpleContact();
						contact.setExtraData(view.getItem());
						EventBus.getInstance()
								.fireEvent(
										new ContactEvent.GotoEdit(
												OpportunityReadPresenter.this,
												contact));
					}

					@Override
					public void selectAssociateItems(
							Set<SimpleContactOpportunityRel> items) {
						List<ContactOpportunity> associateContacts = new ArrayList<ContactOpportunity>();
						SimpleOpportunity opportunity = view.getItem();
						for (SimpleContact contact : items) {
							ContactOpportunity associateContact = new ContactOpportunity();
							associateContact.setContactid(contact.getId());
							associateContact.setOpportunityid(opportunity
									.getId());
							associateContact
									.setCreatedtime(new GregorianCalendar()
											.getTime());
							associateContacts.add(associateContact);
						}

						ContactService contactService = ApplicationContextUtil
								.getSpringBean(ContactService.class);
						contactService.saveContactOpportunityRelationship(
								associateContacts, AppContext.getAccountId());
						view.getRelatedContactHandlers().refresh();
					}
				});

		view.getRelatedLeadHandlers().addRelatedListHandler(
				new AbstractRelatedListHandler<SimpleLead>() {
					@Override
					public void createNewRelatedItem(String itemId) {
						SimpleLead lead = new SimpleLead();
						lead.setExtraData(view.getItem());
						EventBus.getInstance().fireEvent(
								new LeadEvent.GotoEdit(
										OpportunityReadPresenter.this, lead));
					}

					@Override
					public void selectAssociateItems(Set<SimpleLead> items) {
						SimpleOpportunity opportunity = view.getItem();
						List<OpportunityLead> associateLeads = new ArrayList<OpportunityLead>();
						for (SimpleLead lead : items) {
							OpportunityLead associateLead = new OpportunityLead();
							associateLead.setLeadid(lead.getId());
							associateLead.setOpportunityid(opportunity.getId());
							associateLead
									.setCreatedtime(new GregorianCalendar()
											.getTime());
							associateLeads.add(associateLead);
						}

						OpportunityService opportunityService = ApplicationContextUtil
								.getSpringBean(OpportunityService.class);
						opportunityService.saveOpportunityLeadRelationship(
								associateLeads, AppContext.getAccountId());
						view.getRelatedLeadHandlers().refresh();
					}
				});
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (AppContext.canRead(RolePermissionCollections.CRM_OPPORTUNITY)) {
			CrmToolbar crmToolbar = ViewManager.getView(CrmToolbar.class);
			crmToolbar.gotoItem(AppContext
					.getMessage(CrmCommonI18nEnum.TOOLBAR_OPPORTUNTIES_HEADER));

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
