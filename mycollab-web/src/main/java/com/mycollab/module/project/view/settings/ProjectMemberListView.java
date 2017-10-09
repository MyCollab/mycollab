package com.mycollab.module.project.view.settings;

import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.mycollab.vaadin.mvp.PageView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ProjectMemberListView extends PageView {
    void setSearchCriteria(ProjectMemberSearchCriteria searchCriteria);
}
