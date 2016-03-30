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
package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.esofthead.mycollab.vaadin.web.ui.DefaultGenericSearchPanel;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.0.0
 */
public class FollowingTicketSearchPanel extends DefaultGenericSearchPanel<FollowingTicketSearchCriteria> {
    private static final long serialVersionUID = 1L;

    private FollowingTicketBasicSearchLayout basicSearchLayout;

    private List<SimpleProject> projects;

    @Override
    protected HeaderWithFontAwesome buildSearchTitle() {
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
        basicSearchLayout.callSearchAction();
    }

    @SuppressWarnings("rawtypes")
    private class FollowingTicketBasicSearchLayout extends BasicSearchLayout {
        private static final long serialVersionUID = 1L;

        private UserInvolvedProjectsListSelect projectField;
        private TextField summaryField;
        private CheckBox taskSelect, bugSelect, riskSelect;

        @SuppressWarnings("unchecked")
        public FollowingTicketBasicSearchLayout() {
            super(FollowingTicketSearchPanel.this);
        }

        @Override
        public ComponentContainer constructHeader() {
            return null;
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
            summaryField.setInputPrompt("Query by name");
            selectionLayout.addComponent(summaryField, 1, 0);

            Label typeLb = new Label("Type:");
            typeLb.setWidthUndefined();

            selectionLayout.addComponent(typeLb, 0, 1);

            MHorizontalLayout typeSelectWrapper = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, false));
            selectionLayout.addComponent(typeSelectWrapper, 1, 1);

            this.taskSelect = new CheckBox("Task", true);
            this.taskSelect.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK));

            this.bugSelect = new CheckBox("Bug", true);
            this.bugSelect.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG));

            this.riskSelect = new CheckBox("Risk", true);
            this.riskSelect.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.RISK));

            typeSelectWrapper.with(this.taskSelect, this.bugSelect, this.riskSelect);

            Label projectLb = new Label("Project:");
            projectLb.setWidthUndefined();
            selectionLayout.addComponent(projectLb, 2, 0);

            projectField = new UserInvolvedProjectsListSelect();
            projectField.setWidth("300px");
            projectField.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
            projectField.setNullSelectionAllowed(false);
            projectField.setMultiSelect(true);
            projectField.setRows(4);
            selectionLayout.addComponent(projectField, 3, 0, 3, 1);

            Button queryBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SUBMIT), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    doSearch();
                }
            });
            queryBtn.setStyleName(UIConstants.BUTTON_ACTION);
            selectionLayout.addComponent(queryBtn, 4, 0);

            return basicSearchBody;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected SearchCriteria fillUpSearchCriteria() {
            FollowingTicketSearchCriteria searchCriteria = new FollowingTicketSearchCriteria();
            searchCriteria.setUser(StringSearchField.and(AppContext.getUsername()));

            List<String> types = new ArrayList<>();
            if (this.taskSelect.getValue()) {
                types.add(ProjectTypeConstants.TASK);
            }
            if (this.bugSelect.getValue()) {
                types.add(ProjectTypeConstants.BUG);
            }
            if (this.riskSelect.getValue()) {
                types.add(ProjectTypeConstants.RISK);
            }

            if (types.size() > 0) {
                searchCriteria.setTypes(new SetSearchField<>(types.toArray(new String[types.size()])));
            } else {
                searchCriteria.setTypes(null);
            }

            String summary = summaryField.getValue().trim();
            searchCriteria.setSummary(StringSearchField.and(StringUtils.isEmpty(summary) ? "" : summary));

            Collection<Integer> selectedProjects = (Collection<Integer>) projectField.getValue();
            if (CollectionUtils.isNotEmpty(selectedProjects)) {
                searchCriteria.setExtraTypeIds(new SetSearchField<>(selectedProjects.toArray(new Integer[selectedProjects.size()])));
            } else {
                List<Integer> keys = new ArrayList<>();
                for (SimpleProject project : projects) {
                    keys.add(project.getId());
                }
                if (keys.size() > 0) {
                    searchCriteria.setExtraTypeIds(new SetSearchField<>(keys.toArray(new Integer[keys.size()])));
                }
            }

            return searchCriteria;
        }
    }

    private class UserInvolvedProjectsListSelect extends ListSelect {
        private static final long serialVersionUID = 1L;

        public UserInvolvedProjectsListSelect() {
            FollowingTicketSearchPanel.this.projects = ApplicationContextUtil.getSpringBean(ProjectService.class)
                    .getProjectsUserInvolved(AppContext.getUsername(), AppContext.getAccountId());

            for (SimpleProject project : projects) {
                this.addItem(project.getId());
                this.setItemCaption(project.getId(), project.getName());
            }
        }
    }
}
