package com.mycollab.common.domain.criteria;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;

/**
 * @author MyCollab Ltd
 * @since 5.2.9
 */
public class TagSearchCriteria extends SearchCriteria {
    private NumberSearchField projectId;

    public NumberSearchField getProjectId() {
        return projectId;
    }

    public void setProjectId(NumberSearchField projectId) {
        this.projectId = projectId;
    }
}
