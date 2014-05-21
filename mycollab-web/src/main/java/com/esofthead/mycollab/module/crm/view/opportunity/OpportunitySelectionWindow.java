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

import java.util.Arrays;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
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
public class OpportunitySelectionWindow extends Window {

	private static final long serialVersionUID = 1L;
	private OpportunitySearchCriteria searchCriteria;
	private OpportunityTableDisplay tableItem;
	private FieldSelection fieldSelection;

	public OpportunitySelectionWindow(FieldSelection fieldSelection) {
		super("Opportunity Name Lookup");
		this.setWidth("900px");
		this.fieldSelection = fieldSelection;
		this.setModal(true);
		this.setResizable(false);
	}

	public void show() {
		searchCriteria = new OpportunitySearchCriteria();
		searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));

		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);

		createOpportunityList();
		OpportunitySimpleSearchPanel opportunitySimpleSearchPanel = new OpportunitySimpleSearchPanel();
		opportunitySimpleSearchPanel
				.addSearchHandler(new SearchHandler<OpportunitySearchCriteria>() {

					@Override
					public void onSearch(OpportunitySearchCriteria criteria) {
						tableItem.setSearchCriteria(criteria);
					}

				});
		layout.addComponent(opportunitySimpleSearchPanel);
		layout.addComponent(tableItem);
		this.setContent(layout);

		tableItem.setSearchCriteria(searchCriteria);
		center();
	}

	private void createOpportunityList() {
		tableItem = new OpportunityTableDisplay(Arrays.asList(
				OpportunityTableFieldDef.opportunityName,
				OpportunityTableFieldDef.saleStage,
				OpportunityTableFieldDef.accountName,
				OpportunityTableFieldDef.assignUser));

		tableItem.setWidth("100%");

		tableItem.addGeneratedColumn("opportunityname",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public com.vaadin.ui.Component generateCell(
							final Table source, final Object itemId,
							final Object columnId) {
						final SimpleOpportunity opportunity = tableItem
								.getBeanByIndex(itemId);

						ButtonLink b = new ButtonLink(opportunity
								.getOpportunityname(),
								new Button.ClickListener() {

									@Override
									public void buttonClick(
											final Button.ClickEvent event) {
										// TODO Auto-generated method stub
										fieldSelection
												.fireValueChange(opportunity);
										OpportunitySelectionWindow.this.close();
									}
								});
						return b;
					}
				});
	}
}
