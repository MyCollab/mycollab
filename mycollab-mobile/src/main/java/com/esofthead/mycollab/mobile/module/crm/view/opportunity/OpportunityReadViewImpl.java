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

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.form.view.DynaFormLayout;
import com.esofthead.mycollab.mobile.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.AbstractPreviewItemComp;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmPreviewFormControlsGenerator;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmRelatedItemsScreenData;
import com.esofthead.mycollab.mobile.module.crm.ui.NotesList;
import com.esofthead.mycollab.mobile.module.crm.view.activity.ActivityRelatedItemView;
import com.esofthead.mycollab.mobile.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */

@ViewComponent
public class OpportunityReadViewImpl extends
		AbstractPreviewItemComp<SimpleOpportunity> implements
		OpportunityReadView {
	private static final long serialVersionUID = 1588189344759887006L;
	private NotesList associateNotes;
	private ActivityRelatedItemView associateActivities;
	private OpportunityRelatedContactView associateContacts;
	private OpportunityRelatedLeadView associateLeads;

	@Override
	public HasPreviewFormHandlers<SimpleOpportunity> getPreviewFormHandlers() {
		return this.previewForm;
	}

	@Override
	protected void onPreviewItem() {
		// Do nothing
	}

	@Override
	protected String initFormTitle() {
		return this.beanItem.getOpportunityname();
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleOpportunity> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleOpportunity>();
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

	@Override
	protected ComponentContainer createButtonControls() {
		return new CrmPreviewFormControlsGenerator<SimpleOpportunity>(
				previewForm)
				.createButtonControls(RolePermissionCollections.CRM_OPPORTUNITY);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		HorizontalLayout toolbarLayout = new HorizontalLayout();
		toolbarLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		toolbarLayout.setSpacing(true);

		Button relatedContacts = new Button();
		relatedContacts.setCaption("<span aria-hidden=\"true\" data-icon=\""
				+ IconConstants.CRM_CONTACT
				+ "\"></span><div class=\"screen-reader-text\">"
				+ AppContext.getMessage(CrmCommonI18nEnum.TAB_CONTACT)
				+ "</div>");
		relatedContacts.setHtmlContentAllowed(true);
		relatedContacts.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 7589415773039335559L;

			@Override
			public void buttonClick(ClickEvent arg0) {
				EventBusFactory.getInstance().post(
						new OpportunityEvent.GoToRelatedItems(this,
								new CrmRelatedItemsScreenData(
										getAssociateContacts())));
			}
		});
		toolbarLayout.addComponent(relatedContacts);

		Button relatedLeads = new Button();
		relatedLeads.setCaption("<span aria-hidden=\"true\" data-icon=\""
				+ IconConstants.CRM_LEAD
				+ "\"></span><div class=\"screen-reader-text\">"
				+ AppContext.getMessage(CrmCommonI18nEnum.TAB_LEAD) + "</div>");
		relatedLeads.setHtmlContentAllowed(true);
		relatedLeads.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 7589415773039335559L;

			@Override
			public void buttonClick(ClickEvent arg0) {
				EventBusFactory.getInstance().post(
						new OpportunityEvent.GoToRelatedItems(this,
								new CrmRelatedItemsScreenData(
										getAssociateLeads())));
			}
		});
		toolbarLayout.addComponent(relatedLeads);

		Button relatedNotes = new Button();
		relatedNotes.setCaption("<span aria-hidden=\"true\" data-icon=\""
				+ IconConstants.CRM_DOCUMENT
				+ "\"></span><div class=\"screen-reader-text\">"
				+ AppContext.getMessage(CrmCommonI18nEnum.TAB_NOTE) + "</div>");
		relatedNotes.setHtmlContentAllowed(true);
		relatedNotes.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 7589415773039335559L;

			@Override
			public void buttonClick(ClickEvent arg0) {
				EventBusFactory.getInstance().post(
						new OpportunityEvent.GoToRelatedItems(this,
								new CrmRelatedItemsScreenData(
										getAssociateNotes())));
			}
		});
		toolbarLayout.addComponent(relatedNotes);

		Button relatedActivities = new Button();
		relatedActivities.setCaption("<span aria-hidden=\"true\" data-icon=\""
				+ IconConstants.CRM_ACTIVITY
				+ "\"></span><div class=\"screen-reader-text\">"
				+ AppContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY)
				+ "</div>");
		relatedActivities.setHtmlContentAllowed(true);
		relatedActivities.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 7589415773039335559L;

			@Override
			public void buttonClick(ClickEvent arg0) {
				EventBusFactory.getInstance().post(
						new OpportunityEvent.GoToRelatedItems(this,
								new CrmRelatedItemsScreenData(
										getAssociateActivities())));
			}
		});
		toolbarLayout.addComponent(relatedActivities);

		return toolbarLayout;
	}

	private ActivityRelatedItemView getAssociateActivities() {
		if (associateActivities == null)
			associateActivities = new ActivityRelatedItemView();
		final ActivitySearchCriteria searchCriteria = new ActivitySearchCriteria();
		searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		searchCriteria.setType(new StringSearchField(SearchField.AND,
				CrmTypeConstants.OPPORTUNITY));
		searchCriteria.setTypeid(new NumberSearchField(SearchField.AND,
				beanItem.getId()));
		associateActivities.setSearchCriteria(searchCriteria);
		return associateActivities;
	}

	private NotesList getAssociateNotes() {
		if (associateNotes == null)
			associateNotes = new NotesList("Related Notes");
		associateNotes
				.showNotes(CrmTypeConstants.OPPORTUNITY, beanItem.getId());
		return associateNotes;
	}

	protected OpportunityRelatedLeadView getAssociateLeads() {
		if (associateLeads == null)
			associateLeads = new OpportunityRelatedLeadView();
		associateLeads.displayLeads(beanItem);
		return associateLeads;
	}

	protected OpportunityRelatedContactView getAssociateContacts() {
		if (associateContacts == null)
			associateContacts = new OpportunityRelatedContactView();
		associateContacts.displayContacts(beanItem);
		return associateContacts;
	}

}
