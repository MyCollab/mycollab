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
package com.mycollab.module.crm.domain.criteria;

import com.mycollab.db.arguments.*;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ActivitySearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    private StringSearchField subject;
    private StringSearchField assignUser;
    private DateTimeSearchField startDate;
    private DateTimeSearchField endDate;
    private StringSearchField type;
    private NumberSearchField typeid;
    private BooleanSearchField isClosed;

    public StringSearchField getSubject() {
        return subject;
    }

    public void setSubject(StringSearchField subject) {
        this.subject = subject;
    }

    public StringSearchField getAssignUser() {
        return assignUser;
    }

    public void setAssignUser(StringSearchField assignUser) {
        this.assignUser = assignUser;
    }

    public DateTimeSearchField getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTimeSearchField startDate) {
        this.startDate = startDate;
    }

    public DateTimeSearchField getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTimeSearchField endDate) {
        this.endDate = endDate;
    }

    public StringSearchField getType() {
        return type;
    }

    public void setType(StringSearchField type) {
        this.type = type;
    }

    public NumberSearchField getTypeid() {
        return typeid;
    }

    public void setTypeid(NumberSearchField typeid) {
        this.typeid = typeid;
    }

    public BooleanSearchField getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(BooleanSearchField isClosed) {
        this.isClosed = isClosed;
    }
}
