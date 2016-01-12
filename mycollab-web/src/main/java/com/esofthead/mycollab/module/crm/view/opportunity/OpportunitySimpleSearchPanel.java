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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.user.ui.components.ActiveUserComboBox;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.web.ui.GenericSearchPanel;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.ValueComboBox;
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
public class OpportunitySimpleSearchPanel extends GenericSearchPanel<OpportunitySearchCriteria> {
    private TextField textValueField;
    private ActiveUserComboBox userBox;
    private GridLayout layoutSearchPanel;
    private ValueComboBox group;

    public OpportunitySimpleSearchPanel() {
        createBasicSearchLayout();
    }

    private void createBasicSearchLayout() {
        layoutSearchPanel = new GridLayout(3, 3);
        layoutSearchPanel.setSpacing(true);

        group = new ValueComboBox(false, "Name", "Account Name", "Sales Stage",
                AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE));
        group.select("Name");
        group.setImmediate(true);
        group.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                removeComponents();
                String searchType = (String) group.getValue();
                if (searchType.equals("Name")) {
                    addTextFieldSearch();
                } else if (searchType.equals("Account Name")) {
                    addTextFieldSearch();
                } else if (searchType.equals("Sales Stage")) {
                    addTextFieldSearch();
                } else if (searchType.equals(AppContext
                        .getMessage(GenericI18Enum.FORM_ASSIGNEE))) {
                    addUserListSelectField();
                }
            }
        });

        layoutSearchPanel.addComponent(group, 1, 0);
        layoutSearchPanel.setComponentAlignment(group, Alignment.MIDDLE_CENTER);
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
        layoutSearchPanel.addComponent(searchBtn, 2, 0);
        layoutSearchPanel.setComponentAlignment(searchBtn,
                Alignment.MIDDLE_CENTER);
        this.setCompositionRoot(layoutSearchPanel);
    }

    private void doSearch() {
        OpportunitySearchCriteria searchCriteria = new OpportunitySearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));

        String searchType = (String) group.getValue();
        if (StringUtils.isNotBlank(searchType)) {
            if (textValueField != null) {
                String strSearch = textValueField.getValue();
                if (StringUtils.isNotBlank(strSearch)) {
                    if (searchType.equals("Name")) {
                        searchCriteria.setOpportunityName(new StringSearchField(strSearch));
                    }
                }
            }

            if (userBox != null) {
                String user = (String) userBox.getValue();
                if (StringUtils.isNotBlank(user)) {
                    searchCriteria.setAssignUsers(new SetSearchField<>(user));
                }
            }
        }

        notifySearchHandler(searchCriteria);
    }

    private void addTextFieldSearch() {
        textValueField = new TextField();
        textValueField.addShortcutListener(new ShortcutListener("OpportunitySearchField",
                ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object o, Object o1) {
                doSearch();
            }
        });
        layoutSearchPanel.addComponent(textValueField, 0, 0);
        layoutSearchPanel.setComponentAlignment(textValueField, Alignment.MIDDLE_CENTER);
    }

    private void addUserListSelectField() {
        userBox = new ActiveUserComboBox();
        userBox.setImmediate(true);
        layoutSearchPanel.addComponent(userBox, 0, 0);
        layoutSearchPanel.setComponentAlignment(userBox, Alignment.MIDDLE_CENTER);
    }

    private void removeComponents() {
        layoutSearchPanel.removeComponent(0, 0);
        userBox = null;
        textValueField = null;
    }

    @Override
    public void setTotalCountNumber(int totalCountNumber) {

    }
}