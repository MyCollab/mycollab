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
package com.esofthead.mycollab.module.crm.view.contact;

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.form.view.DynaFormLayout;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
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
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.IRelatedListHandlers;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
@ViewComponent
public class ContactReadViewImpl extends AbstractPreviewItemComp<SimpleContact>
		implements ContactReadView {

	private static final long serialVersionUID = 1L;

	protected ContactOpportunityListComp associateOpportunityList;
	protected ActivityRelatedItemListComp associateActivityList;
	protected NoteListItems noteListItems;

	public ContactReadViewImpl() {
		super(MyCollabResource.newResource("icons/22/crm/contact.png"));
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		return noteListItems;
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleContact> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleContact>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void showHistory() {
				final ContactHistoryLogWindow historyLog = new ContactHistoryLogWindow(
						ModuleNameConstants.CRM, CrmTypeConstants.CONTACT);
				historyLog.loadHistory(beanItem.getId());
				UI.getCurrent().addWindow(historyLog);
			}
		};
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new CrmPreviewFormControlsGenerator<SimpleContact>(previewForm)
				.createButtonControls(RolePermissionCollections.CRM_CONTACT);
	}

	public AdvancedPreviewBeanForm<SimpleContact> getPreviewForm() {
		return this.previewForm;
	}

	protected void displayNotes() {
		this.noteListItems.showNotes(CrmTypeConstants.CONTACT,
				this.beanItem.getId());
	}

	protected void displayActivities() {
		final ActivitySearchCriteria criteria = new ActivitySearchCriteria();
		criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
		criteria.setType(new StringSearchField(SearchField.AND,
				CrmTypeConstants.CONTACT));
		criteria.setTypeid(new NumberSearchField(this.beanItem.getId()));
		this.associateActivityList.setSearchCriteria(criteria);
	}

	protected void displayAssociateOpportunityList() {
		final OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
		criteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		criteria.setContactId(new NumberSearchField(SearchField.AND,
				this.beanItem.getId()));
		this.associateOpportunityList.displayOpportunities(this.beanItem);
	}

	@Override
	protected void onPreviewItem() {
		this.displayNotes();
		this.displayActivities();
		this.displayAssociateOpportunityList();
	}

	@Override
	protected String initFormTitle() {
		// check if there is converted lead associates with this contact
		LeadService leadService = ApplicationContextUtil
				.getSpringBean(LeadService.class);
		SimpleLead lead = leadService.findConvertedLeadOfContact(
				beanItem.getId(), AppContext.getAccountId());
		if (lead != null) {
			return "<h2>"
					+ beanItem.getContactName()
					+ LocalizationHelper
							.getMessage(
									LeadI18nEnum.CONVERT_FROM_LEAD_TITLE,
									CrmResources
											.getResourceLink(CrmTypeConstants.LEAD),
									CrmLinkGenerator.generateCrmItemLink(
											CrmTypeConstants.LEAD, lead.getId()),
									lead.getLeadName()) + "</h2>";
		} else {
			return beanItem.getContactName();
		}
	}

	@Override
	protected void initRelatedComponents() {
		this.associateOpportunityList = new ContactOpportunityListComp();
		this.associateActivityList = new ActivityRelatedItemListComp(true);
		this.noteListItems = new NoteListItems("Notes");

		previewItemContainer.addTab(previewLayout, "About");
		previewItemContainer.addTab(associateOpportunityList, "Opportunities");
		previewItemContainer.addTab(associateActivityList, "Activities");
		
		previewItemContainer.selectTab("About");
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new DynaFormLayout(CrmTypeConstants.CONTACT,
				ContactDefaultDynaFormLayoutFactory.getForm());
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleContact> initBeanFormFieldFactory() {
		return new ContactReadFormFieldFactory(previewForm);
	}

	@Override
	public SimpleContact getItem() {
		return beanItem;
	}

	@Override
	public HasPreviewFormHandlers<SimpleContact> getPreviewFormHandlers() {
		return previewForm;
	}

	@Override
	public IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers() {
		return associateActivityList;
	}

	@Override
	public IRelatedListHandlers<SimpleOpportunity> getRelatedOpportunityHandlers() {
		return associateOpportunityList;
	}
}
