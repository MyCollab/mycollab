package com.mycollab.module.crm.view.lead;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.db.query.Param;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.event.LeadEvent;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
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
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class LeadSearchPanel extends DefaultGenericSearchPanel<LeadSearchCriteria> {
    private static final long serialVersionUID = 1L;

    private boolean canCreateLead;

    private static Param[] paramFields = new Param[]{
            LeadSearchCriteria.p_leadContactName,
            LeadSearchCriteria.p_accountName, LeadSearchCriteria.p_website,
            LeadSearchCriteria.p_anyEmail, LeadSearchCriteria.p_anyPhone,
            LeadSearchCriteria.p_anyCity,
            LeadSearchCriteria.p_billingCountry,
            LeadSearchCriteria.p_shippingCountry,
            LeadSearchCriteria.p_statuses, LeadSearchCriteria.p_sources,
            LeadSearchCriteria.p_assignee};

    public LeadSearchPanel(boolean canCreateLead) {
        this.canCreateLead = canCreateLead;
    }

    @Override
    protected HeaderWithFontAwesome buildSearchTitle() {
        return ComponentUtils.header(CrmTypeConstants.LEAD, UserUIContext.getMessage(LeadI18nEnum.LIST));
    }

    @Override
    protected Component buildExtraControls() {
        return (canCreateLead) ? new MButton(UserUIContext.getMessage(LeadI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new LeadEvent.GotoAdd(this, null)))
                .withIcon(FontAwesome.PLUS).withStyleName(WebThemes.BUTTON_ACTION)
                .withVisible(UserUIContext.canWrite(RolePermissionCollections.CRM_LEAD)) : null;
    }

    @Override
    protected BasicSearchLayout<LeadSearchCriteria> createBasicSearchLayout() {
        return new LeadBasicSearchLayout();
    }

    @Override
    protected SearchLayout<LeadSearchCriteria> createAdvancedSearchLayout() {
        return new LeadAdvancedSearchLayout();
    }

    private class LeadBasicSearchLayout extends BasicSearchLayout<LeadSearchCriteria> {
        private TextField nameField;
        private CheckBox myItemCheckbox;

        LeadBasicSearchLayout() {
            super(LeadSearchPanel.this);
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout layout = new MHorizontalLayout().withMargin(true);

            nameField = new MTextField().withInputPrompt(UserUIContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT))
                    .withWidth(WebUIConstants.DEFAULT_CONTROL_WIDTH);
            layout.with(nameField).withAlign(nameField, Alignment.MIDDLE_CENTER);

            myItemCheckbox = new CheckBox(UserUIContext.getMessage(GenericI18Enum.OPT_MY_ITEMS));
            layout.with(myItemCheckbox).withAlign(myItemCheckbox, Alignment.MIDDLE_CENTER);

            MButton searchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent -> callSearchAction())
                    .withIcon(FontAwesome.SEARCH).withStyleName(WebThemes.BUTTON_ACTION)
                    .withClickShortcut(ShortcutAction.KeyCode.ENTER);
            layout.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_LEFT);

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> nameField.setValue(""))
                    .withStyleName(WebThemes.BUTTON_OPTION);

            layout.with(cancelBtn).withAlign(cancelBtn, Alignment.MIDDLE_CENTER);

            MButton advancedSearchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
                    clickEvent -> moveToAdvancedSearchLayout()).withStyleName(WebThemes.BUTTON_LINK);

            layout.with(advancedSearchBtn).withAlign(advancedSearchBtn, Alignment.MIDDLE_CENTER);
            return layout;
        }

        @Override
        protected LeadSearchCriteria fillUpSearchCriteria() {
            LeadSearchCriteria searchCriteria = new LeadSearchCriteria();
            searchCriteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));

            if (StringUtils.isNotBlank(nameField.getValue().trim())) {
                searchCriteria.setLeadName(StringSearchField.and(nameField.getValue()));
            }

            if (myItemCheckbox.getValue()) {
                searchCriteria.setAssignUsers(new SetSearchField<>(UserUIContext.getUsername()));
            } else {
                searchCriteria.setAssignUsers(null);
            }
            return searchCriteria;
        }
    }

    private class LeadAdvancedSearchLayout extends DynamicQueryParamLayout<LeadSearchCriteria> {

        LeadAdvancedSearchLayout() {
            super(LeadSearchPanel.this, CrmTypeConstants.LEAD);
        }

        @Override
        public Param[] getParamFields() {
            return paramFields;
        }

        @Override
        protected Class<LeadSearchCriteria> getType() {
            return LeadSearchCriteria.class;
        }

        @Override
        protected Component buildSelectionComp(String fieldId) {
            if ("assignuser".equals(fieldId)) {
                return new ActiveUserListSelect();
            }
            return null;
        }
    }
}
