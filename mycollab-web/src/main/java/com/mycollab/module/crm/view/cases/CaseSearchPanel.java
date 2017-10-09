package com.mycollab.module.crm.view.cases;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.db.query.Param;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.module.crm.event.CaseEvent;
import com.mycollab.module.crm.i18n.CaseI18nEnum;
import com.mycollab.module.crm.ui.components.ComponentUtils;
import com.mycollab.module.crm.view.account.AccountSelectionField;
import com.mycollab.module.user.ui.components.ActiveUserListSelect;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.mycollab.vaadin.web.ui.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CaseSearchPanel extends DefaultGenericSearchPanel<CaseSearchCriteria> {
    private static final long serialVersionUID = 1L;

    private boolean canCreateCase;

    private static Param[] paramFields = new Param[]{
            CaseSearchCriteria.p_account, CaseSearchCriteria.p_priority,
            CaseSearchCriteria.p_status, CaseSearchCriteria.p_email,
            CaseSearchCriteria.p_origin, CaseSearchCriteria.p_reason,
            CaseSearchCriteria.p_subject, CaseSearchCriteria.p_type,
            CaseSearchCriteria.p_createdtime,
            CaseSearchCriteria.p_lastupdatedtime};

    public CaseSearchPanel(boolean canCreateCase) {
        this.canCreateCase = canCreateCase;
    }

    @Override
    protected HeaderWithFontAwesome buildSearchTitle() {
        return ComponentUtils.header(CrmTypeConstants.CASE, UserUIContext.getMessage(CaseI18nEnum.LIST));
    }

    @Override
    protected Component buildExtraControls() {
        return (canCreateCase) ? new MButton(UserUIContext.getMessage(CaseI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new CaseEvent.GotoAdd(this, null)))
                .withIcon(FontAwesome.PLUS).withStyleName(WebThemes.BUTTON_ACTION)
                .withVisible(UserUIContext.canWrite(RolePermissionCollections.CRM_CASE)) : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BasicSearchLayout<CaseSearchCriteria> createBasicSearchLayout() {
        return new CaseBasicSearchLayout();
    }

    @Override
    protected SearchLayout<CaseSearchCriteria> createAdvancedSearchLayout() {
        return new CaseAdvancedSearchLayout();
    }

    private class CaseAdvancedSearchLayout extends DynamicQueryParamLayout<CaseSearchCriteria> {
        private static final long serialVersionUID = 1L;

        CaseAdvancedSearchLayout() {
            super(CaseSearchPanel.this, CrmTypeConstants.CASE);
        }

        @Override
        public Param[] getParamFields() {
            return paramFields;
        }

        @Override
        protected Class<CaseSearchCriteria> getType() {
            return CaseSearchCriteria.class;
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

    private class CaseBasicSearchLayout extends BasicSearchLayout<CaseSearchCriteria> {
        private static final long serialVersionUID = 1L;
        private TextField subjectField;
        private CheckBox myItemCheckbox;

        CaseBasicSearchLayout() {
            super(CaseSearchPanel.this);
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);

            subjectField = new MTextField().withInputPrompt(UserUIContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT))
                    .withWidth(WebUIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.with(subjectField).withAlign(subjectField, Alignment.MIDDLE_CENTER);

            this.myItemCheckbox = new CheckBox(UserUIContext.getMessage(GenericI18Enum.OPT_MY_ITEMS));
            basicSearchBody.with(myItemCheckbox).withAlign(myItemCheckbox, Alignment.MIDDLE_CENTER);

            MButton searchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent -> callSearchAction())
                    .withIcon(FontAwesome.SEARCH).withStyleName(WebThemes.BUTTON_ACTION);

            basicSearchBody.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_LEFT);

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> subjectField.setValue(""))
                    .withStyleName(WebThemes.BUTTON_OPTION);
            basicSearchBody.with(cancelBtn).withAlign(cancelBtn, Alignment.MIDDLE_CENTER);

            MButton advancedSearchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
                    clickEvent -> moveToAdvancedSearchLayout()).withStyleName(WebThemes.BUTTON_LINK);
            basicSearchBody.with(advancedSearchBtn).withAlign(advancedSearchBtn, Alignment.MIDDLE_CENTER);
            return basicSearchBody;
        }

        @Override
        protected CaseSearchCriteria fillUpSearchCriteria() {
            CaseSearchCriteria searchCriteria = new CaseSearchCriteria();
            searchCriteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));

            if (StringUtils.isNotBlank(subjectField.getValue())) {
                searchCriteria.setSubject(StringSearchField.and(subjectField.getValue().trim()));
            }

            if (myItemCheckbox.getValue()) {
                searchCriteria.setAssignUsers(new SetSearchField<>(UserUIContext.getUsername()));
            } else {
                searchCriteria.setAssignUsers(null);
            }
            return searchCriteria;
        }
    }
}
