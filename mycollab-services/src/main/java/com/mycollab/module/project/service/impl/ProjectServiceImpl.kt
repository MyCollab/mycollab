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
package com.mycollab.module.project.service.impl

import com.google.common.eventbus.AsyncEventBus
import com.mycollab.aspect.ClassInfo
import com.mycollab.aspect.ClassInfoMap
import com.mycollab.aspect.Traceable
import com.mycollab.cache.CleanCacheEvent
import com.mycollab.common.ModuleNameConstants
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum
import com.mycollab.core.UserInvalidInputException
import com.mycollab.core.cache.CacheKey
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.billing.service.BillingPlanCheckerService
import com.mycollab.module.project.ProjectMemberStatusConstants
import com.mycollab.module.project.ProjectRolePermissionCollections
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.dao.ProjectMapper
import com.mycollab.module.project.dao.ProjectMapperExt
import com.mycollab.module.project.dao.ProjectMemberMapper
import com.mycollab.module.project.domain.*
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria
import com.mycollab.module.project.esb.AddProjectEvent
import com.mycollab.module.project.esb.DeleteProjectEvent
import com.mycollab.module.project.service.ProjectRoleService
import com.mycollab.module.project.service.ProjectService
import com.mycollab.module.user.domain.BillingAccount
import com.mycollab.security.AccessPermissionFlag
import com.mycollab.security.BooleanPermissionFlag
import com.mycollab.security.PermissionMap
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(nameField = "name", extraFieldName = "id")
class ProjectServiceImpl(private val projectMapper: ProjectMapper,
                         private val projectMapperExt: ProjectMapperExt,
                         private val projectMemberMapper: ProjectMemberMapper,
                         private val projectRoleService: ProjectRoleService,
                         private val billingPlanCheckerService: BillingPlanCheckerService,
                         private val asyncEventBus: AsyncEventBus) : DefaultService<Int, Project, ProjectSearchCriteria>(), ProjectService {

    override val crudMapper: ICrudGenericDAO<Int, Project>
        get() = projectMapper as ICrudGenericDAO<Int, Project>

    override val searchMapper: ISearchableDAO<ProjectSearchCriteria>
        get() = projectMapperExt

    override fun updateWithSession(record: Project, username: String?): Int {
        assertExistProjectShortnameInAccount(record.id, record.shortname, record.saccountid)
        return super.updateWithSession(record, username)
    }

    override fun savePlainProject(record: Project, username: String?): Int {
        billingPlanCheckerService.validateAccountCanCreateMoreProject(record.saccountid)
        assertExistProjectShortnameInAccount(null, record.shortname, record.saccountid)
        val projectId = super.saveWithSession(record, username)
        asyncEventBus.post(CleanCacheEvent(record.saccountid, arrayOf(ProjectService::class.java)))
        return projectId
    }

    override fun saveWithSession(record: Project, username: String?): Int {
        billingPlanCheckerService.validateAccountCanCreateMoreProject(record.saccountid)
        assertExistProjectShortnameInAccount(null, record.shortname, record.saccountid)
        val projectId = savePlainProject(record, username)

        // Add the first user to project
        val projectMember = ProjectMember()
        projectMember.isadmin = java.lang.Boolean.TRUE
        projectMember.status = ProjectMemberStatusConstants.ACTIVE
        projectMember.joindate = LocalDateTime.now()
        projectMember.projectid = projectId
        projectMember.username = username
        projectMember.saccountid = record.saccountid
        projectMemberMapper.insert(projectMember)

        // add client role to project
        val clientRole = createProjectRole(projectId, record.saccountid, "Client", "Default role for client")

        val clientRoleId = projectRoleService.saveWithSession(clientRole, username)

        val permissionMapClient = PermissionMap()
        (0 until ProjectRolePermissionCollections.PROJECT_PERMISSIONS.size)
                .map { ProjectRolePermissionCollections.PROJECT_PERMISSIONS[it] }
                .forEach {
                    if (it == ProjectRolePermissionCollections.USERS
                            || it == ProjectRolePermissionCollections.ROLES
                            || it == ProjectRolePermissionCollections.TIME) {
                        permissionMapClient.addPath(it, AccessPermissionFlag.READ_ONLY)
                    } else if (it == ProjectRolePermissionCollections.FINANCE ||
                            it == ProjectRolePermissionCollections.APPROVE_TIMESHEET) {
                        permissionMapClient.addPath(it, BooleanPermissionFlag.TRUE)
                    } else {
                        permissionMapClient.addPath(it, AccessPermissionFlag.READ_ONLY)
                    }
                }
        projectRoleService.savePermission(projectId, clientRoleId, permissionMapClient, record.saccountid)

        // add consultant role to project
        LOG.debug("Add consultant role to project ${record.name}")
        val consultantRole = createProjectRole(projectId, record.saccountid, "Consultant",
                "Default role for consultant")
        val consultantRoleId = projectRoleService.saveWithSession(consultantRole, username)

        val permissionMapConsultant = PermissionMap()
        (0 until ProjectRolePermissionCollections.PROJECT_PERMISSIONS.size)
                .map { ProjectRolePermissionCollections.PROJECT_PERMISSIONS[it] }
                .forEach {
                    if (it == ProjectRolePermissionCollections.USERS || it == ProjectRolePermissionCollections.ROLES) {
                        permissionMapConsultant.addPath(it, AccessPermissionFlag.READ_ONLY)
                    } else if (it == ProjectRolePermissionCollections.TIME) {
                        permissionMapConsultant.addPath(it, AccessPermissionFlag.READ_WRITE)
                    } else if (it == ProjectRolePermissionCollections.INVOICE) {
                        permissionMapConsultant.addPath(it, AccessPermissionFlag.NO_ACCESS)
                    } else if (it == ProjectRolePermissionCollections.FINANCE) {
                        permissionMapConsultant.addPath(it, BooleanPermissionFlag.FALSE)
                    } else if (it == ProjectRolePermissionCollections.APPROVE_TIMESHEET) {
                        permissionMapConsultant.addPath(it, BooleanPermissionFlag.FALSE)
                    } else {
                        permissionMapConsultant.addPath(it, AccessPermissionFlag.ACCESS)
                    }
                }
        projectRoleService.savePermission(projectId, consultantRoleId,
                permissionMapConsultant, record.saccountid)

        // add admin role to project
        LOG.debug("Add admin role to project ${record.name}")
        val adminRole = createProjectRole(projectId, record.saccountid, "Admin", "Default role for admin")
        val adminRoleId = projectRoleService.saveWithSession(adminRole, username)

        val permissionMapAdmin = PermissionMap()
        (0 until ProjectRolePermissionCollections.PROJECT_PERMISSIONS.size)
                .map { ProjectRolePermissionCollections.PROJECT_PERMISSIONS[it] }
                .forEach {
                    if (it == ProjectRolePermissionCollections.FINANCE ||
                            it == ProjectRolePermissionCollections.APPROVE_TIMESHEET) {
                        permissionMapAdmin.addPath(it, BooleanPermissionFlag.TRUE)
                    } else {
                        permissionMapAdmin.addPath(it, AccessPermissionFlag.ACCESS)
                    }
                }
        projectRoleService.savePermission(projectId, adminRoleId, permissionMapAdmin, record.saccountid)

        //Do async task to create some post data after project is created
        val event = AddProjectEvent(projectId, record.saccountid)
        asyncEventBus.post(event)

        return projectId
    }

    private fun assertExistProjectShortnameInAccount(projectId: Int?, shortName: String, sAccountId: Int?) {
        val ex = ProjectExample()
        val criteria = ex.createCriteria()
        criteria.andShortnameEqualTo(shortName).andSaccountidEqualTo(sAccountId)
        if (projectId != null) {
            criteria.andIdNotEqualTo(projectId)
        }
        if (projectMapper.countByExample(ex) > 0) {
            throw UserInvalidInputException("There is already project in the account has short name $shortName")
        }
    }

    private fun createProjectRole(projectId: Int?, sAccountId: Int?, roleName: String, description: String): ProjectRole {
        val projectRole = ProjectRole()
        projectRole.projectid = projectId
        projectRole.saccountid = sAccountId
        projectRole.rolename = roleName
        projectRole.description = description
        return projectRole
    }

    override fun findById(projectId: Int, sAccountId: Int): SimpleProject =
            projectMapperExt.findProjectById(projectId)

    override fun getProjectKeysUserInvolved(username: String, sAccountId: Int): List<Int> {
        val searchCriteria = ProjectSearchCriteria()
        searchCriteria.involvedMember = StringSearchField.and(username)
        searchCriteria.projectStatuses = SetSearchField(StatusI18nEnum.Open.name)
        return projectMapperExt.getUserProjectKeys(searchCriteria)
    }

    override fun getAccountInfoOfProject(projectId: Int): BillingAccount =
            projectMapperExt.getAccountInfoOfProject(projectId)

    override fun massRemoveWithSession(projects: List<Project>, username: String?, sAccountId: Int) {
        super.massRemoveWithSession(projects, username, sAccountId)
        val event = DeleteProjectEvent(projects.toTypedArray(), sAccountId)
        asyncEventBus.post(event)
    }

    override fun getTotalActiveProjectsInAccount(@CacheKey sAccountId: Int): Int {
        val criteria = ProjectSearchCriteria()
        criteria.saccountid = NumberSearchField(sAccountId)
        criteria.projectStatuses = SetSearchField(StatusI18nEnum.Open.name)
        return projectMapperExt.getTotalCount(criteria)
    }

    override fun findProjectRelayEmailNotifications(): List<ProjectRelayEmailNotification> =
            projectMapperExt.findProjectRelayEmailNotifications()

    override fun getProjectsUserInvolved(username: String, sAccountId: Int): List<SimpleProject> =
            projectMapperExt.getProjectsUserInvolved(username, sAccountId)

    override fun getTotalActiveProjectsOfInvolvedUsers(username: String, @CacheKey sAccountId: Int?): Int? {
        val criteria = ProjectSearchCriteria()
        criteria.involvedMember = StringSearchField.and(username)
        criteria.projectStatuses = SetSearchField(StatusI18nEnum.Open.name)
        return projectMapperExt.getTotalCount(criteria)
    }

    companion object {

        init {
            ClassInfoMap.put(ProjectServiceImpl::class.java, ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.PROJECT))
        }

        private val LOG = LoggerFactory.getLogger(ProjectServiceImpl::class.java)
    }
}
