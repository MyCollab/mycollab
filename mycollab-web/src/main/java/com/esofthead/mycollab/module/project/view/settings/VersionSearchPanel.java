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

package com.esofthead.mycollab.module.project.view.settings;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.events.BugVersionEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.VersionI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.ComponentUtils;
import com.esofthead.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.esofthead.mycollab.vaadin.web.ui.DefaultGenericSearchPanel;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class VersionSearchPanel extends DefaultGenericSearchPanel<VersionSearchCriteria> {
    private static final long serialVersionUID = 1L;
    protected VersionSearchCriteria searchCriteria;

    protected SearchLayout<VersionSearchCriteria> createBasicSearchLayout() {
        return new VersionBasicSearchLayout();
    }

    @Override
    protected SearchLayout<VersionSearchCriteria> createAdvancedSearchLayout() {
        return null;
    }

    @Override
    protected HeaderWithFontAwesome buildSearchTitle() {
        return ComponentUtils.headerH2(ProjectTypeConstants.BUG_VERSION, AppContext.getMessage(VersionI18nEnum.VIEW_LIST_TITLE));
    }

    @Override
    protected Component buildExtraControls() {
        Button createBtn = new Button(AppContext.getMessage(BugI18nEnum.BUTTON_NEW_VERSION), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final Button.ClickEvent event) {
                EventBusFactory.getInstance().post(new BugVersionEvent.GotoAdd(this, null));
            }
        });
        createBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.VERSIONS));
        createBtn.setStyleName(UIConstants.BUTTON_ACTION);
        createBtn.setIcon(FontAwesome.PLUS);
        return createBtn;
    }

    private class VersionBasicSearchLayout extends BasicSearchLayout<VersionSearchCriteria> {
        private static final long serialVersionUID = 1L;
        private TextField nameField;

        VersionBasicSearchLayout() {
            super(VersionSearchPanel.this);
        }

        @Override
        public ComponentContainer constructHeader() {
            return VersionSearchPanel.this.constructHeader();
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);
            basicSearchBody.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

            Label nameLbl = new Label("Name:");
            basicSearchBody.with(nameLbl);

            nameField = new TextField();
            nameField.setInputPrompt("Query by version name");
            nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
            nameField.addShortcutListener(new ShortcutListener("VersionSearchName", ShortcutAction.KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object o, Object o1) {
                    callSearchAction();
                }
            });
            basicSearchBody.with(nameField);

            Button searchBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    callSearchAction();
                }
            });
            searchBtn.setStyleName(UIConstants.BUTTON_ACTION);
            searchBtn.setIcon(FontAwesome.SEARCH);
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
        protected VersionSearchCriteria fillUpSearchCriteria() {
            searchCriteria = new VersionSearchCriteria();
            searchCriteria.setProjectId(new NumberSearchField(SearchField.AND, CurrentProjectVariables.getProjectId()));
            searchCriteria.setVersionname(StringSearchField.and(nameField.getValue().trim()));
            return searchCriteria;
        }
    }
}