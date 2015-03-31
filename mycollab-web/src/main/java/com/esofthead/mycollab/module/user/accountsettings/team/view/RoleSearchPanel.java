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

package com.esofthead.mycollab.module.user.accountsettings.team.view;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.esofthead.mycollab.module.user.domain.criteria.RoleSearchCriteria;
import com.esofthead.mycollab.module.user.events.RoleEvent;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.maddon.layouts.MHorizontalLayout;

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
        return new HeaderWithFontAwesome(FontAwesome.USERS, AppContext.getMessage(RoleI18nEnum.VIEW_LIST_TITLE));
    }

    @Override
    protected void buildExtraControls() {
        final Button createBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_CREATE),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        EventBusFactory.getInstance().post(
                                new RoleEvent.GotoAdd(this, null));
                    }
                });
        createBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        createBtn.setIcon(FontAwesome.PLUS);
        createBtn.setEnabled(AppContext
                .canWrite(RolePermissionCollections.ACCOUNT_ROLE));
        this.addHeaderRight(createBtn);
    }

    @SuppressWarnings("rawtypes")
    private class RoleBasicSearchLayout extends
            GenericSearchPanel.BasicSearchLayout {

        @SuppressWarnings("unchecked")
        public RoleBasicSearchLayout() {
            super(RoleSearchPanel.this);
        }

        private static final long serialVersionUID = 1L;
        private TextField nameField;

        @Override
        public ComponentContainer constructHeader() {
            return RoleSearchPanel.this.constructHeader();
        }

        @Override
        public ComponentContainer constructBody() {
            final MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true).with(new Label("Name"));

            nameField = ShortcutExtension.installShortcutAction(new TextField(),
                    new ShortcutListener("RoleSearchName", ShortcutAction.KeyCode.ENTER,
                            null) {
                        @Override
                        public void handleAction(Object o, Object o1) {
                            callSearchAction();
                        }
                    });
            nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.addComponent(nameField);

            final Button searchBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
            searchBtn.setIcon(FontAwesome.SEARCH);
            searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
            searchBtn.addClickListener(new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    RoleBasicSearchLayout.this.callSearchAction();
                }
            });
            basicSearchBody.addComponent(searchBtn);

            final Button clearBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final Button.ClickEvent event) {
                            RoleBasicSearchLayout.this.nameField.setValue("");
                        }
                    });
            clearBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
            basicSearchBody.addComponent(clearBtn);
            basicSearchBody.setComponentAlignment(clearBtn, Alignment.MIDDLE_LEFT);
            return basicSearchBody;
        }

        @Override
        protected SearchCriteria fillUpSearchCriteria() {
            RoleSearchCriteria searchCriteria = new RoleSearchCriteria();
            if (StringUtils.isNotBlank(this.nameField.getValue())) {
                searchCriteria
                        .setRoleName(new StringSearchField(SearchField.AND,
                                this.nameField.getValue()));
            }
            return searchCriteria;
        }
    }
}