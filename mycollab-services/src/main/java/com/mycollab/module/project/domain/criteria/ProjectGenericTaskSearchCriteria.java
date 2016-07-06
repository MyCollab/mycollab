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

import com.mycollab.db.arguments.*;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectGenericTaskSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    private SetSearchField<Integer> projectIds;

    private StringSearchField assignUser;

    private SearchField isOpenned;

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
}
