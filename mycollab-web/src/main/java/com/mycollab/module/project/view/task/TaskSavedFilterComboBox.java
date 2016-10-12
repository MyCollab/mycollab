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
package com.mycollab.module.project.view.task;

import com.mycollab.common.domain.OptionVal;
import com.mycollab.common.i18n.OptionI18nEnum;
import com.mycollab.common.i18n.QueryI18nEnum;
import com.mycollab.common.service.OptionValService;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.query.*;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.query.CurrentProjectIdInjector;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.SavedFilterComboBox;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.1
 */
public class TaskSavedFilterComboBox extends SavedFilterComboBox {
    public static final String ALL_TASKS = "ALL_TICKETS";
    public static final String OPEN_TASKS = "OPEN_TICKETS";
    public static final String OVERDUE_TASKS = "OVERDUE_TICKETS";
    public static final String MY_TASKS = "MY_TICKETS";
    public static final String TASKS_CREATED_BY_ME = "TICKETS_CREATED_BY_ME";
    public static final String NEW_TASKS_THIS_WEEK = "NEW_TICKETS_THIS_WEEK";
    public static final String UPDATE_TASKS_THIS_WEEK = "UPDATE_TICKETS_THIS_WEEK";
    public static final String NEW_TASKS_LAST_WEEK = "NEW_TICKETS_LAST_WEEK";
    public static final String UPDATE_TASKS_LAST_WEEK = "UPDATE_TICKETS_LAST_WEEK";

    public TaskSavedFilterComboBox() {
        super(ProjectTypeConstants.TASK);

        SearchQueryInfo allTasksQuery = new SearchQueryInfo(ALL_TASKS, UserUIContext.getMessage(TaskI18nEnum.VAL_ALL_TASKS),
                SearchFieldInfo.inCollection(TaskSearchCriteria.p_projectIds, new CurrentProjectIdInjector()));

        SearchQueryInfo allOpenTaskQuery = new SearchQueryInfo(OPEN_TASKS, UserUIContext.getMessage(TaskI18nEnum.VAL_ALL_OPEN_TASKS),
                SearchFieldInfo.inCollection(TaskSearchCriteria.p_status, new VariableInjector() {
                    @Override
                    public Object eval() {
                        OptionValService optionValService = AppContextUtil.getSpringBean(OptionValService.class);
                        List<OptionVal> options = optionValService.findOptionValsExcludeClosed(ProjectTypeConstants.TASK,
                                CurrentProjectVariables.getProjectId(), MyCollabUI.getAccountId());
                        List<String> statuses = new ArrayList<>();
                        for (OptionVal option : options) {
                            statuses.add(option.getTypeval());
                        }
                        return statuses;
                    }

                    @Override
                    public Class getType() {
                        return String.class;
                    }

                    @Override
                    public boolean isArray() {
                        return false;
                    }

                    @Override
                    public boolean isCollection() {
                        return true;
                    }
                }));

        SearchQueryInfo overdueTaskQuery = new SearchQueryInfo(OVERDUE_TASKS, UserUIContext.getMessage(TaskI18nEnum.VAL_OVERDUE_TASKS),
                new SearchFieldInfo(SearchField.AND, TaskSearchCriteria.p_duedate, DateParam.BEFORE, new LazyValueInjector() {
                    @Override
                    protected Object doEval() {
                        return new LocalDate().toDate();
                    }
                }), new SearchFieldInfo(SearchField.AND, new StringParam("id-status", "m_prj_task", "status"), QueryI18nEnum.StringI18nEnum.IS_NOT.name(),
                ConstantValueInjector.valueOf(OptionI18nEnum.StatusI18nEnum.Closed.name())));

        SearchQueryInfo myTasksQuery = new SearchQueryInfo(MY_TASKS, UserUIContext.getMessage(TaskI18nEnum.VAL_MY_TASKS),
                SearchFieldInfo.inCollection(TaskSearchCriteria.p_assignee, ConstantValueInjector.valueOf(Collections.singletonList(UserUIContext.getUsername()))));

        SearchQueryInfo tasksCreatedByMeQuery = new SearchQueryInfo(TASKS_CREATED_BY_ME, UserUIContext.getMessage(TaskI18nEnum.VAL_TASKS_CREATED_BY_ME),
                SearchFieldInfo.inCollection(TaskSearchCriteria.p_createdUser, ConstantValueInjector.valueOf(Collections.singletonList(UserUIContext.getUsername()))));

        SearchQueryInfo newTasksThisWeekQuery = new SearchQueryInfo(NEW_TASKS_THIS_WEEK, UserUIContext.getMessage(TaskI18nEnum.VAL_NEW_THIS_WEEK),
                SearchFieldInfo.inDateRange(TaskSearchCriteria.p_createtime, VariableInjector.THIS_WEEK));

        SearchQueryInfo updateTasksThisWeekQuery = new SearchQueryInfo(UPDATE_TASKS_THIS_WEEK, UserUIContext.getMessage(TaskI18nEnum.VAL_UPDATE_THIS_WEEK),
                SearchFieldInfo.inDateRange(TaskSearchCriteria.p_lastupdatedtime, VariableInjector.THIS_WEEK));

        SearchQueryInfo newTasksLastWeekQuery = new SearchQueryInfo(NEW_TASKS_LAST_WEEK, UserUIContext.getMessage(TaskI18nEnum.VAL_NEW_LAST_WEEK),
                SearchFieldInfo.inDateRange(TaskSearchCriteria.p_createtime, VariableInjector.LAST_WEEK));

        SearchQueryInfo updateTasksLastWeekQuery = new SearchQueryInfo(UPDATE_TASKS_LAST_WEEK, UserUIContext.getMessage(TaskI18nEnum.VAL_UPDATE_LAST_WEEK),
                SearchFieldInfo.inDateRange(TaskSearchCriteria.p_lastupdatedtime, VariableInjector.LAST_WEEK));

        this.addSharedSearchQueryInfo(allTasksQuery);
        this.addSharedSearchQueryInfo(allOpenTaskQuery);
        this.addSharedSearchQueryInfo(overdueTaskQuery);
        this.addSharedSearchQueryInfo(myTasksQuery);
        this.addSharedSearchQueryInfo(tasksCreatedByMeQuery);
        this.addSharedSearchQueryInfo(newTasksThisWeekQuery);
        this.addSharedSearchQueryInfo(updateTasksThisWeekQuery);
        this.addSharedSearchQueryInfo(newTasksLastWeekQuery);
        this.addSharedSearchQueryInfo(updateTasksLastWeekQuery);
    }

    public void setTotalCountNumber(int countNumber) {
        componentsText.setReadOnly(false);
        componentsText.setValue(selectedQueryName + " (" + countNumber + ")");
        componentsText.setReadOnly(true);
    }
}
