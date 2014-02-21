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
package com.esofthead.mycollab.module.crm.view.lead;

import static com.esofthead.mycollab.module.crm.ui.components.CrmPreviewFormControlsGenerator.BACK_BTN_PRESENTED;
import static com.esofthead.mycollab.module.crm.ui.components.CrmPreviewFormControlsGenerator.HISTORY_BTN_PRESENTED;
import static com.esofthead.mycollab.module.crm.ui.components.CrmPreviewFormControlsGenerator.NEXT_BTN_PRESENTED;
import static com.esofthead.mycollab.module.crm.ui.components.CrmPreviewFormControlsGenerator.PREVIOUS_BTN_PRESENTED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.events.AccountEvent;
import com.esofthead.mycollab.module.crm.events.ContactEvent;
import com.esofthead.mycollab.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.module.crm.ui.components.CrmPreviewFormControlsGenerator;
import com.esofthead.mycollab.module.crm.ui.components.NoteListItems;
import com.esofthead.mycollab.module.crm.view.activity.ActivityRelatedItemListComp;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AddViewLayout2;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
@ViewComponent
public class LeadConvertReadViewImpl extends AbstractPageView implements
		LeadConvertReadView {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory
			.getLogger(LeadConvertReadViewImpl.class);

	private SimpleLead lead;

	AdvancedPreviewBeanForm<SimpleLead> previewForm;

	private LeadCampaignListComp associateCampaignList;
	private ActivityRelatedItemListComp associateActivityList;
	private NoteListItems noteListItems;

	public LeadConvertReadViewImpl() {
		previewForm = new AdvancedPreviewBeanForm<SimpleLead>() {
			private static final long serialVersionUID = 1L;

			public SimpleLead getBean() {
				return lead;
			}

			@Override
			public void showHistory() {
				LeadHistoryLogWindow historyLog = new LeadHistoryLogWindow(
						ModuleNameConstants.CRM, CrmTypeConstants.LEAD);
				historyLog.loadHistory(lead.getId());
				UI.getCurrent().addWindow(historyLog);
			}
		};
	}

	@Override
	public void displayConvertLeadInfo(final SimpleLead lead) {
		this.removeAllComponents();
		this.lead = lead;
		AddViewLayout2 previewLayout = new AddViewLayout2(
				"You can not access the Lead, it is converted already",
				MyCollabResource.newResource("icons/22/crm/lead.png"));

		HorizontalLayout buttonControls = new CrmPreviewFormControlsGenerator<SimpleLead>(
				previewForm).createButtonControls(BACK_BTN_PRESENTED
				| PREVIOUS_BTN_PRESENTED | NEXT_BTN_PRESENTED
				| HISTORY_BTN_PRESENTED, RolePermissionCollections.CRM_LEAD);

		previewLayout.addControlButtons(buttonControls);

		VerticalLayout convertLeadInfoLayout = new VerticalLayout();
		previewLayout.addBody(convertLeadInfoLayout);

		Label header = new Label("Conversion Details");
		header.addStyleName("h2");
		convertLeadInfoLayout.addComponent(header);

		GridFormLayoutHelper layoutHelper = new GridFormLayoutHelper(1, 3);
		layoutHelper.getLayout().setWidth("100%");
		layoutHelper.getLayout().setMargin(false);
		layoutHelper.getLayout().addStyleName("colored-gridlayout");

		log.debug("Display associate account");
		AccountService accountService = ApplicationContextUtil
				.getSpringBean(AccountService.class);
		final SimpleAccount account = accountService
				.findAccountAssoWithConvertedLead(lead.getId(),
						AppContext.getAccountId());
		if (account != null) {
			Button accountLink = new Button(account.getAccountname(),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							EventBus.getInstance().fireEvent(
									new AccountEvent.GotoRead(this, account
											.getId()));

						}
					});
			accountLink.setIcon(MyCollabResource
					.newResource("icons/16/crm/account.png"));
			accountLink.setStyleName("link");
			layoutHelper.addComponent(accountLink, "Account", 0, 0);
		} else {
			layoutHelper.addComponent(new Label(""), "Account", 0, 0);
		}

		log.debug("Display associate contact");
		ContactService contactService = ApplicationContextUtil
				.getSpringBean(ContactService.class);
		final SimpleContact contact = contactService
				.findContactAssoWithConvertedLead(lead.getId(),
						AppContext.getAccountId());
		if (contact != null) {
			Button contactLink = new Button(contact.getContactName(),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							EventBus.getInstance().fireEvent(
									new ContactEvent.GotoRead(this, contact
											.getId()));

						}
					});
			contactLink.setIcon(MyCollabResource
					.newResource("icons/16/crm/contact.png"));
			contactLink.setStyleName("link");
			layoutHelper.addComponent(contactLink, "Contact", 0, 1);
		} else {
			layoutHelper.addComponent(new Label(""), "Contact", 0, 1);
		}

		log.debug("Display associate opportunity");
		OpportunityService opportunityService = ApplicationContextUtil
				.getSpringBean(OpportunityService.class);
		final SimpleOpportunity opportunity = opportunityService
				.findOpportunityAssoWithConvertedLead(lead.getId(),
						AppContext.getAccountId());
		if (opportunity != null) {
			Button opportunityLink = new Button(
					opportunity.getOpportunityname(),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							EventBus.getInstance().fireEvent(
									new OpportunityEvent.GotoRead(this,
											opportunity.getId()));

						}
					});
			opportunityLink.setIcon(MyCollabResource
					.newResource("icons/16/crm/opportunity.png"));
			opportunityLink.setStyleName("link");
			layoutHelper.addComponent(opportunityLink, "Opportunity", 0, 2);
		} else {
			layoutHelper.addComponent(new Label(""), "Opportunity", 0, 2);
		}

		convertLeadInfoLayout.addComponent(layoutHelper.getLayout());
		previewLayout.addBody(createBottomPanel());
		this.addComponent(previewLayout);
	}

	private ComponentContainer createBottomPanel() {
		associateCampaignList = new LeadCampaignListComp();
		associateCampaignList.setEnableControlButtons(false);

		noteListItems = new NoteListItems("Notes");
		noteListItems.setEnableCreateButton(false);

		associateActivityList = new ActivityRelatedItemListComp(false);

		final TabSheet tabContainer = new TabSheet();
		tabContainer.setWidth("100%");

		tabContainer.addTab(noteListItems, "Notes",
				MyCollabResource.newResource("icons/16/crm/note.png"));
		tabContainer.addTab(associateCampaignList, "Campaigns",
				MyCollabResource.newResource("icons/16/crm/campaign.png"));
		tabContainer.addTab(associateActivityList, "Activities",
				MyCollabResource.newResource("icons/16/crm/calendar.png"));

		displayNotes();
		displayActivities();
		displayCampaigns();

		return tabContainer;
	}

	protected void displayNotes() {
		noteListItems.showNotes(CrmTypeConstants.LEAD, lead.getId());
	}

	protected void displayActivities() {
		ActivitySearchCriteria criteria = new ActivitySearchCriteria();
		criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
		criteria.setType(new StringSearchField(SearchField.AND,
				CrmTypeConstants.LEAD));
		criteria.setTypeid(new NumberSearchField(lead.getId()));
		associateActivityList.setSearchCriteria(criteria);
	}

	protected void displayCampaigns() {
		associateCampaignList.displayCampaigns(lead);
	}

	@Override
	public HasPreviewFormHandlers<SimpleLead> getPreviewFormHandlers() {
		return previewForm;
	}

}
