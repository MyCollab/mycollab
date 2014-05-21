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
package com.esofthead.mycollab.module.crm.view.campaign;

import java.util.Arrays;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.ui.ButtonLink;
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class CampaignSelectionWindow extends Window {

	private static final long serialVersionUID = 1L;
	private CampaignSearchCriteria searchCriteria;
	private CampaignTableDisplay tableItem;
	private FieldSelection<CampaignWithBLOBs> fieldSelection;

	public CampaignSelectionWindow(
			FieldSelection<CampaignWithBLOBs> fieldSelection) {
		super("Campaign Name Lookup");
		this.setWidth("800px");
		this.fieldSelection = fieldSelection;
		this.setModal(true);
		this.setResizable(false);
	}

	public void show() {
		searchCriteria = new CampaignSearchCriteria();
		searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));

		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);

		createCampaignList();
		CampaignSimpleSearchPanel campaignSimpleSearchPanel = new CampaignSimpleSearchPanel();
		campaignSimpleSearchPanel
				.addSearchHandler(new SearchHandler<CampaignSearchCriteria>() {

					@Override
					public void onSearch(CampaignSearchCriteria criteria) {
						tableItem.setSearchCriteria(criteria);
					}

				});
		layout.addComponent(campaignSimpleSearchPanel);
		layout.addComponent(tableItem);
		this.setContent(layout);

		tableItem.setSearchCriteria(searchCriteria);
		center();
	}

	@SuppressWarnings("serial")
	private void createCampaignList() {
		tableItem = new CampaignTableDisplay(Arrays.asList(
				CampaignTableFieldDef.campaignname, CampaignTableFieldDef.type,
				CampaignTableFieldDef.status, CampaignTableFieldDef.endDate,
				CampaignTableFieldDef.assignUser));

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

						ButtonLink b = new ButtonLink(campaign
								.getCampaignname(), new Button.ClickListener() {

							@Override
							public void buttonClick(
									final Button.ClickEvent event) {
								// TODO Auto-generated method stub
								fieldSelection.fireValueChange(campaign);
								CampaignSelectionWindow.this.close();
							}
						});
						return b;
					}
				});
	}
}
