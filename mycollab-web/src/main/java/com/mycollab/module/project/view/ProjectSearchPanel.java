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
package com.mycollab.module.project.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.db.query.Param;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.ui.components.ComponentUtils;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.web.ui.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class ProjectSearchPanel extends DefaultGenericSearchPanel<ProjectSearchCriteria> {
    private static Param[] paramFields = new Param[]{
            ProjectSearchCriteria.p_name, ProjectSearchCriteria.p_status, ProjectSearchCriteria.p_startdate,
            ProjectSearchCriteria.p_enddate, ProjectSearchCriteria.p_createdtime
    };

    @Override
    protected SearchLayout<ProjectSearchCriteria> createBasicSearchLayout() {
        return new ProjectBasicSearchLayout();
    }

    @Override
    protected SearchLayout<ProjectSearchCriteria> createAdvancedSearchLayout() {
        return new ProjectAdvancedSearchLayout();
    }

    @Override
    protected ComponentContainer buildSearchTitle() {
        return ComponentUtils.headerH2(ProjectTypeConstants.PROJECT, UserUIContext.getMessage(ProjectI18nEnum.LIST));
    }

    @Override
    protected Component buildExtraControls() {
        MButton createBtn = new MButton(UserUIContext.getMessage(ProjectI18nEnum.NEW),
                clickEvent -> UI.getCurrent().addWindow(ViewManager.getCacheComponent(AbstractProjectAddWindow.class)))
                .withStyleName(WebThemes.BUTTON_ACTION).withIcon(FontAwesome.PLUS);
        createBtn.setVisible(UserUIContext.canBeYes(RolePermissionCollections.CREATE_NEW_PROJECT));
        return createBtn;
    }

    private class ProjectBasicSearchLayout extends BasicSearchLayout<ProjectSearchCriteria> {
        private static final long serialVersionUID = 1L;

        private TextField nameField;

        private ProjectBasicSearchLayout() {
            super(ProjectSearchPanel.this);
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);

            nameField = new MTextField().withInputPrompt(UserUIContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT))
                    .withWidth(WebUIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.with(nameField).withAlign(nameField, Alignment.MIDDLE_CENTER);

            MButton searchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent ->
                    callSearchAction()).withIcon(FontAwesome.SEARCH).withStyleName(WebThemes.BUTTON_ACTION)
                    .withClickShortcut(ShortcutAction.KeyCode.ENTER);
            basicSearchBody.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_LEFT);

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLEAR),
                    clickEvent -> nameField.setValue("")).withStyleName(WebThemes.BUTTON_OPTION);
            basicSearchBody.with(cancelBtn).withAlign(cancelBtn, Alignment.MIDDLE_CENTER);

            MButton advancedSearchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
                    clickEvent -> moveToAdvancedSearchLayout()).withStyleName(WebThemes.BUTTON_LINK);
            basicSearchBody.with(advancedSearchBtn).withAlign(advancedSearchBtn, Alignment.MIDDLE_CENTER);

            return basicSearchBody;
        }

        @Override
        protected ProjectSearchCriteria fillUpSearchCriteria() {
            ProjectSearchCriteria searchCriteria = new ProjectSearchCriteria();
            searchCriteria.setProjectName(StringSearchField.and(this.nameField.getValue().trim()));
            return searchCriteria;
        }
    }

    private class ProjectAdvancedSearchLayout extends DynamicQueryParamLayout<ProjectSearchCriteria> {
        private static final long serialVersionUID = 1L;

        private ProjectAdvancedSearchLayout() {
            super(ProjectSearchPanel.this, ProjectTypeConstants.PROJECT);
        }

        @Override
        public Param[] getParamFields() {
            return paramFields;
        }

        @Override
        protected Class<ProjectSearchCriteria> getType() {
            return ProjectSearchCriteria.class;
        }

        @Override
        protected Component buildSelectionComp(String fieldId) {
            return null;
        }
    }
}
