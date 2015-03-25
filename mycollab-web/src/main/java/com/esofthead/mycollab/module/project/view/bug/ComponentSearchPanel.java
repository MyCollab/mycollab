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

package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.events.BugComponentEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ComponentI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.ProjectViewHeader;
import com.esofthead.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.DefaultGenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.esofthead.mycollab.vaadin.ui.ShortcutExtension;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.maddon.layouts.MHorizontalLayout;

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
        return new ProjectViewHeader(ProjectTypeConstants.BUG_COMPONENT,
                AppContext.getMessage(ComponentI18nEnum.VIEW_LIST_TITLE));
    }

    @Override
    protected void buildExtraControls() {
        final Button createBtn = new Button(
                AppContext.getMessage(BugI18nEnum.BUTTON_NEW_COMPONENT),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        EventBusFactory.getInstance().post(
                                new BugComponentEvent.GotoAdd(this, null));
                    }
                });
        createBtn.setEnabled(CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.COMPONENTS));
        createBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        createBtn.setIcon(FontAwesome.PLUS);
        this.addHeaderRight(createBtn);
    }

    @SuppressWarnings("rawtypes")
    private class ComponentBasicSearchLayout extends BasicSearchLayout {
        private static final long serialVersionUID = 1L;
        private TextField nameField;
        private CheckBox myItemCheckbox;

        @SuppressWarnings("unchecked")
        public ComponentBasicSearchLayout() {
            super(ComponentSearchPanel.this);
        }

        @Override
        public ComponentContainer constructHeader() {
            return ComponentSearchPanel.this.constructHeader();
        }

        @Override
        public ComponentContainer constructBody() {
            final MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);
            Label nameLbl = new Label("Name:");
            basicSearchBody.with(nameLbl).withAlign(nameLbl, Alignment.MIDDLE_LEFT);

            nameField = ShortcutExtension.installShortcutAction(new TextField(),
                    new ShortcutListener("ComponentSearchName", ShortcutAction.KeyCode.ENTER, null) {
                        @Override
                        public void handleAction(Object o, Object o1) {
                            callSearchAction();
                        }
                    });
            nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.with(nameField).withAlign(nameField, Alignment.MIDDLE_CENTER);

            myItemCheckbox = new CheckBox(
                    AppContext.getMessage(GenericI18Enum.SEARCH_MYITEMS_CHECKBOX));
            basicSearchBody.with(myItemCheckbox).withAlign(myItemCheckbox,
                    Alignment.MIDDLE_CENTER);

            final Button searchBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
            searchBtn.setIcon(FontAwesome.SEARCH);
            searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

            searchBtn.addClickListener(new Button.ClickListener() {

                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    callSearchAction();
                }
            });
            basicSearchBody.with(searchBtn).withAlign(searchBtn,
                    Alignment.MIDDLE_LEFT);

            final Button cancelBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR));
            cancelBtn.addClickListener(new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    nameField.setValue("");
                }
            });
            cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
            basicSearchBody.with(cancelBtn);

            return basicSearchBody;
        }

        @Override
        protected SearchCriteria fillUpSearchCriteria() {
            ComponentSearchCriteria searchCriteria = new ComponentSearchCriteria();
            searchCriteria.setProjectid(new NumberSearchField(SearchField.AND, CurrentProjectVariables.getProjectId()));
            searchCriteria.setComponentName(new StringSearchField(this.nameField.getValue().trim()));

            if (this.myItemCheckbox.getValue()) {
                searchCriteria.setUserlead(new StringSearchField(SearchField.AND,
                        AppContext.getUsername()));
            } else {
                searchCriteria.setUserlead(null);
            }

            return searchCriteria;
        }
    }

}
