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
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.esofthead.mycollab.module.crm.ui.components.CrmViewHeader;
import com.esofthead.mycollab.module.crm.view.account.AccountSelectionField;
import com.esofthead.mycollab.module.crm.view.campaign.CampaignSelectionField;
import com.esofthead.mycollab.module.user.ui.components.ActiveUserListSelect;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
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
        return new CrmViewHeader(CrmTypeConstants.OPPORTUNITY, AppContext.getMessage(OpportunityI18nEnum.VIEW_LIST_TITLE));
    }

    @Override
    protected void buildExtraControls() {
        Button createAccountBtn = new Button(AppContext.getMessage(OpportunityI18nEnum.BUTTON_NEW_OPPORTUNITY),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        EventBusFactory.getInstance().post(new OpportunityEvent.GotoAdd(
                                OpportunitySearchPanel.this, null));
                    }
                });
        createAccountBtn.setIcon(FontAwesome.PLUS);
        createAccountBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        createAccountBtn.setEnabled(AppContext.canWrite(RolePermissionCollections.CRM_OPPORTUNITY));
        addHeaderRight(createAccountBtn);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BasicSearchLayout<OpportunitySearchCriteria> createBasicSearchLayout() {
        return new OpportunityBasicSearchLayout();
    }

    @Override
    protected SearchLayout<OpportunitySearchCriteria> createAdvancedSearchLayout() {
        return new OpportunityAdvancedSearchLayout();
    }

    @SuppressWarnings("rawtypes")
    private class OpportunityBasicSearchLayout extends BasicSearchLayout {
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

            nameField = ShortcutExtension.installShortcutAction(new TextField(),
                    new ShortcutListener("OpportunitySearchRequest", ShortcutAction.KeyCode.ENTER, null) {
                        @Override
                        public void handleAction(Object o, Object o1) {
                            callSearchAction();
                        }
                    });
            nameField.setInputPrompt("Query by opportunity name");
            nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
            layout.with(nameField).withAlign(nameField, Alignment.MIDDLE_CENTER);

            this.myItemCheckbox = new CheckBox(AppContext.getMessage(GenericI18Enum.SEARCH_MYITEMS_CHECKBOX));
            this.myItemCheckbox.setWidth("75px");
            layout.with(myItemCheckbox).withAlign(myItemCheckbox, Alignment.MIDDLE_CENTER);

            Button searchBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
            searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
            searchBtn.setIcon(FontAwesome.SEARCH);

            searchBtn.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
                    OpportunityBasicSearchLayout.this.callSearchAction();
                }
            });
            layout.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_LEFT);

            Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR));
            cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
            cancelBtn.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
                    OpportunityBasicSearchLayout.this.nameField.setValue("");
                }
            });
            layout.with(cancelBtn).withAlign(cancelBtn, Alignment.MIDDLE_CENTER);

            Button advancedSearchBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            OpportunitySearchPanel.this.moveToAdvancedSearchLayout();
                        }
                    });
            advancedSearchBtn.setStyleName(UIConstants.THEME_LINK);
            layout.with(advancedSearchBtn).withAlign(advancedSearchBtn, Alignment.MIDDLE_CENTER);
            return layout;
        }

        @Override
        protected SearchCriteria fillUpSearchCriteria() {
            OpportunitySearchCriteria searchCriteria = new OpportunitySearchCriteria();
            searchCriteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));

            if (StringUtils.isNotBlank(this.nameField.getValue().trim())) {
                searchCriteria.setOpportunityName(new StringSearchField(this.nameField.getValue().trim()));
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
            if ("opportunity-assignee".equals(fieldId)) {
                return new ActiveUserListSelect();
            } else if ("opportunity-account".equals(fieldId)) {
                return new AccountSelectionField();
            } else if ("opportunity-campaign".equals(fieldId)) {
                return new CampaignSelectionField();
            }
            return null;
        }
    }
}
