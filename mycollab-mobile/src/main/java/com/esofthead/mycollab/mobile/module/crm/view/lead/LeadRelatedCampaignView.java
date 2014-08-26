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
package com.esofthead.mycollab.mobile.module.crm.view.lead;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.CrmEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedListView;
import com.esofthead.mycollab.mobile.module.crm.view.campaign.CampaignListDisplay;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class LeadRelatedCampaignView extends
		AbstractRelatedListView<SimpleCampaign, CampaignSearchCriteria> {
	private static final long serialVersionUID = 3836477709565175561L;
	private SimpleLead lead;

	public LeadRelatedCampaignView() {
		super();

		setCaption(AppContext
				.getMessage(CampaignI18nEnum.M_TITLE_RELATED_CAMPAIGNS));
		this.itemList = new CampaignListDisplay();
		this.setContent(this.itemList);
	}

	private void loadCampaigns() {
		final CampaignSearchCriteria searchCriteria = new CampaignSearchCriteria();
		searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		searchCriteria.setLeadId(new NumberSearchField(SearchField.AND,
				this.lead.getId()));
		this.itemList.setSearchCriteria(searchCriteria);
	}

	public void displayCampaign(SimpleLead lead) {
		this.lead = lead;
		loadCampaigns();
	}

	@Override
	public void refresh() {
		loadCampaigns();
	}

	@Override
	protected Component createRightComponent() {
		NavigationBarQuickMenu addCampaign = new NavigationBarQuickMenu();
		addCampaign.setStyleName("add-btn");

		VerticalLayout addBtns = new VerticalLayout();
		addBtns.setStyleName("edit-btn-layout");
		addBtns.setSpacing(true);
		addBtns.setMargin(true);
		addBtns.setWidth("100%");

		NavigationButton newCampaign = new NavigationButton();
		newCampaign.setTargetViewCaption(AppContext
				.getMessage(CampaignI18nEnum.VIEW_NEW_TITLE));
		newCampaign
				.addClickListener(new NavigationButton.NavigationButtonClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(
							NavigationButton.NavigationButtonClickEvent arg0) {
						fireNewRelatedItem("");
					}
				});
		addBtns.addComponent(newCampaign);

		NavigationButton selectCampaign = new NavigationButton();
		selectCampaign.setTargetViewCaption("Select Campaigns");
		selectCampaign
				.addClickListener(new NavigationButton.NavigationButtonClickListener() {

					private static final long serialVersionUID = 6803421194441320713L;

					@Override
					public void buttonClick(
							NavigationButton.NavigationButtonClickEvent event) {
						final LeadCampaignSelectionView campaignSelectionView = new LeadCampaignSelectionView(
								LeadRelatedCampaignView.this);
						CampaignSearchCriteria criteria = new CampaignSearchCriteria();
						criteria.setSaccountid(new NumberSearchField(AppContext
								.getAccountId()));
						campaignSelectionView.setSearchCriteria(criteria);
						EventBusFactory.getInstance().post(
								new CrmEvent.PushView(
										LeadRelatedCampaignView.this,
										campaignSelectionView));
					}
				});
		addBtns.addComponent(selectCampaign);

		addCampaign.setContent(addBtns);

		return addCampaign;
	}

}
