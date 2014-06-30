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
package com.esofthead.mycollab.mobile.module.crm.view.campaign;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.mobile.ui.AbstractSelectionView;
import com.esofthead.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class CampaignSelectionView extends
		AbstractSelectionView<CampaignWithBLOBs> {

	private static final long serialVersionUID = 1L;
	private CampaignSearchCriteria searchCriteria;
	private CampaignListDisplay tableItem;

	public CampaignSelectionView() {
		super();
		createUI();
		this.setCaption("Campaign Name Lookup");
	}

	private void createUI() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);

		createCampaignList();

		layout.addComponent(tableItem);
		this.setContent(layout);
	}

	@SuppressWarnings("serial")
	private void createCampaignList() {
		tableItem = new CampaignListDisplay("campaignname");

		tableItem.setWidth("100%");

		tableItem.addGeneratedColumn("campaignname",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public com.vaadin.ui.Component generateCell(
							final Table source, final Object itemId,
							final Object columnId) {
						final SimpleCampaign campaign = tableItem
								.getBeanByIndex(itemId);

						Button b = new Button(campaign.getCampaignname(),
								new Button.ClickListener() {

									@Override
									public void buttonClick(
											final Button.ClickEvent event) {
										selectionField
												.fireValueChange(campaign);
										CampaignSelectionView.this
												.getNavigationManager()
												.navigateBack();
									}
								});
						return b;
					}
				});
	}

	@Override
	public void load() {
		searchCriteria = new CampaignSearchCriteria();
		searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));

		tableItem.setSearchCriteria(searchCriteria);

		SimpleCampaign clearCampaign = new SimpleCampaign();
		tableItem.getBeanContainer().addItemAt(0, clearCampaign);
	}
}
