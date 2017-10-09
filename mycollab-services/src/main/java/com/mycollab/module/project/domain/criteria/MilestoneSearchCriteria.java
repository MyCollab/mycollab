package com.mycollab.module.project.domain.criteria;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MilestoneSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    private StringSearchField assignUser;
    private SetSearchField<String> statuses;
    private SetSearchField<Integer> projectIds;
    private NumberSearchField id;
    private StringSearchField milestoneName;

    public StringSearchField getAssignUser() {
        return assignUser;
    }

    public void setAssignUser(StringSearchField assignUser) {
        this.assignUser = assignUser;
    }

    public SetSearchField<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(SetSearchField<String> statuses) {
        this.statuses = statuses;
    }

    public void setId(NumberSearchField id) {
        this.id = id;
    }

    public NumberSearchField getId() {
        return id;
    }

    public void setMilestoneName(StringSearchField milestoneName) {
        this.milestoneName = milestoneName;
    }

    public StringSearchField getMilestoneName() {
        return milestoneName;
    }

    public SetSearchField<Integer> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(SetSearchField<Integer> projectIds) {
        this.projectIds = projectIds;
    }
}
