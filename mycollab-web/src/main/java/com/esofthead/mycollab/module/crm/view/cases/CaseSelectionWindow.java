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
package com.esofthead.mycollab.module.crm.view.cases;

import java.util.Arrays;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
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
public class CaseSelectionWindow extends Window {

	private static final long serialVersionUID = 1L;
	private CaseSearchCriteria searchCriteria;
	private CaseTableDisplay tableItem;
	private FieldSelection fieldSelection;

	public CaseSelectionWindow(FieldSelection fieldSelection) {
		super("Case Name Lookup");
		this.setWidth("900px");
		this.fieldSelection = fieldSelection;
		this.setModal(true);
	}

	public void show() {
		searchCriteria = new CaseSearchCriteria();
		searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));

		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);

		createCaseList();
		CaseSimpleSearchPanel caseSimpleSearchPanel = new CaseSimpleSearchPanel();
		caseSimpleSearchPanel
				.addSearchHandler(new SearchHandler<CaseSearchCriteria>() {

					@Override
					public void onSearch(CaseSearchCriteria criteria) {
						tableItem.setSearchCriteria(criteria);
					}

				});
		layout.addComponent(caseSimpleSearchPanel);
		layout.addComponent(tableItem);
		this.setContent(layout);

		tableItem.setSearchCriteria(searchCriteria);
		center();
	}

	@SuppressWarnings("serial")
	private void createCaseList() {
		tableItem = new CaseTableDisplay(Arrays.asList(
				CaseTableFieldDef.subject, CaseTableFieldDef.account,
				CaseTableFieldDef.priority, CaseTableFieldDef.status,
				CaseTableFieldDef.assignUser));

		tableItem.addGeneratedColumn("subject", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public com.vaadin.ui.Component generateCell(final Table source,
					final Object itemId, final Object columnId) {
				final SimpleCase cases = tableItem.getBeanByIndex(itemId);

				ButtonLink b = new ButtonLink(cases.getSubject(),
						new Button.ClickListener() {

							@Override
							public void buttonClick(
									final Button.ClickEvent event) {
								fieldSelection.fireValueChange(cases);
								CaseSelectionWindow.this.close();
							}
						});
				return b;
			}
		});
	}
}
