package com.mycollab.module.crm.view.account;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.db.query.Param;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.event.AccountEvent;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.module.crm.ui.components.ComponentUtils;
import com.mycollab.module.user.ui.components.ActiveUserListSelect;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.mycollab.vaadin.web.ui.*;
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

    private boolean canCreateAccount;

    private static Param[] paramFields = new Param[]{
            AccountSearchCriteria.p_accountName, AccountSearchCriteria.p_anyPhone, AccountSearchCriteria.p_website,
            AccountSearchCriteria.p_numemployees, AccountSearchCriteria.p_assignee, AccountSearchCriteria.p_industries,
            AccountSearchCriteria.p_types, AccountSearchCriteria.p_billingCountry,
            AccountSearchCriteria.p_shippingCountry, AccountSearchCriteria.p_anyCity, AccountSearchCriteria.p_createdtime,
            AccountSearchCriteria.p_lastupdatedtime};

    public AccountSearchPanel(boolean canCreateAccount) {
        this.canCreateAccount = canCreateAccount;
    }

    @Override
    protected HeaderWithFontAwesome buildSearchTitle() {
        return ComponentUtils.header(CrmTypeConstants.ACCOUNT, UserUIContext.getMessage(AccountI18nEnum.LIST));
    }

    @Override
    protected Component buildExtraControls() {
        return (canCreateAccount) ? new MButton(UserUIContext.getMessage(AccountI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new AccountEvent.GotoAdd(this, null)))
                .withIcon(FontAwesome.PLUS).withStyleName(WebThemes.BUTTON_ACTION)
                .withVisible(UserUIContext.canWrite(RolePermissionCollections.CRM_ACCOUNT)) : null;
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

        AccountAdvancedSearchLayout() {
            super(AccountSearchPanel.this, CrmTypeConstants.ACCOUNT);
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

        AccountBasicSearchLayout() {
            super(AccountSearchPanel.this);
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);

            nameField = new MTextField().withInputPrompt(UserUIContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT))
                    .withWidth(WebUIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.with(nameField).withAlign(nameField, Alignment.MIDDLE_CENTER);

            myItemCheckbox = new CheckBox(UserUIContext.getMessage(GenericI18Enum.OPT_MY_ITEMS));
            myItemCheckbox.addStyleName(ValoTheme.CHECKBOX_SMALL);

            basicSearchBody.with(myItemCheckbox).withAlign(myItemCheckbox, Alignment.MIDDLE_CENTER);

            MButton searchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent -> callSearchAction())
                    .withIcon(FontAwesome.SEARCH).withStyleName(WebThemes.BUTTON_ACTION)
                    .withClickShortcut(ShortcutAction.KeyCode.ENTER);

            basicSearchBody.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_LEFT);

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> nameField.setValue(""))
                    .withStyleName(WebThemes.BUTTON_OPTION);
            basicSearchBody.with(cancelBtn).withAlign(cancelBtn, Alignment.MIDDLE_CENTER);

            MButton advancedSearchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
                    clickEvent -> moveToAdvancedSearchLayout()).withStyleName(WebThemes.BUTTON_LINK);
            basicSearchBody.with(advancedSearchBtn).withAlign(advancedSearchBtn, Alignment.MIDDLE_CENTER);
            return basicSearchBody;
        }

        @Override
        protected AccountSearchCriteria fillUpSearchCriteria() {
            AccountSearchCriteria searchCriteria = new AccountSearchCriteria();
            searchCriteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
            searchCriteria.setAccountname(StringSearchField.and(nameField.getValue().trim()));
            if (myItemCheckbox.getValue()) {
                searchCriteria.setAssignUser(StringSearchField.and(UserUIContext.getUsername()));
            } else {
                searchCriteria.setAssignUsers(null);
            }

            return searchCriteria;
        }
    }
}
