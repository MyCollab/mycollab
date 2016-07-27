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
package com.mycollab.module.crm.view.account;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.db.query.Param;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.events.AccountEvent;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.module.crm.ui.components.ComponentUtils;
import com.mycollab.module.user.ui.components.ActiveUserListSelect;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.mycollab.vaadin.web.ui.DefaultGenericSearchPanel;
import com.mycollab.vaadin.web.ui.DynamicQueryParamLayout;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccountSearchPanel extends DefaultGenericSearchPanel<AccountSearchCriteria> {

    private static Param[] paramFields = new Param[]{
            AccountSearchCriteria.p_accountName, AccountSearchCriteria.p_anyPhone, AccountSearchCriteria.p_website,
            AccountSearchCriteria.p_numemployees, AccountSearchCriteria.p_assignee, AccountSearchCriteria.p_industries,
            AccountSearchCriteria.p_types, AccountSearchCriteria.p_assignee, AccountSearchCriteria.p_billingCountry,
            AccountSearchCriteria.p_shippingCountry, AccountSearchCriteria.p_anyCity, AccountSearchCriteria.p_createdtime,
            AccountSearchCriteria.p_lastupdatedtime};

    @Override
    protected HeaderWithFontAwesome buildSearchTitle() {
        return ComponentUtils.header(CrmTypeConstants.ACCOUNT, AppContext.getMessage(AccountI18nEnum.LIST));
    }

    @Override
    protected Component buildExtraControls() {
        MButton newBtn = new MButton(AppContext.getMessage(AccountI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new AccountEvent.GotoAdd(this, null)))
                .withIcon(FontAwesome.PLUS).withStyleName(WebUIConstants.BUTTON_ACTION)
                .withVisible(AppContext.canWrite(RolePermissionCollections.CRM_ACCOUNT));
        return newBtn;
    }

    @Override
    protected BasicSearchLayout<AccountSearchCriteria> createBasicSearchLayout() {
        return new AccountBasicSearchLayout();
    }

    @Override
    protected SearchLayout<AccountSearchCriteria> createAdvancedSearchLayout() {
        return new AccountAdvancedSearchLayout();
    }

    private class AccountAdvancedSearchLayout extends DynamicQueryParamLayout<AccountSearchCriteria> {

        public AccountAdvancedSearchLayout() {
            super(AccountSearchPanel.this, CrmTypeConstants.ACCOUNT);
        }

        @Override
        public ComponentContainer constructHeader() {
            return AccountSearchPanel.this.constructHeader();
        }

        @Override
        public Param[] getParamFields() {
            return paramFields;
        }

        @Override
        protected Class<AccountSearchCriteria> getType() {
            return AccountSearchCriteria.class;
        }

        @Override
        protected Component buildSelectionComp(String fieldId) {
            if ("assignuser".equals(fieldId)) {
                return new ActiveUserListSelect();
            }
            return null;
        }
    }

    private class AccountBasicSearchLayout extends BasicSearchLayout<AccountSearchCriteria> {
        private TextField nameField;
        private CheckBox myItemCheckbox;

        public AccountBasicSearchLayout() {
            super(AccountSearchPanel.this);
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);

            nameField = new MTextField().withInputPrompt(AppContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT))
                    .withWidth(WebUIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.with(nameField).withAlign(nameField, Alignment.MIDDLE_CENTER);

            myItemCheckbox = new CheckBox(AppContext.getMessage(GenericI18Enum.OPT_MY_ITEMS));
            myItemCheckbox.addStyleName(ValoTheme.CHECKBOX_SMALL);

            basicSearchBody.with(myItemCheckbox).withAlign(myItemCheckbox, Alignment.MIDDLE_CENTER);

            MButton searchBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent -> callSearchAction())
                    .withIcon(FontAwesome.SEARCH).withStyleName(WebUIConstants.BUTTON_ACTION)
                    .withClickShortcut(ShortcutAction.KeyCode.ENTER);

            basicSearchBody.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_LEFT);

            MButton cancelBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> nameField.setValue(""))
                    .withStyleName(WebUIConstants.BUTTON_OPTION);
            basicSearchBody.with(cancelBtn).withAlign(cancelBtn, Alignment.MIDDLE_CENTER);

            MButton advancedSearchBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
                    clickEvent -> moveToAdvancedSearchLayout()).withStyleName(WebUIConstants.BUTTON_LINK);
            basicSearchBody.with(advancedSearchBtn).withAlign(advancedSearchBtn, Alignment.MIDDLE_CENTER);
            return basicSearchBody;
        }

        @Override
        public ComponentContainer constructHeader() {
            return AccountSearchPanel.this.constructHeader();
        }

        @Override
        protected AccountSearchCriteria fillUpSearchCriteria() {
            AccountSearchCriteria searchCriteria = new AccountSearchCriteria();
            searchCriteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
            searchCriteria.setAccountname(StringSearchField.and(nameField.getValue().trim()));
            if (myItemCheckbox.getValue()) {
                searchCriteria.setAssignUser(StringSearchField.and(AppContext.getUsername()));
            } else {
                searchCriteria.setAssignUsers(null);
            }

            return searchCriteria;
        }
    }
}
