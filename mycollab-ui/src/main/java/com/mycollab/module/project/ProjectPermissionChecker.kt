package com.mycollab.module.project

import com.mycollab.module.project.domain.SimpleProjectMember
import com.mycollab.module.project.service.ProjectMemberService
import com.mycollab.security.PermissionMap
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.UserUIContext

/**
 * @author MyCollab Ltd
 * @since 5.2.8
 */
object ProjectPermissionChecker {
    private val memberService: ProjectMemberService
        get() = AppContextUtil.getSpringBean(ProjectMemberService::class.java)

    @JvmStatic fun canWrite(prjId: Int, permissionItem: String): Boolean {
        val member = memberService.findMemberByUsername(UserUIContext.getUsername(), prjId, AppUI.accountId)
        return if (member != null) {
            when {
                member.isProjectOwner -> true
                else -> {
                    val permissionMap = member.permissionMaps
                    permissionMap != null && permissionMap.canWrite(permissionItem)
                }
            }
        } else false
    }
}
