package com.mycollab.module.crm.view.contact;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.db.query.Param;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.event.ContactEvent;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.module.crm.ui.components.ComponentUtils;
import com.mycollab.module.crm.view.account.AccountSelectionField;
import com.mycollab.module.user.ui.components.ActiveUserListSelect;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.mycollab.vaadin.web.ui.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ContactSearchPanel extends DefaultGenericSearchPanel<ContactSearchCriteria> {

    private static Param[] paramFields = new Param[]{
            ContactSearchCriteria.p_name, ContactSearchCriteria.p_account,
            ContactSearchCriteria.p_leadsource,
            ContactSearchCriteria.p_billingCountry,
            ContactSearchCriteria.p_shippingCountry,
            ContactSearchCriteria.p_anyPhone, ContactSearchCriteria.p_anyEmail,
            ContactSearchCriteria.p_anyCity, ContactSearchCriteria.p_assignee,
            ContactSearchCriteria.p_createdtime,
            ContactSearchCriteria.p_lastupdatedtime};

    @Override
    protected HeaderWithFontAwesome buildSearchTitle() {
        return ComponentUtils.header(CrmTypeConstants.CONTACT, UserUIContext.getMessage(ContactI18nEnum.LIST));
    }

    @Override
    protected Component buildExtraControls() {
        return new MButton(UserUIContext.getMessage(ContactI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new ContactEvent.GotoAdd(this, null)))
                .withIcon(FontAwesome.PLUS).withStyleName(WebThemes.BUTTON_ACTION)
                .withVisible(UserUIContext.canWrite(RolePermissionCollections.CRM_CONTACT));
    }

    @Override
    protected BasicSearchLayout<ContactSearchCriteria> createBasicSearchLayout() {
        return new ContactBasicSearchLayout();
    }

    @Override
    protected SearchLayout<ContactSearchCriteria> createAdvancedSearchLayout() {
        return new ContactAdvancedSearchLayout();
    }

    private class ContactBasicSearchLayout extends BasicSearchLayout<ContactSearchCriteria> {
        private static final long serialVersionUID = 1L;
        private TextField nameField;
        private CheckBox myItemCheckbox;

        private ContactBasicSearchLayout() {
            super(ContactSearchPanel.this);
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);
            nameField = new MTextField().withInputPrompt(UserUIContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT))
                    .withWidth(WebUIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.with(nameField).withAlign(nameField, Alignment.MIDDLE_CENTER);

            this.myItemCheckbox = new CheckBox(UserUIContext.getMessage(GenericI18Enum.OPT_MY_ITEMS));
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
        protected ContactSearchCriteria fillUpSearchCriteria() {
            ContactSearchCriteria searchCriteria = new ContactSearchCriteria();
            searchCriteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
            if (StringUtils.isNotBlank(nameField.getValue())) {
                searchCriteria.setContactName(StringSearchField.and(nameField.getValue().trim()));
            }

            if (this.myItemCheckbox.getValue()) {
                searchCriteria.setAssignUsers(new SetSearchField<>(UserUIContext.getUsername()));
            } else {
                searchCriteria.setAssignUsers(null);
            }
            return searchCriteria;
        }
    }

    private class ContactAdvancedSearchLayout extends DynamicQueryParamLayout<ContactSearchCriteria> {

        ContactAdvancedSearchLayout() {
            super(ContactSearchPanel.this, CrmTypeConstants.CONTACT);
        }

        @Override
        public Param[] getParamFields() {
            return paramFields;
        }

        @Override
        protected Class<ContactSearchCriteria> getType() {
            return ContactSearchCriteria.class;
        }

        @Override
        protected Component buildSelectionComp(String fieldId) {
            if ("assignuser".equals(fieldId)) {
                return new ActiveUserListSelect();
            } else if ("account".equals(fieldId)) {
                return new AccountSelectionField();
            }
            return null;
        }
    }
}
