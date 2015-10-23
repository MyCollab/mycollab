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
package com.esofthead.mycollab.module.project.domain.criteria;

import com.esofthead.mycollab.core.arguments.DateSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.DateParam;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class StandupReportSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    private NumberSearchField projectId;
    private StringSearchField logBy;
    private DateSearchField onDate;

    public static final DateParam p_fordays = new DateParam("standup-forday", null, "m_prj_standup", "forday");

    public NumberSearchField getProjectId() {
        return projectId;
    }

    public void setProjectId(NumberSearchField projectId) {
        this.projectId = projectId;
    }

    public StringSearchField getLogBy() {
        return logBy;
    }

    public void setLogBy(StringSearchField logBy) {
        this.logBy = logBy;
    }

    public DateSearchField getOnDate() {
        return onDate;
    }

    public void setOnDate(DateSearchField onDate) {
        this.onDate = onDate;
    }
}
