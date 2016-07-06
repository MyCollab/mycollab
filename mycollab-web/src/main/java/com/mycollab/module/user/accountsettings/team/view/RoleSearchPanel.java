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

package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.domain.criteria.RoleSearchCriteria;
import com.mycollab.module.user.events.RoleEvent;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.mycollab.vaadin.web.ui.DefaultGenericSearchPanel;
import com.mycollab.vaadin.web.ui.GenericSearchPanel;
import com.mycollab.vaadin.web.ui.ShortcutExtension;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class RoleSearchPanel extends DefaultGenericSearchPanel<RoleSearchCriteria> {
    private static final long serialVersionUID = 1L;

    @Override
    protected SearchLayout<RoleSearchCriteria> createBasicSearchLayout() {
        return new RoleBasicSearchLayout();
    }

    @Override
    protected SearchLayout<RoleSearchCriteria> createAdvancedSearchLayout() {
        return null;
    }

    @Override
    protected HeaderWithFontAwesome buildSearchTitle() {
        return HeaderWithFontAwesome.h2(FontAwesome.USERS, AppContext.getMessage(RoleI18nEnum.LIST));
    }

    @Override
    protected Component buildExtraControls() {
        Button createBtn = new Button(AppContext.getMessage(RoleI18nEnum.NEW), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final Button.ClickEvent event) {
                EventBusFactory.getInstance().post(new RoleEvent.GotoAdd(this, null));
            }
        });
        createBtn.setStyleName(UIConstants.BUTTON_ACTION);
        createBtn.setIcon(FontAwesome.PLUS);
        createBtn.setEnabled(AppContext.canWrite(RolePermissionCollections.ACCOUNT_ROLE));
        return createBtn;
    }

    private class RoleBasicSearchLayout extends GenericSearchPanel.BasicSearchLayout<RoleSearchCriteria> {
        private static final long serialVersionUID = 1L;

        private TextField nameField;

        public RoleBasicSearchLayout() {
            super(RoleSearchPanel.this);
        }

        @Override
        public ComponentContainer constructHeader() {
            return RoleSearchPanel.this.constructHeader();
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true).with(new Label("Name"));

            nameField = ShortcutExtension.installShortcutAction(new TextField(),
                    new ShortcutListener("RoleSearchName", ShortcutAction.KeyCode.ENTER, null) {
                        @Override
                        public void handleAction(Object o, Object o1) {
                            callSearchAction();
                        }
                    });
            nameField.setInputPrompt(AppContext.getMessage(RoleI18nEnum.OPT_QUERY_BY_ROLE_NAME));
            nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.addComponent(nameField);

            Button searchBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent -> callSearchAction());
            searchBtn.setIcon(FontAwesome.SEARCH);
            searchBtn.setStyleName(UIConstants.BUTTON_ACTION);
            basicSearchBody.addComponent(searchBtn);

            Button clearBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> nameField.setValue(""));
            clearBtn.setStyleName(UIConstants.BUTTON_OPTION);
            basicSearchBody.addComponent(clearBtn);
            basicSearchBody.setComponentAlignment(clearBtn, Alignment.MIDDLE_LEFT);
            return basicSearchBody;
        }

        @Override
        protected RoleSearchCriteria fillUpSearchCriteria() {
            RoleSearchCriteria searchCriteria = new RoleSearchCriteria();
            if (StringUtils.isNotBlank(this.nameField.getValue())) {
                searchCriteria.setRoleName(StringSearchField.and(this.nameField.getValue()));
            }
            return searchCriteria;
        }
    }
}