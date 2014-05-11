/**
 * This file is part of mycollab-mobile.
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
package com.esofthead.mycollab.mobile.module.crm.view.contact;

import com.esofthead.mycollab.mobile.form.view.DynaFormLayout;
import com.esofthead.mycollab.mobile.module.crm.localization.LeadI18nEnum;
import com.esofthead.mycollab.mobile.module.crm.ui.AbstractPreviewItemComp;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmPreviewFormControlsGenerator;
import com.esofthead.mycollab.mobile.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmResources;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
@ViewComponent
public class ContactReadViewImpl extends AbstractPreviewItemComp<SimpleContact>
		implements ContactReadView {

	private static final long serialVersionUID = 1L;

	/*
	 * protected ContactOpportunityListComp associateOpportunityList; protected
	 * ActivityRelatedItemListComp associateActivityList; protected
	 * NoteListItems noteListItems;
	 * 
	 * private DateInfoComp dateInfoComp; private PeopleInfoComp peopleInfoComp;
	 */

	@Override
	protected ComponentContainer createBottomPanel() {
		return null;
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleContact> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleContact>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void showHistory() {
				// TODO add historyWindow
			}
		};
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new CrmPreviewFormControlsGenerator<SimpleContact>(previewForm)
				.createButtonControls(RolePermissionCollections.CRM_CONTACT);
	}

	@Override
	public AdvancedPreviewBeanForm<SimpleContact> getPreviewForm() {
		return this.previewForm;
	}

	/*
	 * protected void displayNotes() {
	 * this.noteListItems.showNotes(CrmTypeConstants.CONTACT,
	 * this.beanItem.getId()); }
	 * 
	 * protected void displayActivities() { final ActivitySearchCriteria
	 * criteria = new ActivitySearchCriteria(); criteria.setSaccountid(new
	 * NumberSearchField(AppContext.getAccountId())); criteria.setType(new
	 * StringSearchField(SearchField.AND, CrmTypeConstants.CONTACT));
	 * criteria.setTypeid(new NumberSearchField(this.beanItem.getId()));
	 * this.associateActivityList.setSearchCriteria(criteria); }
	 * 
	 * protected void displayAssociateOpportunityList() { final
	 * OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
	 * criteria.setSaccountid(new NumberSearchField(SearchField.AND,
	 * AppContext.getAccountId())); criteria.setContactId(new
	 * NumberSearchField(SearchField.AND, this.beanItem.getId()));
	 * this.associateOpportunityList.displayOpportunities(this.beanItem); }
	 */

	@Override
	protected void onPreviewItem() {
	}

	@Override
	public void previewItem(SimpleContact item) {
		this.beanItem = item;
		this.setCaption(initFormTitle());

		previewForm.setFormLayoutFactory(initFormLayoutFactory());
		previewForm.setBeanFormFieldFactory(initBeanFormFieldFactory());
		previewForm.setBean(item);

		onPreviewItem();
	}

	@Override
	protected String initFormTitle() {
		// check if there is converted lead associates with this contact
		LeadService leadService = ApplicationContextUtil
				.getSpringBean(LeadService.class);
		SimpleLead lead = leadService.findConvertedLeadOfContact(
				beanItem.getId(), AppContext.getAccountId());
		if (lead != null) {
			return beanItem.getContactName()
					+ "&nbsp;"
					+ AppContext
							.getMessage(
									LeadI18nEnum.CONVERT_FROM_LEAD_TITLE,
									CrmResources
											.getResourceLink(CrmTypeConstants.LEAD),
									CrmLinkGenerator.generateCrmItemLink(
											CrmTypeConstants.LEAD, lead.getId()),
									lead.getLeadName());
		} else {
			return beanItem.getContactName();
		}
	}

	protected void initRelatedComponents() {
		/*
		 * this.associateOpportunityList = new ContactOpportunityListComp();
		 * this.associateActivityList = new ActivityRelatedItemListComp(true);
		 * this.noteListItems = new NoteListItems("Notes");
		 * 
		 * CssLayout navigatorWrapper =
		 * previewItemContainer.getNavigatorWrapper(); VerticalLayout basicInfo
		 * = new VerticalLayout(); basicInfo.setWidth("100%");
		 * basicInfo.setMargin(true); basicInfo.setSpacing(true);
		 * basicInfo.setStyleName("basic-info");
		 * 
		 * dateInfoComp = new DateInfoComp();
		 * basicInfo.addComponent(dateInfoComp);
		 * 
		 * peopleInfoComp = new PeopleInfoComp();
		 * basicInfo.addComponent(peopleInfoComp);
		 * 
		 * navigatorWrapper.addComponentAsFirst(basicInfo);
		 * 
		 * previewItemContainer.addTab(previewContent, "About");
		 * previewItemContainer.addTab(associateOpportunityList,
		 * "Opportunities"); previewItemContainer.addTab(associateActivityList,
		 * "Activities");
		 */
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

	/*
	 * @Override public IRelatedListHandlers<SimpleActivity>
	 * getRelatedActivityHandlers() { return associateActivityList; }
	 * 
	 * @Override public IRelatedListHandlers<SimpleOpportunity>
	 * getRelatedOpportunityHandlers() { return associateOpportunityList; }
	 */
}
