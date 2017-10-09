package com.mycollab.mobile.module.project.view

import com.mycollab.mobile.ui.IListView
import com.mycollab.module.project.domain.SimpleProject
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
interface UserProjectListView : IListView<ProjectSearchCriteria, SimpleProject>
