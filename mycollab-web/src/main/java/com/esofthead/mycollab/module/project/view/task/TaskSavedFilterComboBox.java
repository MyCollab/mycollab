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
package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.common.domain.OptionVal;
import com.esofthead.mycollab.common.service.OptionValService;
import com.esofthead.mycollab.core.db.query.SearchFieldInfo;
import com.esofthead.mycollab.core.db.query.SearchQueryInfo;
import com.esofthead.mycollab.core.db.query.VariableInjecter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.query.CurrentProjectIdInjecter;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.SavedFilterComboBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.1
 */
public class TaskSavedFilterComboBox extends SavedFilterComboBox {
    public TaskSavedFilterComboBox() {
        super(ProjectTypeConstants.TASK);

        SearchQueryInfo allTasksQuery = new SearchQueryInfo("All Tasks", SearchFieldInfo.inCollection
                (TaskSearchCriteria.p_projectIds, new CurrentProjectIdInjecter()));

        SearchQueryInfo allOpenTaskQuery = new SearchQueryInfo("All Open Task", SearchFieldInfo.inCollection(
                TaskSearchCriteria.p_status, new VariableInjecter() {
                    @Override
                    public Object eval() {
                        OptionValService optionValService = ApplicationContextUtil.getSpringBean(OptionValService.class);
                        List<OptionVal> options = optionValService.findOptionValsExcludeClosed(ProjectTypeConstants.TASK,
                                CurrentProjectVariables.getProjectId(), AppContext.getAccountId());
                        List<String> statuses = new ArrayList<>();
                        for (OptionVal option : options) {
                            statuses.add(option.getTypeval());
                        }
                        return statuses;
                    }
                }));

        SearchQueryInfo myTasksQuery = new SearchQueryInfo("My Tasks", SearchFieldInfo.inCollection
                (TaskSearchCriteria.p_assignee, new VariableInjecter() {
                    @Override
                    public Object eval() {
                        return Arrays.asList(AppContext.getUsername());
                    }
                }));

        SearchQueryInfo newTasksThisWeekQuery = new SearchQueryInfo("New This Week", SearchFieldInfo.inDateRange
                (TaskSearchCriteria.p_createtime, VariableInjecter.THIS_WEEK));

        SearchQueryInfo updateTasksThisWeekQuery = new SearchQueryInfo("Update This Week", SearchFieldInfo.inDateRange
                (TaskSearchCriteria.p_lastupdatedtime, VariableInjecter.THIS_WEEK));

        SearchQueryInfo newTasksLastWeekQuery = new SearchQueryInfo("New Last Week", SearchFieldInfo.inDateRange
                (TaskSearchCriteria.p_createtime, VariableInjecter.LAST_WEEK));

        SearchQueryInfo updateTasksLastWeekQuery = new SearchQueryInfo("Update Last Week", SearchFieldInfo.inDateRange
                (TaskSearchCriteria.p_lastupdatedtime, VariableInjecter.LAST_WEEK));

        this.addSharedSearchQueryInfo(allTasksQuery);
        this.addSharedSearchQueryInfo(allOpenTaskQuery);
        this.addSharedSearchQueryInfo(myTasksQuery);
        this.addSharedSearchQueryInfo(newTasksThisWeekQuery);
        this.addSharedSearchQueryInfo(updateTasksThisWeekQuery);
        this.addSharedSearchQueryInfo(newTasksLastWeekQuery);
        this.addSharedSearchQueryInfo(updateTasksLastWeekQuery);
    }
}
