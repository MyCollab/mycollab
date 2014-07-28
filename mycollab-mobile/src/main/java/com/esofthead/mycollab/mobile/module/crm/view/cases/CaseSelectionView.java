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
package com.esofthead.mycollab.mobile.module.crm.view.cases;

import java.util.HashSet;
import java.util.Set;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList.RowDisplayHandler;
import com.esofthead.mycollab.mobile.ui.AbstractSelectionView;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class CaseSelectionView extends AbstractSelectionView<SimpleCase> {
	private static final long serialVersionUID = 2092608350938161913L;
	private CaseSearchCriteria searchCriteria;
	private CaseListDisplay itemList;

	private RowDisplayHandler<SimpleCase> rowHandler;

	private boolean isMultiSelect = false;

	private Set<SimpleCase> selections;

	private Button doneBtn;

	public CaseSelectionView() {
		super();
		doneBtn = new Button("Done");
		setMultiSelect(false);
		createUI();
		this.setCaption("Case Name Lookup");
	}

	public void setMultiSelect(boolean value) {
		if (value == this.isMultiSelect)
			return;

		if (value) {
			this.selections = new HashSet<SimpleCase>();
			this.setRightComponent(doneBtn);
			this.rowHandler = new CaseRowMultiSelectDisplayHandler();
		} else {
			this.selections = null;
			this.setRightComponent(null);
			this.rowHandler = new CaseRowDisplayHandler();
		}
		this.isMultiSelect = value;

	}

	public boolean isMultiSelect() {
		return this.isMultiSelect;
	}

	public void createUI() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);

		createCaseList();

		layout.addComponent(itemList);
		this.setContent(layout);
	}

	private void createCaseList() {
		itemList = new CaseListDisplay();
		itemList.setWidth("100%");
		itemList.setRowDisplayHandler(rowHandler);
	}

	@Override
	public void load() {
		searchCriteria = new CaseSearchCriteria();
		searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));

		itemList.setSearchCriteria(searchCriteria);

		SimpleCase clearCase = new SimpleCase();
		itemList.getListContainer().addComponentAsFirst(
				rowHandler.generateRow(clearCase, 0));
	}

	private class CaseRowDisplayHandler implements
			RowDisplayHandler<SimpleCase> {

		@Override
		public Component generateRow(final SimpleCase cases, int rowIndex) {
			Button b = new Button(cases.getSubject(),
					new Button.ClickListener() {

						private static final long serialVersionUID = -8792072785486355790L;

						@Override
						public void buttonClick(final Button.ClickEvent event) {
							selectionField.fireValueChange(cases);
							CaseSelectionView.this.getNavigationManager()
									.navigateBack();
						}
					});
			return b;
		}

	}

	private class CaseRowMultiSelectDisplayHandler implements
			RowDisplayHandler<SimpleCase> {

		@Override
		public Component generateRow(final SimpleCase cases, int rowIndex) {
			Button b = new Button(cases.getSubject(),
					new Button.ClickListener() {

						private static final long serialVersionUID = -8792072785486355790L;

						@Override
						public void buttonClick(final Button.ClickEvent event) {
							// TODO: toggle selection
						}
					});
			return b;
		}

	}
}
