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
import com.mycollab.core.UserInvalidInputException
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.utils.ArrayUtils
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.project.ProjectMemberStatusConstants
import com.mycollab.module.project.dao.ProjectMemberMapper
import com.mycollab.module.project.dao.ProjectMemberMapperExt
import com.mycollab.module.project.domain.ProjectMember
import com.mycollab.module.project.domain.ProjectMemberExample
import com.mycollab.module.project.domain.SimpleProjectMember
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria
import com.mycollab.module.project.esb.DeleteProjectMemberEvent
import com.mycollab.module.project.esb.InviteProjectMembersEvent
import com.mycollab.module.project.service.ProjectMemberService
import com.mycollab.module.user.domain.SimpleUser
import org.apache.commons.collections.CollectionUtils
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
class ProjectMemberServiceImpl(private val projectMemberMapper: ProjectMemberMapper,
                               private val projectMemberMapperExt: ProjectMemberMapperExt,
                               private val asyncEventBus: AsyncEventBus) : DefaultService<Int, ProjectMember, ProjectMemberSearchCriteria>(), ProjectMemberService {

    override val crudMapper: ICrudGenericDAO<Int, ProjectMember>
        get() = projectMemberMapper as ICrudGenericDAO<Int, ProjectMember>

    override val searchMapper: ISearchableDAO<ProjectMemberSearchCriteria>
        get() = projectMemberMapperExt

    override fun findById(memberId: Int, sAccountId: Int): SimpleProjectMember? =
            projectMemberMapperExt.findMemberById(memberId)

    override fun getUsersNotInProject(projectId: Int?, sAccountId: Int?): List<SimpleUser> =
            projectMemberMapperExt.getUsersNotInProject(projectId!!, sAccountId!!)

    override fun findMemberByUsername(username: String, projectId: Int, sAccountId: Int): SimpleProjectMember? =
            projectMemberMapperExt.findMemberByUsername(username, projectId)

    override fun updateWithSession(record: ProjectMember, username: String?): Int {
        val oldMember = findById(record.id, record.saccountid)
        if (oldMember != null) {
            if (java.lang.Boolean.FALSE == record.isadmin && java.lang.Boolean.TRUE == oldMember.isadmin) {
                val userAccountEx = ProjectMemberExample()
                userAccountEx.createCriteria().andUsernameNotIn(listOf(record.username)).andProjectidEqualTo(record.projectid)
                        .andIsadminEqualTo(java.lang.Boolean.TRUE).andStatusEqualTo(ProjectMemberStatusConstants.ACTIVE)
                if (projectMemberMapper.countByExample(userAccountEx) == 0L) {
                    throw UserInvalidInputException("Can not change role of user ${record.username}. The reason is ${record.username} is the unique account owner of the current project.")
                }
            }
        }

        return super.updateWithSession(record, username)
    }

    override fun massRemoveWithSession(items: List<ProjectMember>, username: String?, sAccountId: Int) {
        if (CollectionUtils.isNotEmpty(items)) {
            val userNames = items.map { it.username }
            var ex = ProjectMemberExample()
            ex.createCriteria().andUsernameNotIn(userNames).andProjectidEqualTo(items[0].projectid)
                    .andIsadminEqualTo(true).andStatusEqualTo(ProjectMemberStatusConstants.ACTIVE)
            if (projectMemberMapper.countByExample(ex) == 0L) {
                throw UserInvalidInputException("Can not delete users. The reason is there is no project owner in the rest users")
            }

            val updateMember = ProjectMember()
            updateMember.status = ProjectMemberStatusConstants.INACTIVE
            ex = ProjectMemberExample()
            ex.createCriteria().andSaccountidEqualTo(sAccountId).andIdIn(ArrayUtils.extractIds(items))
            projectMemberMapper.updateByExampleSelective(updateMember, ex)

            val event = DeleteProjectMemberEvent(items.toTypedArray(), username, sAccountId)
            asyncEventBus.post(event)
        }
    }

    override fun getActiveUsersInProject(projectId: Int?, sAccountId: Int?): List<SimpleUser> =
            projectMemberMapperExt.getActiveUsersInProject(projectId!!, sAccountId!!)

    override fun inviteProjectMembers(email: Array<String>, projectId: Int, projectRoleId: Int?, inviteUser: String,
                                      inviteMessage: String, sAccountId: Int) {
        val event = InviteProjectMembersEvent(email, projectId, projectRoleId, inviteUser,
                inviteMessage, sAccountId)
        asyncEventBus.post(event)
    }

    override fun isUserBelongToProject(username: String, projectId: Int, sAccountId: Int): Boolean {
        val criteria = ProjectMemberSearchCriteria()
        criteria.projectIds = SetSearchField(projectId)
        criteria.saccountid = NumberSearchField(sAccountId)
        criteria.involvedMember = StringSearchField.and(username)
        criteria.statuses = SetSearchField(ProjectMemberStatusConstants.ACTIVE,
                ProjectMemberStatusConstants.NOT_ACCESS_YET)
        return getTotalCount(criteria) > 0
    }

    override fun getActiveUsersInProjects(projectIds: List<Int>, sAccountId: Int?): List<SimpleUser> =
            projectMemberMapperExt.getActiveUsersInProjects(projectIds, sAccountId)

    override fun getActiveUserOfProject(username: String, projectId: Int, @CacheKey sAccountId: Int): SimpleUser? =
            projectMemberMapperExt.getActiveUserOfProject(username, projectId, sAccountId)

    override fun findMembersHourlyInProject(projectId: Int?, sAccountId: Int?, start: Date, end: Date): List<SimpleProjectMember> =
            projectMemberMapperExt.findMembersHourlyInProject(projectId, sAccountId, start, end)
}
