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
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectRoleEvent;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.web.ui.DefaultGenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.esofthead.mycollab.vaadin.web.ui.ShortcutExtension;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
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
public class ProjectRoleSearchPanel extends DefaultGenericSearchPanel<ProjectRoleSearchCriteria> {
    private static final long serialVersionUID = 1L;

    @Override
    protected SearchLayout<ProjectRoleSearchCriteria> createBasicSearchLayout() {
        return new ProjectRoleBasicSearchLayout();
    }

    @Override
    protected SearchLayout<ProjectRoleSearchCriteria> createAdvancedSearchLayout() {
        return null;
    }

    @Override
    protected HeaderWithFontAwesome buildSearchTitle() {
        return HeaderWithFontAwesome.h2(FontAwesome.GROUP, AppContext.getMessage(ProjectRoleI18nEnum.VIEW_LIST_TITLE));
    }

    @Override
    protected void buildExtraControls() {
        Button createBtn = new Button(AppContext.getMessage(ProjectMemberI18nEnum.BUTTON_NEW_ROLE), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                EventBusFactory.getInstance().post(new ProjectRoleEvent.GotoAdd(this, null));
            }
        });
        createBtn.setStyleName(UIConstants.BUTTON_ACTION);
        createBtn.setIcon(FontAwesome.PLUS);
        createBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.ROLES));
        this.addHeaderRight(createBtn);
    }

    @SuppressWarnings("rawtypes")
    private class ProjectRoleBasicSearchLayout extends BasicSearchLayout {
        private static final long serialVersionUID = 1L;
        private TextField nameField;

        @SuppressWarnings("unchecked")
        public ProjectRoleBasicSearchLayout() {
            super(ProjectRoleSearchPanel.this);
        }

        @Override
        public ComponentContainer constructHeader() {
            return ProjectRoleSearchPanel.this.constructHeader();
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);
            basicSearchBody.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

            basicSearchBody.addComponent(new Label("Name"));
            nameField = ShortcutExtension.installShortcutAction(new TextField(),
                    new ShortcutListener("RoleSearchText", ShortcutAction.KeyCode.ENTER, null) {
                        @Override
                        public void handleAction(Object o, Object o1) {
                            callSearchAction();
                        }
                    });
            nameField.setInputPrompt("Query by role name");
            nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.addComponent(nameField);

            Button searchBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    callSearchAction();
                }
            });
            searchBtn.setStyleName(UIConstants.BUTTON_ACTION);
            searchBtn.setIcon(FontAwesome.SEARCH);
            basicSearchBody.addComponent(searchBtn);

            Button clearBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    ProjectRoleBasicSearchLayout.this.nameField.setValue("");
                }
            });
            clearBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
            basicSearchBody.addComponent(clearBtn);
            return basicSearchBody;
        }

        @Override
        protected SearchCriteria fillUpSearchCriteria() {
            ProjectRoleSearchCriteria searchCriteria = new ProjectRoleSearchCriteria();
            searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            searchCriteria.setRolename(StringSearchField.and(nameField.getValue()));
            return searchCriteria;
        }
    }
}