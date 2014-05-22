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

import java.util.Arrays;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.module.crm.domain.Lead;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
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
public class LeadSelectionWindow extends Window {

	private static final long serialVersionUID = 1L;
	private LeadSearchCriteria searchCriteria;
	private LeadTableDisplay tableItem;
	private FieldSelection<Lead> fieldSelection;

	public LeadSelectionWindow(FieldSelection<Lead> fieldSelection) {
		super("Lead Name Lookup");
		this.setWidth("800px");
		this.fieldSelection = fieldSelection;
		this.setModal(true);
		this.setResizable(false);
	}

	public void show() {
		searchCriteria = new LeadSearchCriteria();
		searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));

		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);

		createLeadList();

		LeadSimpleSearchPanel leadSimpleSearchPanel = new LeadSimpleSearchPanel();
		leadSimpleSearchPanel
				.addSearchHandler(new SearchHandler<LeadSearchCriteria>() {

					@Override
					public void onSearch(LeadSearchCriteria criteria) {
						tableItem.setSearchCriteria(criteria);
					}

				});
		layout.addComponent(leadSimpleSearchPanel);
		layout.addComponent(tableItem);
		this.setContent(layout);

		tableItem.setSearchCriteria(searchCriteria);
		center();
	}

	@SuppressWarnings("serial")
	private void createLeadList() {
		tableItem = new LeadTableDisplay(Arrays.asList(LeadTableFieldDef.name,
				LeadTableFieldDef.status, LeadTableFieldDef.assignedUser,
				LeadTableFieldDef.accountName));

		tableItem.setWidth("100%");

		tableItem.addGeneratedColumn("leadName", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final SimpleLead lead = tableItem.getBeanByIndex(itemId);

				ButtonLink b = new ButtonLink(lead.getLeadName(),
						new Button.ClickListener() {

							@Override
							public void buttonClick(
									final Button.ClickEvent event) {
								// TODO Auto-generated method stub
								fieldSelection.fireValueChange(lead);
								LeadSelectionWindow.this.close();
							}
						});
				return b;
			}
		});
	}
}
