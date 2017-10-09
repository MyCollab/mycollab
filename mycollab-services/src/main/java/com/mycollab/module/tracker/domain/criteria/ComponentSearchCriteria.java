package com.mycollab.module.tracker.domain.criteria;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.StringSearchField;

public class ComponentSearchCriteria extends SearchCriteria {

    private NumberSearchField projectId;
    private StringSearchField componentName;
    private NumberSearchField id;
    private StringSearchField status;
    private StringSearchField userlead;

    public StringSearchField getUserlead() {
		return userlead;
	}

	public void setUserlead(StringSearchField userlead) {
		this.userlead = userlead;
	}

    public NumberSearchField getProjectId() {
        return projectId;
    }

    public void setProjectId(NumberSearchField projectId) {
        this.projectId = projectId;
    }

    public StringSearchField getComponentName() {
        return componentName;
    }

    public void setComponentName(StringSearchField componentName) {
        this.componentName = componentName;
    }

    public NumberSearchField getId() {
        return id;
    }

    public void setId(NumberSearchField id) {
        this.id = id;
    }

	public StringSearchField getStatus() {
		return status;
	}

	public void setStatus(StringSearchField status) {
		this.status = status;
	}
}
