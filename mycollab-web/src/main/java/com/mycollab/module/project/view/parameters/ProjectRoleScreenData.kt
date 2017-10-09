package com.mycollab.module.project.view.parameters

import com.mycollab.module.project.domain.ProjectRole
import com.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ProjectRoleScreenData {
    class Search(params: ProjectRoleSearchCriteria) : ScreenData<ProjectRoleSearchCriteria>(params)

    class Add(params: ProjectRole) : ScreenData<ProjectRole>(params)

    class Read(params: Int) : ScreenData<Int>(params) 
}