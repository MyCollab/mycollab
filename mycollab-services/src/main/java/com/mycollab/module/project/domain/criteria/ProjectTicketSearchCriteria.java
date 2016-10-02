/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.domain.criteria;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.*;
import com.mycollab.db.query.*;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectTicketSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    public static final Param p_type = CacheParamMapper.register(ProjectTypeConstants.TICKET,
            GenericI18Enum.FORM_TYPE, new SearchCriteriaBridgeParam<ProjectTicketSearchCriteria>("type") {
                @Override
                public ProjectTicketSearchCriteria injectCriteriaInList(ProjectTicketSearchCriteria searchCriteria, String oper, Collection<?> value) {
                    searchCriteria.setTypes(new SetSearchField(oper, value));
                    return searchCriteria;
                }

                @Override
                public ProjectTicketSearchCriteria injectCriteriaNotInList(ProjectTicketSearchCriteria searchCriteria, String oper, Collection<?> value) {
                    searchCriteria.setTypes(new SetSearchField(oper, value));
                    return searchCriteria;
                }
            });

    public static final Param p_name = CacheParamMapper.register(ProjectTypeConstants.TICKET,
            GenericI18Enum.FORM_NAME, new StringParam("name", "mainTbl", "name"));

    public static final I18nStringListParam p_priority = CacheParamMapper.register(ProjectTypeConstants.TICKET, GenericI18Enum.FORM_PRIORITY,
            new I18nStringListParam("priority", "mainTbl", "priority",
                    Arrays.asList(Priority.Urgent, Priority.High, Priority.Medium, Priority.Low, Priority.None)));

    public static final PropertyListParam<Integer> p_milestones = CacheParamMapper.register(ProjectTypeConstants.TICKET, MilestoneI18nEnum.SINGLE,
            new PropertyListParam<Integer>("milestone", "mainTbl", "milestoneId"));

    public static final PropertyListParam<String> p_assignee = CacheParamMapper.register(ProjectTypeConstants.TICKET, GenericI18Enum.FORM_ASSIGNEE,
            new PropertyListParam<String>("assignuser", "mainTbl", "assignUser"));

    public static final PropertyListParam<String> p_createdUser = CacheParamMapper.register(ProjectTypeConstants.TICKET,
            GenericI18Enum.OPT_CREATED_BY, new PropertyListParam<String>("createduser", "mainTbl", "createdUser"));

    public static final DateParam p_startDate = CacheParamMapper.register(ProjectTypeConstants.TICKET,
            GenericI18Enum.FORM_START_DATE, new DateParam("startdate", "mainTbl", "startDate"));

    public static final DateParam p_endDate = CacheParamMapper.register(ProjectTypeConstants.TICKET,
            GenericI18Enum.FORM_END_DATE, new DateParam("enddate", "mainTbl", "endDate"));

    public static final DateParam p_dueDate = CacheParamMapper.register(ProjectTypeConstants.TICKET,
            GenericI18Enum.FORM_DUE_DATE, new DateParam("duedate", "mainTbl", "duedate"));

    public static final DateParam p_createtime = CacheParamMapper.register(ProjectTypeConstants.TICKET, GenericI18Enum.FORM_CREATED_TIME,
            new DateParam("createtime", "mainTbl", "createdTime"));

    public static final DateParam p_lastupdatedtime = CacheParamMapper.register(ProjectTypeConstants.TICKET, GenericI18Enum.FORM_LAST_UPDATED_TIME,
            new DateParam("lastupdatedtime", "mainTbl", "lastUpdatedTime"));

    public static final I18nStringListParam p_status = CacheParamMapper.register(ProjectTypeConstants.TICKET,
            GenericI18Enum.FORM_STATUS, new I18nStringListParam("status", "mainTbl", "status", null));

    public static final PropertyListParam p_projectIds = CacheParamMapper.register(ProjectTypeConstants.TICKET, null,
            new PropertyListParam("projectid", "mainTbl", "projectId"));

    private SetSearchField<Integer> projectIds;

    private StringSearchField assignUser;

    private SearchField isOpenned;

    private SearchField unAssignee;

    private StringSearchField name;

    private DateSearchField dueDate;

    private RangeDateSearchField dateInRange;

    private NumberSearchField milestoneId;

    private SetSearchField<String> types;

    private SetSearchField<Integer> typeIds;

    public DateSearchField getDueDate() {
        return dueDate;
    }

    public void setDueDate(DateSearchField dueDate) {
        this.dueDate = dueDate;
    }

    public StringSearchField getAssignUser() {
        return assignUser;
    }

    public void setAssignUser(StringSearchField assignUser) {
        this.assignUser = assignUser;
    }

    public SearchField getIsOpenned() {
        return isOpenned;
    }

    public void setIsOpenned(SearchField isOpenned) {
        this.isOpenned = isOpenned;
    }

    public StringSearchField getName() {
        return name;
    }

    public void setName(StringSearchField name) {
        this.name = name;
    }

    public SetSearchField<Integer> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(SetSearchField<Integer> projectIds) {
        this.projectIds = projectIds;
    }

    public NumberSearchField getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(NumberSearchField milestoneId) {
        this.milestoneId = milestoneId;
    }

    public SetSearchField<String> getTypes() {
        return types;
    }

    public void setTypes(SetSearchField<String> types) {
        this.types = types;
    }

    public RangeDateSearchField getDateInRange() {
        return dateInRange;
    }

    public void setDateInRange(RangeDateSearchField dateInRange) {
        this.dateInRange = dateInRange;
    }

    public SetSearchField<Integer> getTypeIds() {
        return typeIds;
    }

    public void setTypeIds(SetSearchField<Integer> typeIds) {
        this.typeIds = typeIds;
    }

    public SearchField getUnAssignee() {
        return unAssignee;
    }

    public void setUnAssignee(SearchField unAssignee) {
        this.unAssignee = unAssignee;
    }
}
