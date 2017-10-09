package com.mycollab.module.tracker.domain.criteria;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.StringSearchField;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
public class VersionSearchCriteria extends SearchCriteria {
    private NumberSearchField projectId;

    private NumberSearchField id;

    private StringSearchField versionname;

    private StringSearchField status;

    public NumberSearchField getProjectId() {
        return projectId;
    }

    public void setProjectId(NumberSearchField projectId) {
        this.projectId = projectId;
    }

    public NumberSearchField getId() {
        return id;
    }

    public void setId(NumberSearchField id) {
        this.id = id;
    }

    public StringSearchField getVersionname() {
        return versionname;
    }

    public void setVersionname(StringSearchField versionname) {
        this.versionname = versionname;
    }

    public StringSearchField getStatus() {
        return status;
    }

    public void setStatus(StringSearchField status) {
        this.status = status;
    }
}
