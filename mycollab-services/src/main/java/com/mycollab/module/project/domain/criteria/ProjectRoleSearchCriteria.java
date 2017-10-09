package com.mycollab.module.project.domain.criteria;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.StringSearchField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectRoleSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    private StringSearchField rolename;
    private NumberSearchField projectId;
    private NumberSearchField id;

    public StringSearchField getRolename() {
        return rolename;
    }

    public void setRolename(StringSearchField rolename) {
        this.rolename = rolename;
    }

    public NumberSearchField getProjectId() {
        return projectId;
    }

    public void setProjectId(NumberSearchField projectId) {
        this.projectId = projectId;
    }

    public void setId(NumberSearchField id) {
        this.id = id;
    }

    public NumberSearchField getId() {
        return id;
    }
}
