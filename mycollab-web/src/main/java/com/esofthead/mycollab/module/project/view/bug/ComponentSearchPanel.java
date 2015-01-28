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
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.events.BugComponentEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ComponentI18nEnum;
import com.esofthead.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.GenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.WebResourceIds;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.maddon.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ComponentSearchPanel extends
        GenericSearchPanel<ComponentSearchCriteria> {

    private static final long serialVersionUID = 1L;
    private final SimpleProject project;
    protected ComponentSearchCriteria searchCriteria;

    public ComponentSearchPanel() {
        this.project = CurrentProjectVariables.getProject();
    }

    @Override
    public void attach() {
        super.attach();
        this.createBasicSearchLayout();
    }

    private void createBasicSearchLayout() {
        this.setCompositionRoot(new ComponentBasicSearchCriteria());
    }

    private HorizontalLayout createSearchTopPanel() {
        final MHorizontalLayout layout = new MHorizontalLayout()
                .withWidth("100%").withSpacing(true)
                .withStyleName(UIConstants.HEADER_VIEW)
                .withMargin(new MarginInfo(true, false, true, false));

        final Image titleIcon = new Image(null,
                MyCollabResource
                        .newResource(WebResourceIds._22_project_component));
        layout.with(titleIcon).withAlign(titleIcon, Alignment.MIDDLE_LEFT);

        final Label componenttitle = new Label(
                AppContext.getMessage(ComponentI18nEnum.VIEW_LIST_TITLE));
        componenttitle.setStyleName(UIConstants.HEADER_TEXT);

        layout.with(componenttitle)
                .withAlign(componenttitle, Alignment.MIDDLE_LEFT)
                .expand(componenttitle);

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
        createBtn.setIcon(MyCollabResource
                .newResource(WebResourceIds._16_addRecord));

        layout.with(createBtn).withAlign(createBtn, Alignment.MIDDLE_RIGHT);

        return layout;
    }

    @SuppressWarnings("rawtypes")
    private class ComponentBasicSearchCriteria extends
            GenericSearchPanel.BasicSearchLayout {

        @SuppressWarnings("unchecked")
        public ComponentBasicSearchCriteria() {
            super(ComponentSearchPanel.this);
        }

        private static final long serialVersionUID = 1L;
        private TextField nameField;
        private CheckBox myItemCheckbox;

        @Override
        public ComponentContainer constructHeader() {
            return ComponentSearchPanel.this.createSearchTopPanel();
        }

        @Override
        public ComponentContainer constructBody() {
            final MHorizontalLayout basicSearchBody = new MHorizontalLayout()
                    .withSpacing(true).withMargin(true);
            Label nameLbl = new Label("Name:");
            basicSearchBody.with(nameLbl).withAlign(nameLbl,
                    Alignment.MIDDLE_LEFT);

            this.nameField = new TextField();
            this.nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.with(nameField).withAlign(nameField,
                    Alignment.MIDDLE_CENTER);

            this.myItemCheckbox = new CheckBox(
                    AppContext
                            .getMessage(GenericI18Enum.SEARCH_MYITEMS_CHECKBOX));
            basicSearchBody.with(myItemCheckbox).withAlign(myItemCheckbox,
                    Alignment.MIDDLE_CENTER);

            final Button searchBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
            searchBtn.setIcon(MyCollabResource
                    .newResource(WebResourceIds._16_search));
            searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

            searchBtn.addClickListener(new Button.ClickListener() {

                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    ComponentBasicSearchCriteria.this.callSearchAction();
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
                    ComponentBasicSearchCriteria.this.nameField.setValue("");
                }
            });
            cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
            basicSearchBody.with(cancelBtn);

            return basicSearchBody;
        }

        @Override
        protected SearchCriteria fillupSearchCriteria() {
            ComponentSearchPanel.this.searchCriteria = new ComponentSearchCriteria();
            ComponentSearchPanel.this.searchCriteria
                    .setProjectid(new NumberSearchField(SearchField.AND,
                            ComponentSearchPanel.this.project.getId()));
            ComponentSearchPanel.this.searchCriteria
                    .setComponentName(new StringSearchField(this.nameField
                            .getValue().trim()));

            if (this.myItemCheckbox.getValue()) {
                ComponentSearchPanel.this.searchCriteria
                        .setUserlead(new StringSearchField(SearchField.AND,
                                AppContext.getUsername()));
            } else {
                ComponentSearchPanel.this.searchCriteria.setUserlead(null);
            }

            return ComponentSearchPanel.this.searchCriteria;
        }
    }

}
