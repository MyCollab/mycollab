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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;
import org.apache.commons.lang3.StringUtils;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
public class CampaignSimpleSearchPanel extends GenericSearchPanel<CampaignSearchCriteria> {
    private CampaignSearchCriteria searchCriteria;
    private TextField textValueField;
    private GridLayout layoutSearchPane;
    private ValueComboBox group;
    private DateSelectionField dateSearchField;

    public CampaignSimpleSearchPanel() {
        createBasicSearchLayout();
    }

    private void createBasicSearchLayout() {
        layoutSearchPane = new GridLayout(3, 2);
        layoutSearchPane.setSpacing(true);

        group = new ValueComboBox(false, "Campaign Name", "Start Date", "End Date");
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
                AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
        searchBtn.setStyleName(UIConstants.BUTTON_ACTION);
        searchBtn.setIcon(FontAwesome.SEARCH);
        searchBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                doSearch();
            }
        });
        layoutSearchPane.addComponent(searchBtn, 2, 0);
        layoutSearchPane.setComponentAlignment(searchBtn,
                Alignment.MIDDLE_CENTER);
        this.setCompositionRoot(layoutSearchPane);
    }

    private void doSearch() {
        searchCriteria = new CampaignSearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));

        String searchType = (String) group.getValue();
        if (StringUtils.isNotBlank(searchType)) {
            if (textValueField != null) {
                String strSearch = textValueField.getValue();
                if (StringUtils.isNotBlank(strSearch)) {
                    if (searchType.equals("Campaign Name")) {
                        searchCriteria
                                .setCampaignName(new StringSearchField(
                                        SearchField.AND, strSearch));
                    }
                }
            }

        }
        notifySearchHandler(searchCriteria);
    }

    private void addTextFieldSearch() {
        textValueField = ShortcutExtension.installShortcutAction(new TextField(),
                new ShortcutListener("CampaignSearchField", ShortcutAction.KeyCode.ENTER,
                        null) {
                    @Override
                    public void handleAction(Object o, Object o1) {
                        doSearch();
                    }
                });
        layoutSearchPane.addComponent(textValueField, 0, 0);
        layoutSearchPane.setComponentAlignment(textValueField,
                Alignment.MIDDLE_CENTER);
    }

    private void addDateFieldSearch() {
        dateSearchField = new DateSelectionField();
        dateSearchField.setDateFormat(AppContext.getUserDateFormat().getDateFormat());
        layoutSearchPane.addComponent(dateSearchField, 0, 0);
        layoutSearchPane.setComponentAlignment(dateSearchField,
                Alignment.MIDDLE_CENTER);
    }

    private void removeComponents() {
        layoutSearchPane.removeComponent(0, 0);
        textValueField = null;
        dateSearchField = null;
    }

    @Override
    public void setTotalCountNumber(int totalCountNumber) {

    }
}
