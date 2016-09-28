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
package com.mycollab.module.project.view.ticket;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.QueryI18nEnum;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.query.ConstantValueInjector;
import com.mycollab.db.query.Param;
import com.mycollab.db.query.SearchFieldInfo;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.event.TicketEvent;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.milestone.MilestoneListSelect;
import com.mycollab.module.project.view.settings.component.ProjectMemberListSelect;
import com.mycollab.shell.events.ShellEvent;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public class TicketSearchPanel extends DefaultGenericSearchPanel<ProjectTicketSearchCriteria> {
    private static final long serialVersionUID = 1L;

    private ProjectTicketSearchCriteria searchCriteria;
    private TicketSavedFilter savedFilterComboBox;

    private static Param[] paramFields = new Param[]{
            ProjectTicketSearchCriteria.p_name, ProjectTicketSearchCriteria.p_priority,
            ProjectTicketSearchCriteria.p_milestones, ProjectTicketSearchCriteria.p_startDate,
            ProjectTicketSearchCriteria.p_endDate, ProjectTicketSearchCriteria.p_dueDate,
            ProjectTicketSearchCriteria.p_assignee, ProjectTicketSearchCriteria.p_createdUser};

    public TicketSearchPanel(boolean canSwitchToAdvanceLayout) {
        super(canSwitchToAdvanceLayout);
    }

    public TicketSearchPanel() {
        super();
    }

    @Override
    protected ComponentContainer buildSearchTitle() {
        if (canSwitchToAdvanceLayout) {
            savedFilterComboBox = new TicketSavedFilter();
            savedFilterComboBox.addQuerySelectListener(new SavedFilterComboBox.QuerySelectListener() {
                @Override
                public void querySelect(SavedFilterComboBox.QuerySelectEvent querySelectEvent) {
                    List<SearchFieldInfo> fieldInfos = querySelectEvent.getSearchFieldInfos();
                    ProjectTicketSearchCriteria criteria = SearchFieldInfo.buildSearchCriteria(ProjectTicketSearchCriteria.class,
                            fieldInfos);
                    criteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                    EventBusFactory.getInstance().post(new TicketEvent.SearchRequest(TicketSearchPanel.this, criteria));
                    EventBusFactory.getInstance().post(new ShellEvent.AddQueryParam(this, fieldInfos));
                }
            });
            ELabel taskIcon = ELabel.h2(ProjectAssetsManager.getAsset(ProjectTypeConstants.TICKET).getHtml()).withWidthUndefined();
            return new MHorizontalLayout(taskIcon, savedFilterComboBox).expand(savedFilterComboBox).alignAll(Alignment.MIDDLE_LEFT);
        } else {
            return null;
        }
    }

    @Override
    public void setTotalCountNumber(Integer countNumber) {
        savedFilterComboBox.setTotalCountNumber(countNumber);
    }

    @Override
    protected SearchLayout<ProjectTicketSearchCriteria> createBasicSearchLayout() {
        return new AssignmentBasicSearchLayout();
    }

    @Override
    protected SearchLayout<ProjectTicketSearchCriteria> createAdvancedSearchLayout() {
        return new AssignmentAdvancedSearchLayout();
    }

    public void setTextField(String name) {
        if (getCompositionRoot() instanceof AssignmentBasicSearchLayout) {
            ((AssignmentBasicSearchLayout) getCompositionRoot()).setNameField(name);
        }
    }

    public void displaySearchFieldInfos(List<SearchFieldInfo> searchFieldInfos) {
        if (canSwitchToAdvanceLayout) {
            AssignmentAdvancedSearchLayout advancedSearchLayout = (AssignmentAdvancedSearchLayout) moveToAdvancedSearchLayout();
            advancedSearchLayout.displaySearchFieldInfos(searchFieldInfos);
        }
    }

    public void selectQueryInfo(String queryId) {
        savedFilterComboBox.selectQueryInfo(queryId);
    }

    private class AssignmentBasicSearchLayout extends BasicSearchLayout<ProjectTicketSearchCriteria> {
        private static final long serialVersionUID = 1L;
        private TextField nameField;
        private CheckBox myItemCheckbox;

        private AssignmentBasicSearchLayout() {
            super(TicketSearchPanel.this);
        }

        public void setNameField(String value) {
            nameField.setValue(value);
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);

            Label nameLbl = new Label(UserUIContext.getMessage(GenericI18Enum.FORM_NAME) + ":");
            basicSearchBody.with(nameLbl).withAlign(nameLbl, Alignment.MIDDLE_LEFT);

            nameField = new MTextField().withInputPrompt(UserUIContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT))
                    .withWidth(WebUIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.with(nameField).withAlign(nameField, Alignment.MIDDLE_CENTER);

            myItemCheckbox = new CheckBox(UserUIContext.getMessage(GenericI18Enum.OPT_MY_ITEMS));
            basicSearchBody.with(myItemCheckbox).withAlign(myItemCheckbox, Alignment.MIDDLE_CENTER);

            MButton searchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent -> callSearchAction())
                    .withIcon(FontAwesome.SEARCH).withStyleName(WebUIConstants.BUTTON_ACTION)
                    .withClickShortcut(ShortcutAction.KeyCode.ENTER);
            basicSearchBody.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_LEFT);

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> nameField.setValue(""))
                    .withStyleName(WebUIConstants.BUTTON_OPTION);
            basicSearchBody.with(cancelBtn).withAlign(cancelBtn, Alignment.MIDDLE_CENTER);

            if (canSwitchToAdvanceLayout) {
                MButton advancedSearchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
                        clickEvent -> moveToAdvancedSearchLayout()).withStyleName(WebUIConstants.BUTTON_LINK);
                basicSearchBody.with(advancedSearchBtn).withAlign(advancedSearchBtn, Alignment.MIDDLE_CENTER);
            }
            return basicSearchBody;
        }

        @Override
        protected ProjectTicketSearchCriteria fillUpSearchCriteria() {
            List<SearchFieldInfo> searchFieldInfos = new ArrayList<>();
            searchFieldInfos.add(new SearchFieldInfo(SearchField.AND, ProjectTicketSearchCriteria.p_name,
                    QueryI18nEnum.StringI18nEnum.CONTAINS.name(),
                    ConstantValueInjector.valueOf(nameField.getValue().trim())));
            if (myItemCheckbox.getValue()) {
                searchFieldInfos.add(new SearchFieldInfo(SearchField.AND, ProjectTicketSearchCriteria.p_assignee,
                        QueryI18nEnum.CollectionI18nEnum.IN.name(),
                        ConstantValueInjector.valueOf(Collections.singletonList(UserUIContext.getUsername()))));
            }
            EventBusFactory.getInstance().post(new ShellEvent.AddQueryParam(this, searchFieldInfos));
            searchCriteria = SearchFieldInfo.buildSearchCriteria(ProjectTicketSearchCriteria.class, searchFieldInfos);
            searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
            return searchCriteria;
        }
    }

    private class AssignmentAdvancedSearchLayout extends DynamicQueryParamLayout<ProjectTicketSearchCriteria> {
        private static final long serialVersionUID = 1L;

        private AssignmentAdvancedSearchLayout() {
            super(TicketSearchPanel.this, ProjectTypeConstants.TICKET);
        }

        @Override
        protected Class<ProjectTicketSearchCriteria> getType() {
            return ProjectTicketSearchCriteria.class;
        }

        @Override
        public Param[] getParamFields() {
            return paramFields;
        }

        @Override
        protected Component buildSelectionComp(String fieldId) {
            if ("assignuser".equals(fieldId) || "createduser".equals(fieldId)) {
                return new ProjectMemberListSelect(false);
            } else if ("milestone".equals(fieldId)) {
                return new MilestoneListSelect();
            }
            return null;
        }

        @Override
        protected ProjectTicketSearchCriteria fillUpSearchCriteria() {
            searchCriteria = super.fillUpSearchCriteria();
            searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
            return searchCriteria;
        }
    }
}
