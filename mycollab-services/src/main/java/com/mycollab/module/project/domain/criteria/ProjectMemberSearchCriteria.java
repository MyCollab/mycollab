package com.mycollab.module.project.domain.criteria;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectMemberSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    private NumberSearchField projectId;

    private NumberSearchField id;

    private SetSearchField<String> statuses;

    private StringSearchField involvedMember;

    private StringSearchField memberFullName;

    public NumberSearchField getId() {
        return id;
    }

    public void setId(NumberSearchField id) {
        this.id = id;
    }

    public NumberSearchField getProjectId() {
        return projectId;
    }

    public void setProjectId(NumberSearchField projectId) {
        this.projectId = projectId;
    }

    public SetSearchField<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(SetSearchField<String> statuses) {
        this.statuses = statuses;
    }

    public StringSearchField getInvolvedMember() {
        return involvedMember;
    }

    public void setInvolvedMember(StringSearchField involvedMember) {
        this.involvedMember = involvedMember;
    }

    public StringSearchField getMemberFullName() {
        return memberFullName;
    }

    public void setMemberFullName(StringSearchField memberFullName) {
        this.memberFullName = memberFullName;
    }
}
