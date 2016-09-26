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
        return HeaderWithFontAwesome.h2(FontAwesome.USERS, UserUIContext.getMessage(RoleI18nEnum.LIST));
    }

    @Override
    protected Component buildExtraControls() {
        return new MButton(UserUIContext.getMessage(RoleI18nEnum.NEW),
                clickEvent -> EventBusFactory.getInstance().post(new RoleEvent.GotoAdd(this, null)))
                .withIcon(FontAwesome.PLUS).withStyleName(WebUIConstants.BUTTON_ACTION)
                .withVisible(UserUIContext.canWrite(RolePermissionCollections.ACCOUNT_ROLE));
    }

    private class RoleBasicSearchLayout extends BasicSearchLayout<RoleSearchCriteria> {
        private static final long serialVersionUID = 1L;

        private TextField nameField;

        private RoleBasicSearchLayout() {
            super(RoleSearchPanel.this);
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true)
                    .with(new Label(UserUIContext.getMessage(GenericI18Enum.FORM_NAME) + ":"));

            nameField = new MTextField().withInputPrompt(UserUIContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT))
                    .withWidth(WebUIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.addComponent(nameField);

            MButton searchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent -> callSearchAction())
                    .withIcon(FontAwesome.SEARCH).withStyleName(WebUIConstants.BUTTON_ACTION)
                    .withClickShortcut(ShortcutAction.KeyCode.ENTER);
            basicSearchBody.addComponent(searchBtn);

            MButton clearBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> nameField.setValue(""))
                    .withStyleName(WebUIConstants.BUTTON_OPTION);
            basicSearchBody.addComponent(clearBtn);
            basicSearchBody.setComponentAlignment(clearBtn, Alignment.MIDDLE_LEFT);
            return basicSearchBody;
        }

        @Override
        protected RoleSearchCriteria fillUpSearchCriteria() {
            RoleSearchCriteria searchCriteria = new RoleSearchCriteria();
            if (StringUtils.isNotBlank(nameField.getValue())) {
                searchCriteria.setRoleName(StringSearchField.and(this.nameField.getValue()));
            }
            return searchCriteria;
        }
    }
}