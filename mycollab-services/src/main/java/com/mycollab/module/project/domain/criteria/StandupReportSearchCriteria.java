package com.mycollab.module.project.domain.criteria;

import com.mycollab.db.arguments.DateSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class StandupReportSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    private SetSearchField<Integer> projectIds;
    private StringSearchField logBy;
    private DateSearchField onDate;

    public SetSearchField<Integer> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(SetSearchField<Integer> projectIds) {
        this.projectIds = projectIds;
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
