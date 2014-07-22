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
package com.esofthead.mycollab.module.crm.view;

import java.util.Iterator;

import org.vaadin.hene.popupbutton.PopupButton;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.events.AccountEvent;
import com.esofthead.mycollab.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.module.crm.events.CampaignEvent;
import com.esofthead.mycollab.module.crm.events.CaseEvent;
import com.esofthead.mycollab.module.crm.events.ContactEvent;
import com.esofthead.mycollab.module.crm.events.CrmEvent;
import com.esofthead.mycollab.module.crm.events.CrmSettingEvent;
import com.esofthead.mycollab.module.crm.events.DocumentEvent;
import com.esofthead.mycollab.module.crm.events.LeadEvent;
import com.esofthead.mycollab.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CallI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CaseI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.ContactI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.MeetingI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.TaskI18nEnum;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.ButtonLink;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent
public class CrmToolbar extends HorizontalLayout implements PageView {
	private static final long serialVersionUID = 1L;

	private final PopupButton addBtn;

	public CrmToolbar() {
		super();
		this.setStyleName("crm-toolbar");
		this.setWidth("100%");
		this.setMargin(new MarginInfo(false, true, false, true));
		final NavigatorItemListener listener = new NavigatorItemListener();
		final Button homeBtn = new Button(
				AppContext
						.getMessage(CrmCommonI18nEnum.TOOLBAR_DASHBOARD_HEADER),
				listener);
		homeBtn.setStyleName("link");
		addComponent(homeBtn);

		final Button accountList = new Button(
				AppContext
						.getMessage(CrmCommonI18nEnum.TOOLBAR_ACCOUNTS_HEADER),
				listener);
		accountList.setEnabled(AppContext
				.canRead(RolePermissionCollections.CRM_ACCOUNT));
		accountList.setStyleName("link");
		addComponent(accountList);

		final Button contactList = new Button(
				AppContext
						.getMessage(CrmCommonI18nEnum.TOOLBAR_CONTACTS_HEADER),
				listener);
		contactList.setEnabled(AppContext
				.canRead(RolePermissionCollections.CRM_CONTACT));
		contactList.setStyleName("link");
		addComponent(contactList);

		final Button campaignList = new Button(
				AppContext
						.getMessage(CrmCommonI18nEnum.TOOLBAR_CAMPAIGNS_HEADER),
				listener);
		campaignList.setEnabled(AppContext
				.canRead(RolePermissionCollections.CRM_CAMPAIGN));
		campaignList.setStyleName("link");
		addComponent(campaignList);

		final Button leadList = new Button(
				AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_LEADS_HEADER),
				listener);
		leadList.setEnabled(AppContext
				.canRead(RolePermissionCollections.CRM_LEAD));
		leadList.setStyleName("link");
		addComponent(leadList);

		final Button opportunityList = new Button(
				AppContext
						.getMessage(CrmCommonI18nEnum.TOOLBAR_OPPORTUNTIES_HEADER),
				listener);
		opportunityList.setEnabled(AppContext
				.canRead(RolePermissionCollections.CRM_OPPORTUNITY));
		opportunityList.setStyleName("link");
		addComponent(opportunityList);

		final Button caseList = new Button(
				AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_CASES_HEADER),
				listener);
		caseList.setEnabled(AppContext
				.canRead(RolePermissionCollections.CRM_CASE));
		caseList.setStyleName("link");
		addComponent(caseList);

		final Button activitiesList = new Button(
				AppContext
						.getMessage(CrmCommonI18nEnum.TOOLBAR_ACTIVITIES_HEADER),
				listener);
		final boolean isActivityEnable = AppContext
				.canRead(RolePermissionCollections.CRM_MEETING)
				|| AppContext.canRead(RolePermissionCollections.CRM_TASK)
				|| AppContext.canRead(RolePermissionCollections.CRM_CALL);
		activitiesList.setEnabled(isActivityEnable);
		activitiesList.setStyleName("link");
		addComponent(activitiesList);

		addStyleName("h-sidebar-menu");

		final Button fileBtn = new Button(
				AppContext
						.getMessage(CrmCommonI18nEnum.TOOLBAR_DOCUMENT_HEADER),
				listener);
		fileBtn.setEnabled(AppContext
				.canRead(RolePermissionCollections.CRM_DOCUMENT));
		fileBtn.setStyleName("link");
		addComponent(fileBtn);

		final Button notificationBtn = new Button(
				AppContext
						.getMessage(CrmCommonI18nEnum.TOOLBAR_CRMNOTIFICATION_HEADER),
				listener);
		notificationBtn.setStyleName("link");
		addComponent(notificationBtn);

		addBtn = new PopupButton(
				AppContext.getMessage(CrmCommonI18nEnum.BUTTON_CREATE));
		addBtn.setIcon(MyCollabResource.newResource("icons/18/create.png"));
		final GridLayout addBtnLayout = new GridLayout(3, 2);
		addBtnLayout.setMargin(true);
		addBtnLayout.setWidth("370px");
		addBtnLayout.setSpacing(true);

		final ButtonLink newAccountBtn = new ButtonLink(
				AppContext.getMessage(AccountI18nEnum.BUTTON_NEW_ACCOUNT),
				listener, false);
		newAccountBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_ACCOUNT));
		newAccountBtn.setIcon(MyCollabResource
				.newResource("icons/18/crm/account.png"));
		addBtnLayout.addComponent(newAccountBtn);

		final ButtonLink newContactBtn = new ButtonLink(
				AppContext.getMessage(ContactI18nEnum.BUTTON_NEW_CONTACT),
				listener, false);
		newContactBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_CONTACT));
		newContactBtn.setIcon(MyCollabResource
				.newResource("icons/18/crm/contact.png"));
		addBtnLayout.addComponent(newContactBtn);

		final ButtonLink newCampaignBtn = new ButtonLink(
				AppContext.getMessage(CampaignI18nEnum.BUTTON_NEW_CAMPAIGN),
				listener, false);
		newCampaignBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_CAMPAIGN));
		newCampaignBtn.setIcon(MyCollabResource
				.newResource("icons/18/crm/campaign.png"));
		addBtnLayout.addComponent(newCampaignBtn);

		final ButtonLink newOpportunityBtn = new ButtonLink(
				AppContext
						.getMessage(OpportunityI18nEnum.BUTTON_NEW_OPPORTUNITY),
				listener, false);
		newOpportunityBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_OPPORTUNITY));
		newOpportunityBtn.setIcon(MyCollabResource
				.newResource("icons/18/crm/opportunity.png"));
		addBtnLayout.addComponent(newOpportunityBtn);

		final ButtonLink newLeadBtn = new ButtonLink(
				AppContext.getMessage(LeadI18nEnum.BUTTON_NEW_LEAD), listener,
				false);
		newLeadBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_LEAD));
		newLeadBtn.setIcon(MyCollabResource
				.newResource("icons/18/crm/lead.png"));
		addBtnLayout.addComponent(newLeadBtn);

		final ButtonLink newCaseBtn = new ButtonLink(
				AppContext.getMessage(CaseI18nEnum.BUTTON_NEW_CASE), listener,
				false);
		newCaseBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_CASE));
		newCaseBtn.setIcon(MyCollabResource
				.newResource("icons/18/crm/case.png"));
		addBtnLayout.addComponent(newCaseBtn);

		final ButtonLink newTaskBtn = new ButtonLink(
				AppContext.getMessage(TaskI18nEnum.BUTTON_NEW_TASK), listener,
				false);
		newTaskBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_TASK));
		newTaskBtn.setIcon(MyCollabResource
				.newResource("icons/18/crm/task.png"));
		addBtnLayout.addComponent(newTaskBtn);

		final ButtonLink newCallBtn = new ButtonLink(
				AppContext.getMessage(CallI18nEnum.BUTTON_NEW_CALL), listener,
				false);
		newCallBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_CALL));
		newCallBtn.setIcon(MyCollabResource
				.newResource("icons/18/crm/call.png"));
		addBtnLayout.addComponent(newCallBtn);

		final ButtonLink newMeetingBtn = new ButtonLink(
				AppContext.getMessage(MeetingI18nEnum.BUTTON_NEW_MEETING),
				listener, false);
		newMeetingBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_MEETING));
		newMeetingBtn.setIcon(MyCollabResource
				.newResource("icons/18/crm/meeting.png"));
		addBtnLayout.addComponent(newMeetingBtn);

		addBtn.setContent(addBtnLayout);
		addBtn.setStyleName("link");
		addComponent(addBtn);

		setExpandRatio(addBtn, 1.0f);
		setComponentAlignment(addBtn, Alignment.MIDDLE_RIGHT);
	}

	private class NavigatorItemListener implements Button.ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(final ClickEvent event) {
			final String caption = event.getButton().getCaption();

			if (AppContext.getMessage(
					CrmCommonI18nEnum.TOOLBAR_DASHBOARD_HEADER).equals(caption)) {
				EventBusFactory.getInstance().post(
						new CrmEvent.GotoHome(this, null));
			} else if (AppContext
					.getMessage(AccountI18nEnum.BUTTON_NEW_ACCOUNT).equals(
							caption)) {
				EventBusFactory.getInstance().post(
						new AccountEvent.GotoAdd(this, null));
			} else if (AppContext.getMessage(
					CrmCommonI18nEnum.TOOLBAR_ACCOUNTS_HEADER).equals(caption)) {
				EventBusFactory.getInstance().post(
						new AccountEvent.GotoList(this, null));
			} else if (AppContext.getMessage(
					CampaignI18nEnum.BUTTON_NEW_CAMPAIGN).equals(caption)) {
				EventBusFactory.getInstance().post(
						new CampaignEvent.GotoAdd(this, null));
			} else if (AppContext.getMessage(
					CrmCommonI18nEnum.TOOLBAR_CAMPAIGNS_HEADER).equals(caption)) {
				EventBusFactory.getInstance().post(
						new CampaignEvent.GotoList(this, null));
			} else if (AppContext.getMessage(CaseI18nEnum.BUTTON_NEW_CASE)
					.equals(caption)) {
				EventBusFactory.getInstance().post(
						new CaseEvent.GotoAdd(this, null));
			} else if (AppContext.getMessage(
					CrmCommonI18nEnum.TOOLBAR_CASES_HEADER).equals(caption)) {
				EventBusFactory.getInstance().post(
						new CaseEvent.GotoList(this, null));
			} else if (AppContext.getMessage(
					CrmCommonI18nEnum.TOOLBAR_CONTACTS_HEADER).equals(caption)) {
				EventBusFactory.getInstance().post(
						new ContactEvent.GotoList(this, null));
			} else if (AppContext
					.getMessage(ContactI18nEnum.BUTTON_NEW_CONTACT).equals(
							caption)) {
				EventBusFactory.getInstance().post(
						new ContactEvent.GotoAdd(this, null));
			} else if (AppContext.getMessage(LeadI18nEnum.BUTTON_NEW_LEAD)
					.equals(caption)) {
				EventBusFactory.getInstance().post(
						new LeadEvent.GotoAdd(this, null));
			} else if (AppContext.getMessage(
					CrmCommonI18nEnum.TOOLBAR_LEADS_HEADER).equals(caption)) {
				EventBusFactory.getInstance().post(
						new LeadEvent.GotoList(this, null));
			} else if (AppContext.getMessage(
					OpportunityI18nEnum.BUTTON_NEW_OPPORTUNITY).equals(caption)) {
				EventBusFactory.getInstance().post(
						new OpportunityEvent.GotoAdd(this, null));
			} else if (AppContext.getMessage(
					CrmCommonI18nEnum.TOOLBAR_OPPORTUNTIES_HEADER).equals(
					caption)) {
				EventBusFactory.getInstance().post(
						new OpportunityEvent.GotoList(this, null));
			} else if (AppContext.getMessage(
					CrmCommonI18nEnum.TOOLBAR_ACTIVITIES_HEADER)
					.equals(caption)) {
				EventBusFactory.getInstance().post(
						new ActivityEvent.GotoCalendar(this, null));
			} else if (AppContext.getMessage(TaskI18nEnum.BUTTON_NEW_TASK)
					.equals(caption)) {
				EventBusFactory.getInstance().post(
						new ActivityEvent.TaskAdd(this, null));
			} else if (AppContext.getMessage(CallI18nEnum.BUTTON_NEW_CALL)
					.equals(caption)) {
				EventBusFactory.getInstance().post(
						new ActivityEvent.CallAdd(this, null));
			} else if (AppContext
					.getMessage(MeetingI18nEnum.BUTTON_NEW_MEETING).equals(
							caption)) {
				EventBusFactory.getInstance().post(
						new ActivityEvent.MeetingAdd(this, null));
			} else if (AppContext.getMessage(
					CrmCommonI18nEnum.TOOLBAR_DOCUMENT_HEADER).equals(caption)) {
				EventBusFactory.getInstance().post(
						new DocumentEvent.GotoDashboard(this, null));
			} else if (AppContext.getMessage(
					CrmCommonI18nEnum.TOOLBAR_CRMNOTIFICATION_HEADER).equals(
					caption)) {
				EventBusFactory.getInstance()
						.post(new CrmSettingEvent.GotoNotificationSetting(this,
								null));
			}

			addBtn.setPopupVisible(false);

			for (final Iterator<com.vaadin.ui.Component> it = CrmToolbar.this
					.iterator(); it.hasNext();) {
				final Button btn = (Button) it.next();
				btn.removeStyleName("isSelected");
			}

			event.getButton().addStyleName("isSelected");
		}
	}

	@Override
	public void addViewListener(ViewListener listener) {
	}

	@Override
	public ComponentContainer getWidget() {
		return this;
	}

	public void gotoItem(final String crmItem) {
		for (final Iterator<com.vaadin.ui.Component> it = this.iterator(); it
				.hasNext();) {
			final Button btn = (Button) it.next();
			if (crmItem.equals(btn.getCaption())) {
				btn.addStyleName("isSelected");
			} else {
				btn.removeStyleName("isSelected");
			}
		}
	}

}
