package com.mycollab.module.project.view.settings

import com.mycollab.module.project.domain.SimpleProjectRole
import com.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria
import com.mycollab.vaadin.web.ui.IListView

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface ProjectRoleListView : IListView<ProjectRoleSearchCriteria, SimpleProjectRole>
