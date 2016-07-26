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
package com.mycollab.module.crm.view.lead;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.db.query.Param;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.events.LeadEvent;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
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

    private static Param[] paramFields = new Param[]{
            LeadSearchCriteria.p_leadContactName,
            LeadSearchCriteria.p_accountName, LeadSearchCriteria.p_website,
            LeadSearchCriteria.p_anyEmail, LeadSearchCriteria.p_anyPhone,
            LeadSearchCriteria.p_anyCity,
            LeadSearchCriteria.p_billingCountry,
            LeadSearchCriteria.p_shippingCountry,
            LeadSearchCriteria.p_statuses, LeadSearchCriteria.p_sources,
            LeadSearchCriteria.p_assignee};

    @Override
    protected HeaderWithFontAwesome buildSearchTitle() {
        return ComponentUtils.header(CrmTypeConstants.LEAD, AppContext.getMessage(LeadI18nEnum.LIST));
    }

    @Override
    protected Component buildExtraControls() {
        MButton newBtn = new MButton(AppContext.getMessage(LeadI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new LeadEvent.GotoAdd(this, null)))
                .withIcon(FontAwesome.PLUS).withStyleName(WebUIConstants.BUTTON_ACTION)
                .withVisible(AppContext.canWrite(RolePermissionCollections.CRM_LEAD));
        return newBtn;
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

        public LeadBasicSearchLayout() {
            super(LeadSearchPanel.this);
        }

        @Override
        public ComponentContainer constructHeader() {
            return LeadSearchPanel.this.constructHeader();
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout layout = new MHorizontalLayout().withMargin(true);

            nameField = new MTextField().withInputPrompt(AppContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT))
                    .withWidth(WebUIConstants.DEFAULT_CONTROL_WIDTH);
            layout.with(nameField).withAlign(nameField, Alignment.MIDDLE_CENTER);

            myItemCheckbox = new CheckBox(AppContext.getMessage(GenericI18Enum.OPT_MY_ITEMS));
            layout.with(myItemCheckbox).withAlign(myItemCheckbox, Alignment.MIDDLE_CENTER);

            MButton searchBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent -> callSearchAction())
                    .withIcon(FontAwesome.SEARCH).withStyleName(WebUIConstants.BUTTON_ACTION)
                    .withClickShortcut(ShortcutAction.KeyCode.ENTER);
            layout.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_LEFT);

            MButton cancelBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> nameField.setValue(""))
                    .withStyleName(WebUIConstants.BUTTON_OPTION);

            layout.with(cancelBtn).withAlign(cancelBtn, Alignment.MIDDLE_CENTER);

            MButton advancedSearchBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
                    clickEvent -> moveToAdvancedSearchLayout()).withStyleName(WebUIConstants.BUTTON_LINK);

            layout.with(advancedSearchBtn).withAlign(advancedSearchBtn, Alignment.MIDDLE_CENTER);
            return layout;
        }

        @Override
        protected LeadSearchCriteria fillUpSearchCriteria() {
            LeadSearchCriteria searchCriteria = new LeadSearchCriteria();
            searchCriteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));

            if (StringUtils.isNotBlank(nameField.getValue().trim())) {
                searchCriteria.setLeadName(StringSearchField.and(nameField.getValue()));
            }

            if (myItemCheckbox.getValue()) {
                searchCriteria.setAssignUsers(new SetSearchField<>(AppContext.getUsername()));
            } else {
                searchCriteria.setAssignUsers(null);
            }
            return searchCriteria;
        }
    }

    private class LeadAdvancedSearchLayout extends DynamicQueryParamLayout<LeadSearchCriteria> {

        public LeadAdvancedSearchLayout() {
            super(LeadSearchPanel.this, CrmTypeConstants.LEAD);
        }

        @Override
        public ComponentContainer constructHeader() {
            return LeadSearchPanel.this.constructHeader();
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
