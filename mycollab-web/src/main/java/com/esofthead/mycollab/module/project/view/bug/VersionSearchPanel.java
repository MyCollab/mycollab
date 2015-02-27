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
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.events.BugVersionEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.VersionI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.ProjectViewHeader;
import com.esofthead.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.GenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.maddon.layouts.MHorizontalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class VersionSearchPanel extends
		GenericSearchPanel<VersionSearchCriteria> {

    private static final long serialVersionUID = 1L;
    private final SimpleProject project;
    protected VersionSearchCriteria searchCriteria;

    public VersionSearchPanel() {
        this.project = CurrentProjectVariables.getProject();
    }

    @Override
    public void attach() {
        super.attach();
        this.createBasicSearchLayout();
    }

    private void createBasicSearchLayout() {

        this.setCompositionRoot(new VersionBasicSearchLayout());
    }

    private HorizontalLayout createSearchTopPanel() {
        final MHorizontalLayout layout = new MHorizontalLayout()
                .withStyleName(UIConstants.HEADER_VIEW).withWidth("100%")
                .withMargin(new MarginInfo(true, false, true, false));

        final Label versionTitle = new ProjectViewHeader(ProjectTypeConstants.BUG_VERSION,
                AppContext.getMessage(VersionI18nEnum.VIEW_LIST_TITLE));
        versionTitle.setStyleName(UIConstants.HEADER_TEXT);
        layout.with(versionTitle)
                .withAlign(versionTitle, Alignment.MIDDLE_LEFT)
                .expand(versionTitle);

        final Button createBtn = new Button(
                AppContext.getMessage(BugI18nEnum.BUTTON_NEW_VERSION),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        EventBusFactory.getInstance().post(
                                new BugVersionEvent.GotoAdd(this, null));
                    }
                });
        createBtn.setEnabled(CurrentProjectVariables
                .canWrite(ProjectRolePermissionCollections.VERSIONS));
        createBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        createBtn.setIcon(FontAwesome.PLUS);

        layout.with(createBtn).withAlign(createBtn, Alignment.MIDDLE_LEFT);

        return layout;
    }

    @SuppressWarnings("rawtypes")
    private class VersionBasicSearchLayout extends GenericSearchPanel.BasicSearchLayout {

        @SuppressWarnings("unchecked")
        public VersionBasicSearchLayout() {
            super(VersionSearchPanel.this);
        }

        private static final long serialVersionUID = 1L;
        private TextField nameField;

        @Override
        public ComponentContainer constructHeader() {
            return VersionSearchPanel.this.createSearchTopPanel();
        }

        @Override
        public ComponentContainer constructBody() {
            final MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);

            Label nameLbl = new Label("Name:");
            basicSearchBody.with(nameLbl).withAlign(nameLbl, Alignment.MIDDLE_LEFT);

            this.nameField = new TextField();
            this.nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
            this.nameField.addShortcutListener(new ShortcutListener("VersionSearchName", ShortcutAction.KeyCode
                    .ENTER, null) {
                @Override
                public void handleAction(Object o, Object o1) {
                    callSearchAction();
                }
            });
            basicSearchBody.with(nameField).withAlign(nameField, Alignment.MIDDLE_CENTER);

            final Button searchBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
            searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
            searchBtn.setIcon(FontAwesome.SEARCH);

            searchBtn.addClickListener(new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    callSearchAction();
                }
            });
            basicSearchBody.with(searchBtn).withAlign(searchBtn,
                    Alignment.MIDDLE_CENTER);

            final Button cancelBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR));
            cancelBtn.addClickListener(new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    VersionBasicSearchLayout.this.nameField.setValue("");
                }
            });
            cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
            basicSearchBody.with(cancelBtn);

            return basicSearchBody;
        }

        @Override
        protected SearchCriteria fillUpSearchCriteria() {
            searchCriteria = new VersionSearchCriteria();
            searchCriteria.setProjectId(new NumberSearchField(SearchField.AND, project.getId()));
            searchCriteria.setVersionname(new StringSearchField(this.nameField.getValue().trim()));
            return searchCriteria;
        }
    }
}