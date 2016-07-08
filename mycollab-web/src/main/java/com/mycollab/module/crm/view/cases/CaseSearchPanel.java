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
package com.mycollab.module.crm.view.cases;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.db.query.Param;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.module.crm.events.CaseEvent;
import com.mycollab.module.crm.i18n.CaseI18nEnum;
import com.mycollab.module.crm.ui.components.ComponentUtils;
import com.mycollab.module.crm.view.account.AccountSelectionField;
import com.mycollab.module.user.ui.components.ActiveUserListSelect;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.mycollab.vaadin.web.ui.DefaultGenericSearchPanel;
import com.mycollab.vaadin.web.ui.DynamicQueryParamLayout;
import com.mycollab.vaadin.web.ui.ShortcutExtension;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CaseSearchPanel extends DefaultGenericSearchPanel<CaseSearchCriteria> {
    private static final long serialVersionUID = 1L;

    private static Param[] paramFields = new Param[]{
            CaseSearchCriteria.p_account, CaseSearchCriteria.p_priority,
            CaseSearchCriteria.p_status, CaseSearchCriteria.p_email,
            CaseSearchCriteria.p_origin, CaseSearchCriteria.p_reason,
            CaseSearchCriteria.p_subject, CaseSearchCriteria.p_type,
            CaseSearchCriteria.p_createdtime,
            CaseSearchCriteria.p_lastupdatedtime};

    @Override
    protected HeaderWithFontAwesome buildSearchTitle() {
        return ComponentUtils.header(CrmTypeConstants.CASE, AppContext.getMessage(CaseI18nEnum.LIST));
    }

    @Override
    protected Component buildExtraControls() {
        MButton newBtn = new MButton(AppContext.getMessage(CaseI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new CaseEvent.GotoAdd(this, null)))
                .withIcon(FontAwesome.PLUS).withStyleName(UIConstants.BUTTON_ACTION)
                .withVisible(AppContext.canWrite(RolePermissionCollections.CRM_CASE));
        return newBtn;
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

        public CaseAdvancedSearchLayout() {
            super(CaseSearchPanel.this, CrmTypeConstants.CASE);
        }

        @Override
        public ComponentContainer constructHeader() {
            return CaseSearchPanel.this.constructHeader();
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

        public CaseBasicSearchLayout() {
            super(CaseSearchPanel.this);
        }

        @Override
        public ComponentContainer constructHeader() {
            return CaseSearchPanel.this.constructHeader();
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);

            subjectField = ShortcutExtension.installShortcutAction(new TextField(),
                    new ShortcutListener("CaseSearchField", ShortcutAction.KeyCode.ENTER, null) {
                        @Override
                        public void handleAction(Object o, Object o1) {
                            callSearchAction();
                        }
                    });
            subjectField.setInputPrompt("Query by case name");
            subjectField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.with(subjectField).withAlign(subjectField, Alignment.MIDDLE_CENTER);

            this.myItemCheckbox = new CheckBox(AppContext.getMessage(GenericI18Enum.OPT_MY_ITEMS));
            basicSearchBody.with(myItemCheckbox).withAlign(myItemCheckbox, Alignment.MIDDLE_CENTER);

            MButton searchBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent -> callSearchAction())
                    .withIcon(FontAwesome.SEARCH).withStyleName(UIConstants.BUTTON_ACTION);

            basicSearchBody.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_LEFT);

            MButton cancelBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> subjectField.setValue(""))
                    .withStyleName(UIConstants.BUTTON_OPTION);
            basicSearchBody.with(cancelBtn).withAlign(cancelBtn, Alignment.MIDDLE_CENTER);

            MButton advancedSearchBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
                    clickEvent -> moveToAdvancedSearchLayout()).withStyleName(UIConstants.BUTTON_LINK);
            basicSearchBody.with(advancedSearchBtn).withAlign(advancedSearchBtn, Alignment.MIDDLE_CENTER);
            return basicSearchBody;
        }

        @Override
        protected CaseSearchCriteria fillUpSearchCriteria() {
            CaseSearchCriteria searchCriteria = new CaseSearchCriteria();
            searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));

            if (StringUtils.isNotBlank(this.subjectField.getValue().trim())) {
                searchCriteria.setSubject(StringSearchField.and(this.subjectField.getValue().trim()));
            }

            if (this.myItemCheckbox.getValue()) {
                searchCriteria.setAssignUsers(new SetSearchField<>(AppContext.getUsername()));
            } else {
                searchCriteria.setAssignUsers(null);
            }
            return searchCriteria;
        }
    }
}
