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

import com.google.common.base.MoreObjects
import com.google.common.eventbus.AsyncEventBus
import com.mycollab.core.SecureAccessException
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.module.file.PathUtils
import com.mycollab.module.project.dao.ProjectRolePermissionMapper
import com.mycollab.module.project.domain.*
import com.mycollab.module.project.esb.NewProjectMemberJoinEvent
import com.mycollab.module.project.service.ProjectMemberService
import com.mycollab.security.PermissionMap
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.UserUIContext
import com.mycollab.vaadin.ui.MyCollabSession
import com.mycollab.vaadin.ui.MyCollabSession.CURRENT_PROJECT
import com.mycollab.vaadin.ui.MyCollabSession.PROJECT_MEMBER
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object CurrentProjectVariables {
    private val LOG = LoggerFactory.getLogger(CurrentProjectVariables::class.java)

    private const val CURRENT_PAGE_VAR = "project_page"

    // get member permission
    @JvmStatic
    var project: SimpleProject?
        get() = MyCollabSession.getCurrentUIVariable(CURRENT_PROJECT) as? SimpleProject
        set(project) {
            MyCollabSession.putCurrentUIVariable(CURRENT_PROJECT, project)
            val prjMemberService = AppContextUtil.getSpringBean(ProjectMemberService::class.java)
            val prjMember = prjMemberService.findMemberByUsername(UserUIContext.getUsername(), project!!.id, AppUI.accountId)
            if (prjMember != null) {
                if (ProjectMemberStatusConstants.INACTIVE == prjMember.status) {
                    throw UserNotBelongProjectException("You are not belong to this project")
                }

                if (prjMember.projectroleid == null) {
                    throw UserNotBelongProjectException("You are not belong to this project")
                }
                val ex = ProjectRolePermissionExample()
                ex.createCriteria().andRoleidEqualTo(prjMember.projectroleid).andProjectidEqualTo(CurrentProjectVariables.projectId)
                val rolePermissionMapper = AppContextUtil.getSpringBean(ProjectRolePermissionMapper::class.java)
                val rolePermissions = rolePermissionMapper.selectByExampleWithBLOBs(ex)
                if (!rolePermissions.isEmpty()) {
                    val rolePer = rolePermissions[0]
                    val permissionMap = PermissionMap.fromJsonString(rolePer.roleval)
                    prjMember.permissionMap = permissionMap
                }

                if (ProjectMemberStatusConstants.NOT_ACCESS_YET == prjMember.status) {
                    prjMember.status = ProjectMemberStatusConstants.ACTIVE
                    prjMemberService.updateSelectiveWithSession(prjMember, UserUIContext.getUsername())
                    val asyncEventBus = AppContextUtil.getSpringBean(AsyncEventBus::class.java)
                    asyncEventBus.post(NewProjectMemberJoinEvent(prjMember.username, prjMember.projectid, AppUI.accountId))
                }
                projectMember = prjMember
            } else if (!UserUIContext.isAdmin()) {
                throw SecureAccessException("You are not belong to this project")
            }
        }

    private var projectMember: SimpleProjectMember?
        get() = MyCollabSession.getCurrentUIVariable(PROJECT_MEMBER) as? SimpleProjectMember
        set(prjMember) = MyCollabSession.putCurrentUIVariable(PROJECT_MEMBER, prjMember)

    @JvmStatic
    val isSuperUser: Boolean
        get() {
            if (UserUIContext.isAdmin()) {
                return true
            }
            return false
        }

    @JvmStatic
    val isProjectArchived: Boolean
        get() = project!!.isProjectArchived

    @JvmStatic
    fun canRead(permissionItem: String): Boolean {
        if (isSuperUser) {
            return true
        }

        return try {
            projectMember!!.canRead(permissionItem)
        } catch (e: Exception) {
            LOG.error("Error while checking permission", e)
            false
        }

    }

    @JvmStatic
    fun canReadAssignments(): Boolean =
            canRead(ProjectRolePermissionCollections.BUGS) || canRead(ProjectRolePermissionCollections.TASKS) ||
                    canRead(ProjectRolePermissionCollections.RISKS) || canRead(ProjectRolePermissionCollections.MILESTONES)

    @JvmStatic
    fun canWrite(permissionItem: String): Boolean {
        if (isProjectArchived) {
            return false
        }

        if (isSuperUser) {
            return true
        }

        return try {
            projectMember!!.canWrite(permissionItem)
        } catch (e: Exception) {
            LOG.error("Error while checking permission", e)
            false
        }

    }

    @JvmStatic
    fun canAccess(permissionItem: String): Boolean {
        if (isProjectArchived) {
            return false
        }

        if (isSuperUser) {
            return true
        }

        return try {
            projectMember!!.canAccess(permissionItem)
        } catch (e: Exception) {
            LOG.error("Error while checking permission", e)
            false
        }

    }

    @JvmStatic
    val features: ProjectCustomizeView
        get() {
            var customizeView: ProjectCustomizeView? = project!!.customizeView
            if (customizeView == null) {
                customizeView = ProjectCustomizeView()
                customizeView.projectid = CurrentProjectVariables.projectId
                customizeView.displayticket = true
                customizeView.displaymessage = true
                customizeView.displaymilestone = true
                customizeView.displaypage = true
                customizeView.displaystandup = true
                customizeView.displaytimelogging = true
                customizeView.displayinvoice = true
            }
            return customizeView
        }

    @JvmStatic
    fun hasMessageFeature(): Boolean = features.displaymessage!!

    @JvmStatic
    fun hasPhaseFeature(): Boolean = features.displaymilestone!!

    @JvmStatic
    fun hasTicketFeature(): Boolean = MoreObjects.firstNonNull(features.displayticket, true)

    @JvmStatic
    fun hasPageFeature(): Boolean = features.displaypage!!

    @JvmStatic
    fun hasTimeFeature(): Boolean = features.displaytimelogging!!

    @JvmStatic
    fun hasInvoiceFeature(): Boolean = java.lang.Boolean.TRUE == features.displayinvoice

    @JvmStatic
    fun hasStandupFeature(): Boolean = features.displaystandup!!

    @JvmStatic
    var currentPagePath: String
        get() {
            var path = MyCollabSession.getCurrentUIVariable(CURRENT_PAGE_VAR) as? String
            if (path == null) {
                path = PathUtils.getProjectDocumentPath(AppUI.accountId, projectId)
                currentPagePath = path
            }

            return path
        }
        set(path) = MyCollabSession.putCurrentUIVariable(CURRENT_PAGE_VAR, path)

    @JvmStatic
    val projectId: Int
        get() {
            val project = project
            return if (project != null) project.id else -1
        }

    @JvmStatic
    val shortName: String
        get() {
            val project = project
            return if (project != null) project.shortname else ""
        }

    @JvmStatic
    fun canWriteTicket(ticket: ProjectTicket): Boolean = when {
        ticket.isTask -> canWrite(ProjectRolePermissionCollections.TASKS)
        ticket.isBug -> canWrite(ProjectRolePermissionCollections.BUGS)
        ticket.isRisk -> canWrite(ProjectRolePermissionCollections.RISKS)
        else -> false
    }

    @JvmStatic
    fun canReadTicket(): Boolean =
            (canRead(ProjectRolePermissionCollections.TASKS) || canRead(ProjectRolePermissionCollections.BUGS) ||
                    canRead(ProjectRolePermissionCollections.RISKS) || canRead(ProjectRolePermissionCollections.SPIKE))

    @JvmStatic
    fun canWriteTicket(): Boolean =
            (canWrite(ProjectRolePermissionCollections.TASKS) || canWrite(ProjectRolePermissionCollections.BUGS) ||
                    canWrite(ProjectRolePermissionCollections.RISKS) || canWrite(ProjectRolePermissionCollections.SPIKE))

    @JvmStatic
    val restrictedItemTypes: SetSearchField<String>
        get() {
            val types = SetSearchField<String>()
            if (canRead(ProjectRolePermissionCollections.MESSAGES)) {
                types.addValue(ProjectTypeConstants.MESSAGE)
            }
            if (canRead(ProjectRolePermissionCollections.MILESTONES)) {
                types.addValue(ProjectTypeConstants.MILESTONE)
            }
            if (canRead(ProjectRolePermissionCollections.TASKS)) {
                types.addValue(ProjectTypeConstants.TASK)
            }
            if (canRead(ProjectRolePermissionCollections.BUGS)) {
                types.addValue(ProjectTypeConstants.BUG)
            }
            if (canRead(ProjectRolePermissionCollections.RISKS)) {
                types.addValue(ProjectTypeConstants.RISK)
            }
            if (canRead(ProjectRolePermissionCollections.COMPONENTS)) {
                types.addValue(ProjectTypeConstants.BUG_COMPONENT)
            }
            if (canRead(ProjectRolePermissionCollections.VERSIONS)) {
                types.addValue(ProjectTypeConstants.BUG_VERSION)
            }
            return types
        }

    @JvmStatic
    val restrictedTicketTypes: SetSearchField<String>
        get() {
            val types = SetSearchField<String>()
            if (canRead(ProjectRolePermissionCollections.TASKS)) {
                types.addValue(ProjectTypeConstants.TASK)
            }
            if (canRead(ProjectRolePermissionCollections.BUGS)) {
                types.addValue(ProjectTypeConstants.BUG)
            }
            if (canRead(ProjectRolePermissionCollections.RISKS)) {
                types.addValue(ProjectTypeConstants.RISK)
            }
            return types
        }
}
