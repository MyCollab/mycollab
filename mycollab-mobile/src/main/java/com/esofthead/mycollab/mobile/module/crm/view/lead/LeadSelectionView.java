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
import com.esofthead.mycollab.mobile.ui.AbstractSelectionView;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
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
public class LeadSelectionView extends AbstractSelectionView<SimpleLead> {
	private static final long serialVersionUID = 8715554837844950390L;
	private LeadSearchCriteria searchCriteria;
	private LeadListDisplay tableItem;

	public LeadSelectionView() {
		super();
		createUI();
		this.setCaption("Lead Name Lookup");
	}

	public void createUI() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);

		createLeadList();

		layout.addComponent(tableItem);
		this.setContent(layout);
	}

	@SuppressWarnings("serial")
	private void createLeadList() {
		tableItem = new LeadListDisplay("leadName");

		tableItem.setWidth("100%");

		tableItem.addGeneratedColumn("leadName", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final SimpleLead lead = tableItem.getBeanByIndex(itemId);

				Button b = new Button(lead.getLeadName(),
						new Button.ClickListener() {

							@Override
							public void buttonClick(
									final Button.ClickEvent event) {
								selectionField.fireValueChange(lead);
								LeadSelectionView.this.getNavigationManager()
										.navigateBack();
							}
						});
				return b;
			}
		});
	}

	@Override
	public void load() {
		searchCriteria = new LeadSearchCriteria();
		searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));

		tableItem.setSearchCriteria(searchCriteria);

		SimpleLead clearLead = new SimpleLead();
		tableItem.getBeanContainer().addItemAt(0, clearLead);
	}
}
