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
package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.tracker.BugStatusConstants;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.GenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
public class BugSimpleSearchPanel extends GenericSearchPanel<BugSearchCriteria> {

	private BugSearchCriteria searchCriteria;
	private TextField textValueField;
	private GridLayout layoutSearchPane;

	@Override
	public void attach() {
		super.attach();
		this.setHeight("32px");
		createBasicSearchLayout();
	}

	private void createBasicSearchLayout() {
		layoutSearchPane = new GridLayout(5, 3);
		layoutSearchPane.setSpacing(true);

		addTextFieldSearch();

		final CheckBox chkIsOpenBug = new CheckBox("Only Open Bugs");
		layoutSearchPane.addComponent(chkIsOpenBug, 2, 0);
		layoutSearchPane.setComponentAlignment(chkIsOpenBug,
				Alignment.MIDDLE_CENTER);

		Button searchBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH_LABEL));
		searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		searchBtn.setIcon(MyCollabResource.newResource("icons/16/search.png"));
		searchBtn.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				searchCriteria = new BugSearchCriteria();
				searchCriteria.setProjectId(new NumberSearchField(
						SearchField.AND, CurrentProjectVariables.getProject()
								.getId()));
				searchCriteria.setSummary(new StringSearchField(textValueField
						.getValue().toString().trim()));

				if ((Boolean) chkIsOpenBug.getValue()) {
					searchCriteria.setStatuses(new SetSearchField<String>(
							SearchField.AND, new String[] {
									BugStatusConstants.INPROGRESS,
									BugStatusConstants.OPEN,
									BugStatusConstants.REOPENNED }));
				}

				BugSimpleSearchPanel.this.notifySearchHandler(searchCriteria);
			}
		});
		layoutSearchPane.addComponent(searchBtn, 3, 0);
		layoutSearchPane.setComponentAlignment(searchBtn,
				Alignment.MIDDLE_CENTER);

		Button clearBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR_LABEL));
		clearBtn.setStyleName(UIConstants.THEME_BLANK_LINK);
		clearBtn.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				textValueField.setValue("");
			}
		});

		layoutSearchPane.addComponent(clearBtn, 4, 0);
		layoutSearchPane.setComponentAlignment(clearBtn,
				Alignment.MIDDLE_CENTER);

		this.setCompositionRoot(layoutSearchPane);
	}

	private void addTextFieldSearch() {
		textValueField = new TextField();
		textValueField.setWidth("300px");
		layoutSearchPane.addComponent(textValueField, 0, 0);
		layoutSearchPane.setComponentAlignment(textValueField,
				Alignment.MIDDLE_CENTER);
	}
}
