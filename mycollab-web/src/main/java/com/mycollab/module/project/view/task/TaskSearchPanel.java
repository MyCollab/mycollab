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
package com.mycollab.module.project.view.task;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.QueryI18nEnum.CollectionI18nEnum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.query.ConstantValueInjector;
import com.mycollab.db.query.Param;
import com.mycollab.db.query.SearchFieldInfo;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.mycollab.module.project.event.TaskEvent;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.milestone.MilestoneListSelect;
import com.mycollab.module.project.view.settings.component.ProjectMemberListSelect;
import com.mycollab.shell.event.ShellEvent;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.mycollab.common.i18n.QueryI18nEnum.StringI18nEnum;

/**
 * @author MyCollab Ltd.
 * @since 4.0.0
 */
public class TaskSearchPanel extends DefaultGenericSearchPanel<TaskSearchCriteria> {
    private static final long serialVersionUID = 1L;
    private TaskSearchCriteria searchCriteria;

    private TaskSavedFilterComboBox savedFilterComboBox;

    private static Param[] paramFields = new Param[]{
            TaskSearchCriteria.p_taskname,
            TaskSearchCriteria.p_assignee, TaskSearchCriteria.p_createtime,
            TaskSearchCriteria.p_duedate, TaskSearchCriteria.p_lastupdatedtime,
            TaskSearchCriteria.p_status, TaskSearchCriteria.p_startdate, TaskSearchCriteria.p_enddate,
            TaskSearchCriteria.p_milestoneId, TaskSearchCriteria.p_taskkey};

    public TaskSearchPanel(boolean canSwitchToAdvanceLayout) {
        super(canSwitchToAdvanceLayout);
    }

    public TaskSearchPanel() {
        super();
    }

    @Override
    protected ComponentContainer buildSearchTitle() {
        if (canSwitchToAdvanceLayout) {
            savedFilterComboBox = new TaskSavedFilterComboBox();
            savedFilterComboBox.addQuerySelectListener(new SavedFilterComboBox.QuerySelectListener() {
                @Override
                public void querySelect(SavedFilterComboBox.QuerySelectEvent querySelectEvent) {
                    List<SearchFieldInfo<TaskSearchCriteria>> fieldInfos = querySelectEvent.getSearchFieldInfos();
                    TaskSearchCriteria criteria = SearchFieldInfo.buildSearchCriteria(TaskSearchCriteria.class, fieldInfos);
                    criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                    EventBusFactory.getInstance().post(new TaskEvent.SearchRequest(TaskSearchPanel.this, criteria));
                    EventBusFactory.getInstance().post(new ShellEvent.AddQueryParam(this, fieldInfos));
                }
            });
            ELabel taskIcon = ELabel.h2(ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK).getHtml()).withUndefinedWidth();
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
    protected SearchLayout<TaskSearchCriteria> createBasicSearchLayout() {
        return new TaskBasicSearchLayout();
    }

    @Override
    protected SearchLayout<TaskSearchCriteria> createAdvancedSearchLayout() {
        return new TaskAdvancedSearchLayout();
    }

    public void setTextField(String name) {
        if (getCompositionRoot() instanceof TaskBasicSearchLayout) {
            ((TaskBasicSearchLayout) getCompositionRoot()).setNameField(name);
        }
    }

    public void displaySearchFieldInfos(List<SearchFieldInfo<TaskSearchCriteria>> searchFieldInfos) {
        if (canSwitchToAdvanceLayout) {
            TaskAdvancedSearchLayout advancedSearchLayout = (TaskAdvancedSearchLayout) moveToAdvancedSearchLayout();
            advancedSearchLayout.displaySearchFieldInfos(searchFieldInfos);
        }
    }

    public void selectQueryInfo(String queryId) {
        savedFilterComboBox.selectQueryInfo(queryId);
    }

    private class TaskBasicSearchLayout extends BasicSearchLayout<TaskSearchCriteria> {
        private static final long serialVersionUID = 1L;
        private TextField nameField;
        private CheckBox myItemCheckbox;

        private TaskBasicSearchLayout() {
            super(TaskSearchPanel.this);
        }

        public void setNameField(String value) {
            nameField.setValue(value);
        }

        @Override
        public ComponentContainer constructBody() {
            MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);

            Label nameLbl = new Label(UserUIContext.getMessage(GenericI18Enum.FORM_NAME) + ":");
            basicSearchBody.with(nameLbl).withAlign(nameLbl, Alignment.MIDDLE_LEFT);

            nameField = new MTextField().withPlaceholder(UserUIContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT))
                    .withWidth(WebUIConstants.DEFAULT_CONTROL_WIDTH);
            basicSearchBody.with(nameField).withAlign(nameField, Alignment.MIDDLE_CENTER);

            myItemCheckbox = new CheckBox(UserUIContext.getMessage(GenericI18Enum.OPT_MY_ITEMS));
            basicSearchBody.with(myItemCheckbox).withAlign(myItemCheckbox, Alignment.MIDDLE_CENTER);

            MButton searchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent -> callSearchAction())
                    .withIcon(VaadinIcons.SEARCH).withStyleName(WebThemes.BUTTON_ACTION)
                    .withClickShortcut(ShortcutAction.KeyCode.ENTER);
            basicSearchBody.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_LEFT);

            MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> nameField.setValue(""))
                    .withStyleName(WebThemes.BUTTON_OPTION);
            basicSearchBody.with(cancelBtn).withAlign(cancelBtn, Alignment.MIDDLE_CENTER);

            if (canSwitchToAdvanceLayout) {
                MButton advancedSearchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
                        clickEvent -> moveToAdvancedSearchLayout()).withStyleName(WebThemes.BUTTON_LINK);
                basicSearchBody.with(advancedSearchBtn).withAlign(advancedSearchBtn, Alignment.MIDDLE_CENTER);
            }
            return basicSearchBody;
        }

        @Override
        protected TaskSearchCriteria fillUpSearchCriteria() {
            List<SearchFieldInfo<TaskSearchCriteria>> searchFieldInfos = new ArrayList<>();
            searchFieldInfos.add(new SearchFieldInfo(SearchField.AND, TaskSearchCriteria.p_taskname, StringI18nEnum.CONTAINS.name(),
                    ConstantValueInjector.valueOf(nameField.getValue().trim())));
            if (myItemCheckbox.getValue()) {
                searchFieldInfos.add(new SearchFieldInfo(SearchField.AND, TaskSearchCriteria.p_assignee, CollectionI18nEnum.IN.name(),
                        ConstantValueInjector.valueOf(Collections.singletonList(UserUIContext.getUsername()))));
            }
            EventBusFactory.getInstance().post(new ShellEvent.AddQueryParam(this, searchFieldInfos));
            searchCriteria = SearchFieldInfo.buildSearchCriteria(TaskSearchCriteria.class, searchFieldInfos);
            searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            return searchCriteria;
        }
    }

    private class TaskAdvancedSearchLayout extends DynamicQueryParamLayout<TaskSearchCriteria> {
        private static final long serialVersionUID = 1L;

        private TaskAdvancedSearchLayout() {
            super(TaskSearchPanel.this, ProjectTypeConstants.TASK);
        }

        @Override
        protected Class<TaskSearchCriteria> getType() {
            return TaskSearchCriteria.class;
        }

        @Override
        public Param[] getParamFields() {
            return paramFields;
        }

        @Override
        protected Component buildSelectionComp(String fieldId) {
            if ("assignuser".equals(fieldId)) {
                return new ProjectMemberListSelect(false, Arrays.asList(CurrentProjectVariables.getProjectId()));
            } else if ("milestone".equals(fieldId)) {
                return new MilestoneListSelect();
            } else if ("status".equals(fieldId)) {
                return new TaskStatusListSelect();
            }
            return null;
        }

        @Override
        protected TaskSearchCriteria fillUpSearchCriteria() {
            searchCriteria = super.fillUpSearchCriteria();
            searchCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            return searchCriteria;
        }
    }
}