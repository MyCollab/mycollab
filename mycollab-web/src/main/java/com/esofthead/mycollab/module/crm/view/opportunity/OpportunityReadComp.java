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

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.form.view.DynaFormLayout;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.localization.LeadI18nEnum;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.crm.ui.components.AbstractPreviewItemComp;
import com.esofthead.mycollab.module.crm.ui.components.CrmPreviewFormControlsGenerator;
import com.esofthead.mycollab.module.crm.ui.components.NoteListItems;
import com.esofthead.mycollab.module.crm.view.CrmResources;
import com.esofthead.mycollab.module.crm.view.activity.ActivityRelatedItemListComp;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
class OpportunityReadComp extends AbstractPreviewItemComp<SimpleOpportunity> {
	private static final long serialVersionUID = 1L;

	protected OpportunityContactListComp associateContactList;
	protected OpportunityLeadListComp associateLeadList;
	protected NoteListItems noteListItems;
	protected ActivityRelatedItemListComp associateActivityList;

	OpportunityReadComp() {
		super(MyCollabResource.newResource("icons/22/crm/opportunity.png"));
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleOpportunity> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleOpportunity>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void showHistory() {
				OpportunityHistoryLogWindow historyLog = new OpportunityHistoryLogWindow(
						ModuleNameConstants.CRM, CrmTypeConstants.OPPORTUNITY);
				historyLog.loadHistory(beanItem.getId());
				UI.getCurrent().addWindow(historyLog);
			}
		};
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new CrmPreviewFormControlsGenerator<SimpleOpportunity>(
				previewForm)
				.createButtonControls(RolePermissionCollections.CRM_OPPORTUNITY);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		final TabSheet tabContainer = new TabSheet();
		tabContainer.setWidth("100%");

		tabContainer.addTab(noteListItems, "Notes",
				MyCollabResource.newResource("icons/16/crm/note.png"));
		tabContainer.addTab(associateContactList, "Contacts",
				MyCollabResource.newResource("icons/16/crm/contact.png"));
		tabContainer.addTab(associateLeadList, "Leads",
				MyCollabResource.newResource("icons/16/crm/lead.png"));
		tabContainer.addTab(associateActivityList, "Activities",
				MyCollabResource.newResource("icons/16/crm/calendar.png"));

		return tabContainer;
	}

	@Override
	protected void onPreviewItem() {
		displayNotes();
		displayActivities();
		displayContacts();
		displayLeads();
	}

	@Override
	protected String initFormTitle() {
		// check if there is converted lead associates with this account
		LeadService leadService = ApplicationContextUtil
				.getSpringBean(LeadService.class);
		SimpleLead lead = leadService.findConvertedLeadOfOpportunity(
				beanItem.getId(), AppContext.getAccountId());
		if (lead != null) {
			return "<h2>"
					+ beanItem.getOpportunityname()
					+ LocalizationHelper
							.getMessage(
									LeadI18nEnum.CONVERT_FROM_LEAD_TITLE,
									CrmResources
											.getResourceLink(CrmTypeConstants.LEAD),
									CrmLinkGenerator.generateCrmItemLink(
											CrmTypeConstants.LEAD, lead.getId()),
									lead.getLeadName()) + "</h2>";
		} else {
			return beanItem.getOpportunityname();
		}
	}

	@Override
	protected void initRelatedComponents() {
		associateContactList = new OpportunityContactListComp();
		associateLeadList = new OpportunityLeadListComp();
		associateActivityList = new ActivityRelatedItemListComp(true);
		noteListItems = new NoteListItems("Notes");
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new DynaFormLayout(CrmTypeConstants.OPPORTUNITY,
				OpportunityDefaultDynaFormLayoutFactory.getForm());
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleOpportunity> initBeanFormFieldFactory() {
		return new OpportunityReadFormFieldFactory(previewForm);
	}

	protected void displayNotes() {
		noteListItems.showNotes(CrmTypeConstants.OPPORTUNITY, beanItem.getId());
	}

	public SimpleOpportunity getOpportunity() {
		return beanItem;
	}

	protected void displayActivities() {
		ActivitySearchCriteria criteria = new ActivitySearchCriteria();
		criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
		criteria.setType(new StringSearchField(SearchField.AND,
				CrmTypeConstants.OPPORTUNITY));
		criteria.setTypeid(new NumberSearchField(beanItem.getId()));
		associateActivityList.setSearchCriteria(criteria);
	}

	protected void displayContacts() {
		associateContactList.displayContacts(beanItem);
	}

	protected void displayLeads() {
		associateLeadList.displayLeads(beanItem);
	}

	public AdvancedPreviewBeanForm<SimpleOpportunity> getPreviewForm() {
		return previewForm;
	}

	public ActivityRelatedItemListComp getAssociateActivityList() {
		return associateActivityList;
	}

	public OpportunityContactListComp getAssociateContactList() {
		return associateContactList;
	}

	public OpportunityLeadListComp getAssociateLeadList() {
		return associateLeadList;
	}
}
