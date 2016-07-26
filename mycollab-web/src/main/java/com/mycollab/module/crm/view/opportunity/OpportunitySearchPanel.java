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
package com.mycollab.module.crm.view.opportunity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.db.query.Param;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.events.OpportunityEvent;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.mycollab.module.crm.ui.components.ComponentUtils;
import com.mycollab.module.crm.view.account.AccountSelectionField;
import com.mycollab.module.crm.view.campaign.CampaignSelectionField;
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
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
public class OpportunitySearchPanel extends DefaultGenericSearchPanel<OpportunitySearchCriteria> {

    private static Param[] paramFields = new Param[]{
            OpportunitySearchCriteria.p_opportunityName,
            OpportunitySearchCriteria.p_account,
            OpportunitySearchCriteria.p_nextStep,
            OpportunitySearchCriteria.p_campaign,
            OpportunitySearchCriteria.p_leadSource,
            OpportunitySearchCriteria.p_saleStage,
            OpportunitySearchCriteria.p_type,
            OpportunitySearchCriteria.p_expectedcloseddate,
            OpportunitySearchCriteria.p_createdtime,
            OpportunitySearchCriteria.p_lastupdatedtime};

    @Override
    protected HeaderWithFontAwesome buildSearchTitle() {
        return ComponentUtils.header(CrmTypeConstants.OPPORTUNITY, AppContext.getMessage(OpportunityI18nEnum.LIST));
    }

    @Override
    protected Component buildExtraControls() {
        MButton newBtn = new MButton(AppContext.getMessage(OpportunityI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new OpportunityEvent.GotoAdd(OpportunitySearchPanel.this, null)))
                .withIcon(FontAwesome.PLUS).withStyleName(WebUIConstants.BUTTON_ACTION)
                .withVisible(AppContext.canWrite(RolePermissionCollections.CRM_OPPORTUNITY));
        return newBtn;
    }

    @Override
    protected BasicSearchLayout<OpportunitySearchCriteria> createBasicSearchLayout() {
        return new OpportunityBasicSearchLayout();
    }

    @Override
    protected SearchLayout<OpportunitySearchCriteria> createAdvancedSearchLayout() {
        return new OpportunityAdvancedSearchLayout();
    }

    @SuppressWarnings("rawtypes")
    private class OpportunityBasicSearchLayout extends BasicSearchLayout<OpportunitySearchCriteria> {
        private static final long serialVersionUID = 1L;
        private TextField nameField;
        private CheckBox myItemCheckbox;

        @SuppressWarnings("unchecked")
        public OpportunityBasicSearchLayout() {
            super(OpportunitySearchPanel.this);
        }

        @Override
        public ComponentContainer constructHeader() {
            return OpportunitySearchPanel.this.constructHeader();
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout layout = new MHorizontalLayout().withMargin(true);

            nameField = new MTextField().withInputPrompt(AppContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT))
                    .withWidth(WebUIConstants.DEFAULT_CONTROL_WIDTH);
            layout.with(nameField).withAlign(nameField, Alignment.MIDDLE_CENTER);

            this.myItemCheckbox = new CheckBox(AppContext.getMessage(GenericI18Enum.OPT_MY_ITEMS));
            layout.with(myItemCheckbox).withAlign(myItemCheckbox, Alignment.MIDDLE_CENTER);

            MButton searchBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent -> callSearchAction())
                    .withIcon(FontAwesome.SEARCH).withStyleName(WebUIConstants.BUTTON_ACTION)
                    .withClickShortcut(ShortcutAction.KeyCode.ENTER);
            layout.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_LEFT);

            MButton cancelBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> nameField.setValue(""))
                    .withStyleName(WebUIConstants.BUTTON_OPTION);
            layout.with(cancelBtn).withAlign(cancelBtn, Alignment.MIDDLE_CENTER);

            MButton advancedSearchBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
                    clickEvent -> moveToAdvancedSearchLayout())
                    .withStyleName(WebUIConstants.BUTTON_LINK);

            layout.with(advancedSearchBtn).withAlign(advancedSearchBtn, Alignment.MIDDLE_CENTER);
            return layout;
        }

        @Override
        protected OpportunitySearchCriteria fillUpSearchCriteria() {
            OpportunitySearchCriteria searchCriteria = new OpportunitySearchCriteria();
            searchCriteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));

            if (StringUtils.isNotBlank(this.nameField.getValue().trim())) {
                searchCriteria.setOpportunityName(StringSearchField.and(this.nameField.getValue().trim()));
            }

            if (this.myItemCheckbox.getValue()) {
                searchCriteria.setAssignUsers(new SetSearchField<>(AppContext.getUsername()));
            } else {
                searchCriteria.setAssignUsers(null);
            }

            return searchCriteria;
        }
    }

    private class OpportunityAdvancedSearchLayout extends DynamicQueryParamLayout<OpportunitySearchCriteria> {

        public OpportunityAdvancedSearchLayout() {
            super(OpportunitySearchPanel.this, CrmTypeConstants.OPPORTUNITY);
        }

        @Override
        public ComponentContainer constructHeader() {
            return OpportunitySearchPanel.this.constructHeader();
        }

        @Override
        public Param[] getParamFields() {
            return paramFields;
        }

        @Override
        protected Class<OpportunitySearchCriteria> getType() {
            return OpportunitySearchCriteria.class;
        }

        @Override
        protected Component buildSelectionComp(String fieldId) {
            if ("assignee".equals(fieldId)) {
                return new ActiveUserListSelect();
            } else if ("account".equals(fieldId)) {
                return new AccountSelectionField();
            } else if ("campaign".equals(fieldId)) {
                return new CampaignSelectionField();
            }
            return null;
        }
    }
}
