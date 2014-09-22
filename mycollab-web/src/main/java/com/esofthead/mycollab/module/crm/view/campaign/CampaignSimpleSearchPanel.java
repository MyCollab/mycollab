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

import org.apache.commons.lang3.StringUtils;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.DateSelectionField;
import com.esofthead.mycollab.vaadin.ui.GenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.ValueComboBox;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
public class CampaignSimpleSearchPanel extends
		GenericSearchPanel<CampaignSearchCriteria> {

	private CampaignSearchCriteria searchCriteria;
	private TextField textValueField;
	private GridLayout layoutSearchPane;
	private DateSelectionField dateSearchField;

	@Override
	public void attach() {
		super.attach();
		createBasicSearchLayout();
	}

	private void createBasicSearchLayout() {
		layoutSearchPane = new GridLayout(3, 2);
		layoutSearchPane.setSpacing(true);

		final ValueComboBox group = new ValueComboBox(false, new String[] {
				"Campaign Name", "Start Date", "End Date" });
		group.select("Campaign Name");
		group.setImmediate(true);
		group.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				removeComponents();
				String searchType = (String) group.getValue();
				if (searchType.equals("Campaign Name")) {
					addTextFieldSearch();
				} else if (searchType.equals("Start Date")) {
					addDateFieldSearch();
				} else if (searchType.equals("End Date")) {
					addDateFieldSearch();
				}
			}
		});

		layoutSearchPane.addComponent(group, 1, 0);
		layoutSearchPane.setComponentAlignment(group, Alignment.MIDDLE_CENTER);
		addTextFieldSearch();

		Button searchBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH_LABEL));
		searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		searchBtn.setIcon(MyCollabResource.newResource("icons/16/search.png"));
		searchBtn.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				searchCriteria = new CampaignSearchCriteria();
				searchCriteria.setSaccountid(new NumberSearchField(
						SearchField.AND, AppContext.getAccountId()));

				String searchType = (String) group.getValue();
				if (StringUtils.isNotBlank(searchType)) {

					if (textValueField != null) {
						String strSearch = (String) textValueField.getValue();
						if (StringUtils.isNotBlank(strSearch)) {

							if (searchType.equals("Campaign Name")) {
								searchCriteria
										.setCampaignName(new StringSearchField(
												SearchField.AND, strSearch));
							} else if (searchType.equals("Email")) {
							} else if (searchType.equals("Phone")) {
							}
						}
					}

				}
				CampaignSimpleSearchPanel.this
						.notifySearchHandler(searchCriteria);
			}
		});
		layoutSearchPane.addComponent(searchBtn, 2, 0);
		layoutSearchPane.setComponentAlignment(searchBtn,
				Alignment.MIDDLE_CENTER);
		this.setCompositionRoot(layoutSearchPane);
	}

	private void addTextFieldSearch() {
		textValueField = new TextField();
		layoutSearchPane.addComponent(textValueField, 0, 0);
		layoutSearchPane.setComponentAlignment(textValueField,
				Alignment.MIDDLE_CENTER);
	}

	private void addDateFieldSearch() {
		dateSearchField = new DateSelectionField();
		dateSearchField.setDateFormat(AppContext.getUserDateFormat());
		layoutSearchPane.addComponent(dateSearchField, 0, 0);
		layoutSearchPane.setComponentAlignment(dateSearchField,
				Alignment.MIDDLE_CENTER);
	}

	private void removeComponents() {
		layoutSearchPane.removeComponent(0, 0);
		textValueField = null;
		dateSearchField = null;
	}

}
