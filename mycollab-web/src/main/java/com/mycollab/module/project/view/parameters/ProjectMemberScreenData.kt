package com.mycollab.module.project.view.parameters

import com.mycollab.module.project.domain.ProjectMember
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ProjectMemberScreenData {
    class Search(params: ProjectMemberSearchCriteria) : ScreenData<ProjectMemberSearchCriteria>(params)

    class Add(params: ProjectMember) : ScreenData<ProjectMember>(params)

    class InviteProjectMembers : ScreenData<Any>(null)

    class Read(params: Any?) : ScreenData<Any>(params)
}