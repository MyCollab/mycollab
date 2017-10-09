package com.mycollab.mobile.module.project.view.settings

import com.mycollab.mobile.ui.IListView
import com.mycollab.module.project.domain.SimpleProjectMember
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
interface ProjectMemberListView : IListView<ProjectMemberSearchCriteria, SimpleProjectMember>
