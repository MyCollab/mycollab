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
package com.esofthead.mycollab.module.crm.view.lead;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.*;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.events.LeadEvent;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.module.crm.ui.components.CrmViewHeader;
import com.esofthead.mycollab.module.user.ui.components.ActiveUserListSelect;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.maddon.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
public class LeadSearchPanel extends
        DefaultGenericSearchPanel<LeadSearchCriteria> {
    private static final long serialVersionUID = 1L;

    private static Param[] paramFields = new Param[]{
            LeadSearchCriteria.p_leadContactName,
            LeadSearchCriteria.p_accountName, LeadSearchCriteria.p_website,
            LeadSearchCriteria.p_anyEmail, LeadSearchCriteria.p_anyPhone,
            LeadSearchCriteria.p_billingCountry,
            LeadSearchCriteria.p_shippingCountry,
            LeadSearchCriteria.p_statuses, LeadSearchCriteria.p_sources,
            LeadSearchCriteria.p_assignee};

    protected LeadSearchCriteria searchCriteria;

    public LeadSearchPanel() {
        this.searchCriteria = new LeadSearchCriteria();
    }

    private HorizontalLayout createSearchTopPanel() {
        final MHorizontalLayout layout = new MHorizontalLayout()
                .withWidth("100%").withSpacing(true)
                .withMargin(new MarginInfo(true, false, true, false))
                .withStyleName(UIConstants.HEADER_VIEW);

        final Label searchtitle = new CrmViewHeader(CrmTypeConstants.LEAD,
                AppContext.getMessage(LeadI18nEnum.VIEW_LIST_TITLE));
        searchtitle.setStyleName(UIConstants.HEADER_TEXT);

        layout.with(searchtitle).withAlign(searchtitle, Alignment.MIDDLE_LEFT)
                .expand(searchtitle);

        final Button createAccountBtn = new Button(
                AppContext.getMessage(LeadI18nEnum.BUTTON_NEW_LEAD),
                new Button.ClickListener() {

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        EventBusFactory.getInstance().post(
                                new LeadEvent.GotoAdd(this, null));
                    }
                });
        createAccountBtn.setIcon(FontAwesome.PLUS);
        createAccountBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        createAccountBtn.setEnabled(AppContext
                .canWrite(RolePermissionCollections.CRM_LEAD));

        layout.with(createAccountBtn).withAlign(createAccountBtn,
                Alignment.MIDDLE_RIGHT);

        return layout;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BasicSearchLayout<LeadSearchCriteria> createBasicSearchLayout() {
        return new LeadBasicSearchLayout();
    }

    @Override
    protected SearchLayout<LeadSearchCriteria> createAdvancedSearchLayout() {
        return new LeadAdvancedSearchLayout();
    }

    @SuppressWarnings("rawtypes")
    private class LeadBasicSearchLayout extends BasicSearchLayout {
        private TextField nameField;
        private CheckBox myItemCheckbox;

        @SuppressWarnings("unchecked")
        public LeadBasicSearchLayout() {
            super(LeadSearchPanel.this);
        }

        @Override
        public ComponentContainer constructHeader() {
            return LeadSearchPanel.this.createSearchTopPanel();
        }

        @Override
        public ComponentContainer constructBody() {
            final MHorizontalLayout layout = new MHorizontalLayout()
                    .withSpacing(true).withMargin(true);

            this.nameField = this.createSeachSupportTextField(new TextField(),
                    "nameFieldOfSearch");
            this.nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
            layout.with(nameField)
                    .withAlign(nameField, Alignment.MIDDLE_CENTER);

            this.myItemCheckbox = new CheckBox(
                    AppContext
                            .getMessage(GenericI18Enum.SEARCH_MYITEMS_CHECKBOX));
            this.myItemCheckbox.setWidth("75px");
            layout.with(myItemCheckbox).withAlign(myItemCheckbox,
                    Alignment.MIDDLE_CENTER);

            final Button searchBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
            searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
            searchBtn.setIcon(FontAwesome.SEARCH);

            searchBtn.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
                    LeadBasicSearchLayout.this.callSearchAction();
                }
            });
            layout.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_LEFT);

            final Button cancelBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR));
            cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
            cancelBtn.addStyleName("cancel-button");
            cancelBtn.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
                    LeadBasicSearchLayout.this.nameField.setValue("");
                }
            });
            layout.with(cancelBtn)
                    .withAlign(cancelBtn, Alignment.MIDDLE_CENTER);

            final Button advancedSearchBtn = new Button(
                    AppContext
                            .getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
                    new Button.ClickListener() {

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            LeadSearchPanel.this.moveToAdvancedSearchLayout();
                        }
                    });
            advancedSearchBtn.setStyleName("link");
            layout.with(advancedSearchBtn).withAlign(advancedSearchBtn,
                    Alignment.MIDDLE_CENTER);
            return layout;
        }

        @Override
        protected SearchCriteria fillupSearchCriteria() {
            LeadSearchPanel.this.searchCriteria = new LeadSearchCriteria();
            LeadSearchPanel.this.searchCriteria
                    .setSaccountid(new NumberSearchField(SearchField.AND,
                            AppContext.getAccountId()));

            if (StringUtils.isNotBlank(this.nameField.getValue()
                    .trim())) {
                LeadSearchPanel.this.searchCriteria
                        .setLeadName(new StringSearchField(SearchField.AND,
                                this.nameField.getValue()));
            }

            if (this.myItemCheckbox.getValue()) {
                LeadSearchPanel.this.searchCriteria
                        .setAssignUsers(new SetSearchField<>(
                                SearchField.AND, new String[]{AppContext
                                .getUsername()}));
            } else {
                LeadSearchPanel.this.searchCriteria.setAssignUsers(null);
            }
            return LeadSearchPanel.this.searchCriteria;
        }
    }

    private class LeadAdvancedSearchLayout extends
            DynamicQueryParamLayout<LeadSearchCriteria> {

        public LeadAdvancedSearchLayout() {
            super(LeadSearchPanel.this, CrmTypeConstants.LEAD);
        }

        @Override
        public ComponentContainer constructHeader() {
            return LeadSearchPanel.this.createSearchTopPanel();
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
            if ("lead-assignuser".equals(fieldId)) {
                return new ActiveUserListSelect();
            }
            return null;
        }
    }
}
