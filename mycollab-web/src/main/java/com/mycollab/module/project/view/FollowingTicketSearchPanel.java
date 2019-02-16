/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.Project;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.RiskI18nEnum;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.HeaderWithIcon;
import com.mycollab.vaadin.web.ui.BasicSearchLayout;
import com.mycollab.vaadin.web.ui.DefaultGenericSearchPanel;
import com.mycollab.vaadin.web.ui.SearchLayout;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MyCollab Ltd.
 * @since 4.0.0
 */
public class FollowingTicketSearchPanel extends DefaultGenericSearchPanel<FollowingTicketSearchCriteria> {
    private static final long serialVersionUID = 1L;

    private FollowingTicketBasicSearchLayout basicSearchLayout;

    private List<SimpleProject> projects;

    @Override
    protected HeaderWithIcon buildSearchTitle() {
        return null;
    }

    @Override
    protected SearchLayout<FollowingTicketSearchCriteria> createBasicSearchLayout() {
        basicSearchLayout = new FollowingTicketBasicSearchLayout();
        return basicSearchLayout;
    }

    @Override
    protected SearchLayout<FollowingTicketSearchCriteria> createAdvancedSearchLayout() {
        return null;
    }

    public void doSearch() {
        this.callSearchAction();
    }

    private class FollowingTicketBasicSearchLayout extends BasicSearchLayout<FollowingTicketSearchCriteria> {
        private static final long serialVersionUID = 1L;

        private UserInvolvedProjectsListSelect projectField;
        private TextField summaryField;
        private CheckBox taskSelect, bugSelect, riskSelect;

        FollowingTicketBasicSearchLayout() {
            super(FollowingTicketSearchPanel.this);
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);

            GridLayout selectionLayout = new GridLayout(5, 2);
            selectionLayout.setSpacing(true);
            selectionLayout.setMargin(true);
            selectionLayout.setDefaultComponentAlignment(Alignment.TOP_RIGHT);
            basicSearchBody.addComponent(selectionLayout);

            Label summaryLb = new Label("Summary:");
            summaryLb.setWidthUndefined();
            selectionLayout.addComponent(summaryLb, 0, 0);

            summaryField = new TextField();
            summaryField.setWidth("100%");
            summaryField.setPlaceholder(UserUIContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT));
            selectionLayout.addComponent(summaryField, 1, 0);

            Label typeLb = new Label("Type:");
            typeLb.setWidthUndefined();

            selectionLayout.addComponent(typeLb, 0, 1);

            MHorizontalLayout typeSelectWrapper = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, false));
            selectionLayout.addComponent(typeSelectWrapper, 1, 1);

            taskSelect = new CheckBox(UserUIContext.getMessage(TaskI18nEnum.SINGLE), true);
            taskSelect.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK));

            bugSelect = new CheckBox(UserUIContext.getMessage(BugI18nEnum.SINGLE), true);
            bugSelect.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG));

            riskSelect = new CheckBox(UserUIContext.getMessage(RiskI18nEnum.SINGLE), true);
            riskSelect.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.RISK));

            typeSelectWrapper.with(taskSelect, bugSelect, riskSelect);

            Label projectLb = new Label("Project:");
            projectLb.setWidthUndefined();
            selectionLayout.addComponent(projectLb, 2, 0);

            projectField = new UserInvolvedProjectsListSelect();
            projectField.setWidth("300px");
            projectField.setRows(4);
            selectionLayout.addComponent(projectField, 3, 0, 3, 1);

            MButton queryBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SUBMIT), clickEvent -> doSearch())
                    .withStyleName(WebThemes.BUTTON_ACTION);
            selectionLayout.addComponent(queryBtn, 4, 0);

            return basicSearchBody;
        }

        @Override
        protected FollowingTicketSearchCriteria fillUpSearchCriteria() {
            FollowingTicketSearchCriteria searchCriteria = new FollowingTicketSearchCriteria();
            searchCriteria.setUser(StringSearchField.and(UserUIContext.getUsername()));

            List<String> types = new ArrayList<>();
            if (taskSelect.getValue()) {
                types.add(ProjectTypeConstants.TASK);
            }
            if (bugSelect.getValue()) {
                types.add(ProjectTypeConstants.BUG);
            }
            if (riskSelect.getValue()) {
                types.add(ProjectTypeConstants.RISK);
            }

            if (types.size() > 0) {
                searchCriteria.setTypes(new SetSearchField<>(types.toArray(new String[types.size()])));
            } else {
                searchCriteria.setTypes(null);
            }

            String summary = summaryField.getValue().trim();
            searchCriteria.setName(StringSearchField.and(StringUtils.isEmpty(summary) ? "" : summary));

            Collection<SimpleProject> selectedProjects = projectField.getValue();
            if (CollectionUtils.isNotEmpty(selectedProjects)) {
                List<Integer> keys = selectedProjects.stream().map(Project::getId).collect(Collectors.toList());
                searchCriteria.setExtraTypeIds(new SetSearchField<>(keys));
            } else {
                List<Integer> keys = projects.stream().map(Project::getId).collect(Collectors.toList());
                if (keys.size() > 0) {
                    searchCriteria.setExtraTypeIds(new SetSearchField<>(keys.toArray(new Integer[keys.size()])));
                }
            }

            return searchCriteria;
        }
    }

    private class UserInvolvedProjectsListSelect extends ListSelect<SimpleProject> {
        private static final long serialVersionUID = 1L;

        UserInvolvedProjectsListSelect() {
            projects = AppContextUtil.getSpringBean(ProjectService.class)
                    .getProjectsUserInvolved(UserUIContext.getUsername(), AppUI.getAccountId());
            setItems(projects);
            setItemCaptionGenerator((ItemCaptionGenerator<SimpleProject>) Project::getName);
        }
    }
}
