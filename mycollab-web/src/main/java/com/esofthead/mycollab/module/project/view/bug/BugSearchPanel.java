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
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.ProjectViewHeader;
import com.esofthead.mycollab.module.project.view.bug.components.ComponentListSelect;
import com.esofthead.mycollab.module.project.view.bug.components.VersionListSelect;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberListSelect;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.maddon.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BugSearchPanel extends DefaultGenericSearchPanel<BugSearchCriteria> {
    private static final long serialVersionUID = 1L;

    private BugSearchCriteria searchCriteria;

    private static Param[] paramFields = new Param[]{
            BugSearchCriteria.p_textDesc, BugSearchCriteria.p_priority,
            BugSearchCriteria.p_severity, BugSearchCriteria.p_status,
            BugSearchCriteria.p_affectedVersions,
            BugSearchCriteria.p_fixedVersions, BugSearchCriteria.p_components,
            BugSearchCriteria.p_assignee, BugSearchCriteria.p_resolveddate,
            BugSearchCriteria.p_duedate, BugSearchCriteria.p_createdtime,
            BugSearchCriteria.p_lastupdatedtime};

    @Override
    protected HeaderWithFontAwesome buildSearchTitle() {
        return new ProjectViewHeader(ProjectTypeConstants.BUG, AppContext.getMessage(BugI18nEnum.VIEW_LIST_TITLE));
    }

    @Override
    protected void buildExtraControls() {

    }

    @Override
    protected SearchLayout<BugSearchCriteria> createBasicSearchLayout() {
        return new BugBasicSearchLayout();
    }

    @Override
    protected SearchLayout<BugSearchCriteria> createAdvancedSearchLayout() {
        return new BugAdvancedSearchLayout();
    }

    private class BugBasicSearchLayout extends BasicSearchLayout<BugSearchCriteria> {
        private static final long serialVersionUID = 1L;
        private TextField nameField;
        private CheckBox myItemCheckbox;

        public BugBasicSearchLayout() {
            super(BugSearchPanel.this);
        }

        @SuppressWarnings("serial")
        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);
            Label nameLbl = new Label("Name:");
            basicSearchBody.with(nameLbl).withAlign(nameLbl, Alignment.MIDDLE_LEFT);

            nameField = ShortcutExtension.installShortcutAction(new TextField(),
                    new ShortcutListener("BugSearchRequest", ShortcutAction.KeyCode.ENTER, null) {
                        @Override
                        public void handleAction(Object o, Object o1) {
                            callSearchAction();
                        }
                    });
            nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
            nameField.setInputPrompt("Query by name, description or environment");
            basicSearchBody.with(nameField).withAlign(nameLbl, Alignment.MIDDLE_CENTER);

            myItemCheckbox = new CheckBox(AppContext.getMessage(GenericI18Enum.SEARCH_MYITEMS_CHECKBOX));
            basicSearchBody.with(myItemCheckbox).withAlign(myItemCheckbox, Alignment.MIDDLE_CENTER);

            Button searchBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
            searchBtn.setIcon(FontAwesome.SEARCH);
            searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

            searchBtn.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
                    callSearchAction();
                }
            });
            searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
            basicSearchBody.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_LEFT);

            Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR));
            cancelBtn.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
                    BugBasicSearchLayout.this.nameField.setValue("");
                }
            });
            cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
            basicSearchBody.addComponent(cancelBtn);

            Button advancedSearchBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(final ClickEvent event) {
                            moveToAdvancedSearchLayout();
                        }
                    });
            advancedSearchBtn.setStyleName(UIConstants.THEME_LINK);
            basicSearchBody.with(advancedSearchBtn).withAlign(advancedSearchBtn, Alignment.MIDDLE_CENTER);

            return basicSearchBody;
        }

        @Override
        protected BugSearchCriteria fillUpSearchCriteria() {
            searchCriteria = new BugSearchCriteria();
            searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            searchCriteria.setSummary(new StringSearchField(this.nameField.getValue().trim()));
            if (this.myItemCheckbox.getValue()) {
                searchCriteria.setAssignuser(new StringSearchField(AppContext.getUsername()));
            } else {
                searchCriteria.setAssignuser(null);
            }
            return searchCriteria;
        }

        @Override
        public ComponentContainer constructHeader() {
            return BugSearchPanel.this.constructHeader();
        }
    }

    private class BugAdvancedSearchLayout extends DynamicQueryParamLayout<BugSearchCriteria> {
        private static final long serialVersionUID = 1L;

        public BugAdvancedSearchLayout() {
            super(BugSearchPanel.this, ProjectTypeConstants.BUG);
        }

        @Override
        public ComponentContainer constructHeader() {
            return BugSearchPanel.this.constructHeader();
        }

        @Override
        protected Class<BugSearchCriteria> getType() {
            return BugSearchCriteria.class;
        }

        @Override
        public Param[] getParamFields() {
            return paramFields;
        }

        @Override
        protected Component buildSelectionComp(String fieldId) {
            if ("bug-assignuser".equals(fieldId)) {
                return new ProjectMemberListSelect(false);
            } else if ("bug_affected_versions".equals(fieldId)) {
                return new VersionListSelect();
            } else if ("bug_fixed_versions".equals(fieldId)) {
                return new VersionListSelect();
            } else if ("bug_components".equals(fieldId)) {
                return new ComponentListSelect();
            }
            return null;
        }

        @Override
        protected BugSearchCriteria fillUpSearchCriteria() {
            searchCriteria = super.fillUpSearchCriteria();
            searchCriteria.setProjectId(new NumberSearchField(SearchField.AND, CurrentProjectVariables.getProjectId()));
            return searchCriteria;
        }
    }
}