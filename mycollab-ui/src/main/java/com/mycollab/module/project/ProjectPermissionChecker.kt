/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
