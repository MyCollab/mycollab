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
package com.mycollab.module.project.view.settings;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.events.BugComponentEvent;
import com.mycollab.module.project.i18n.ComponentI18nEnum;
import com.mycollab.module.project.ui.components.ComponentUtils;
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.mycollab.vaadin.web.ui.DefaultGenericSearchPanel;
import com.mycollab.vaadin.web.ui.ShortcutExtension;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ComponentSearchPanel extends DefaultGenericSearchPanel<ComponentSearchCriteria> {
    private static final long serialVersionUID = 1L;

    @Override
    protected SearchLayout<ComponentSearchCriteria> createBasicSearchLayout() {
        return new ComponentBasicSearchLayout();
    }

    @Override
    protected SearchLayout<ComponentSearchCriteria> createAdvancedSearchLayout() {
        return null;
    }

    @Override
    protected HeaderWithFontAwesome buildSearchTitle() {
        return ComponentUtils.headerH2(ProjectTypeConstants.BUG_COMPONENT, AppContext.getMessage(ComponentI18nEnum.LIST));
    }

    @Override
    protected Component buildExtraControls() {
        Button createBtn = new Button(AppContext.getMessage(ComponentI18nEnum.NEW), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final Button.ClickEvent event) {
                EventBusFactory.getInstance().post(new BugComponentEvent.GotoAdd(this, null));
            }
        });
        createBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.COMPONENTS));
        createBtn.setStyleName(UIConstants.BUTTON_ACTION);
        createBtn.setIcon(FontAwesome.PLUS);
        return createBtn;
    }

    private class ComponentBasicSearchLayout extends BasicSearchLayout<ComponentSearchCriteria> {
        private static final long serialVersionUID = 1L;
        private TextField nameField;
        private CheckBox myItemCheckbox;

        public ComponentBasicSearchLayout() {
            super(ComponentSearchPanel.this);
        }

        @Override
        public ComponentContainer constructHeader() {
            return ComponentSearchPanel.this.constructHeader();
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);
            basicSearchBody.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
            Label nameLbl = new Label("Name:");
            basicSearchBody.with(nameLbl);

            nameField = ShortcutExtension.installShortcutAction(new TextField(),
                    new ShortcutListener("ComponentSearchName", ShortcutAction.KeyCode.ENTER, null) {
                        @Override
                        public void handleAction(Object o, Object o1) {
                            callSearchAction();
                        }
                    });
            nameField.setInputPrompt("Query by component name");
            nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.with(nameField);

            myItemCheckbox = new CheckBox(AppContext.getMessage(GenericI18Enum.OPT_MY_ITEMS));
            basicSearchBody.with(myItemCheckbox);

            Button searchBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    callSearchAction();
                }
            });
            searchBtn.setIcon(FontAwesome.SEARCH);
            searchBtn.setStyleName(UIConstants.BUTTON_ACTION);
            basicSearchBody.with(searchBtn);

            Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    nameField.setValue("");
                }
            });
            cancelBtn.setStyleName(UIConstants.BUTTON_OPTION);
            basicSearchBody.with(cancelBtn);

            return basicSearchBody;
        }

        @Override
        protected ComponentSearchCriteria fillUpSearchCriteria() {
            ComponentSearchCriteria searchCriteria = new ComponentSearchCriteria();
            searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            searchCriteria.setComponentName(StringSearchField.and(this.nameField.getValue().trim()));

            if (this.myItemCheckbox.getValue()) {
                searchCriteria.setUserlead(StringSearchField.and(AppContext.getUsername()));
            } else {
                searchCriteria.setUserlead(null);
            }

            return searchCriteria;
        }
    }

}
