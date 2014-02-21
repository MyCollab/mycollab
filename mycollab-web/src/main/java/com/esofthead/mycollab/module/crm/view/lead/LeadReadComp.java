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

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.form.view.DynaFormLayout;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.ui.components.AbstractPreviewItemComp;
import com.esofthead.mycollab.module.crm.ui.components.CrmPreviewFormControlsGenerator;
import com.esofthead.mycollab.module.crm.ui.components.NoteListItems;
import com.esofthead.mycollab.module.crm.view.activity.ActivityRelatedItemListComp;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
class LeadReadComp extends AbstractPreviewItemComp<SimpleLead> {
	private static final long serialVersionUID = 1L;

	protected LeadCampaignListComp associateCampaignList;
	protected ActivityRelatedItemListComp associateActivityList;
	protected NoteListItems noteListItems;

	public LeadReadComp() {
		super(MyCollabResource.newResource("icons/22/crm/lead.png"));
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleLead> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleLead>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void showHistory() {
				LeadHistoryLogWindow historyLog = new LeadHistoryLogWindow(
						ModuleNameConstants.CRM, CrmTypeConstants.LEAD);
				historyLog.loadHistory(beanItem.getId());
				UI.getCurrent().addWindow(historyLog);
			}
		};
	}

	@Override
	protected ComponentContainer createButtonControls() {
		CrmPreviewFormControlsGenerator<SimpleLead> controlsButton = new CrmPreviewFormControlsGenerator<SimpleLead>(
				previewForm);

		Button convertButton = new Button("Convert",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						previewForm.fireExtraAction("convert", beanItem);
					}
				});
		convertButton.setStyleName(UIConstants.THEME_BLUE_LINK);
		convertButton.setIcon(MyCollabResource
				.newResource("icons/16/convert.png"));
		controlsButton.insertToControlBlock(convertButton);

		return controlsButton
				.createButtonControls(RolePermissionCollections.CRM_LEAD);
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		final TabSheet tabContainer = new TabSheet();
		tabContainer.setWidth("100%");

		tabContainer.addTab(noteListItems, "Notes",
				MyCollabResource.newResource("icons/16/crm/note.png"));
		tabContainer.addTab(associateCampaignList, "Campaigns",
				MyCollabResource.newResource("icons/16/crm/campaign.png"));
		tabContainer.addTab(associateActivityList, "Activities",
				MyCollabResource.newResource("icons/16/crm/calendar.png"));

		return tabContainer;
	}

	@Override
	protected void onPreviewItem() {
		displayNotes();
		displayActivities();
		displayCampaigns();
	}

	@Override
	protected String initFormTitle() {
		return beanItem.getLeadName();
	}

	@Override
	protected void initRelatedComponents() {
		associateCampaignList = new LeadCampaignListComp();
		noteListItems = new NoteListItems("Notes");
		associateActivityList = new ActivityRelatedItemListComp(true);
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new DynaFormLayout(CrmTypeConstants.LEAD,
				LeadDefaultDynaFormLayoutFactory.getForm());
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleLead> initBeanFormFieldFactory() {
		return new LeadReadFormFieldFactory(previewForm);
	}

	protected void displayCampaigns() {
		associateCampaignList.displayCampaigns(beanItem);
	}

	protected void displayNotes() {
		noteListItems.showNotes(CrmTypeConstants.LEAD, beanItem.getId());
	}

	protected void displayActivities() {
		ActivitySearchCriteria criteria = new ActivitySearchCriteria();
		criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
		criteria.setType(new StringSearchField(SearchField.AND,
				CrmTypeConstants.LEAD));
		criteria.setTypeid(new NumberSearchField(beanItem.getId()));
		associateActivityList.setSearchCriteria(criteria);
	}

	public ActivityRelatedItemListComp getAssociateActivityList() {
		return associateActivityList;
	}

	public LeadCampaignListComp getAssociateCampaignList() {
		return associateCampaignList;
	}

	public AdvancedPreviewBeanForm<SimpleLead> getPreviewForm() {
		return previewForm;
	}

}
