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
package com.mycollab.module.project.dao

import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria
import com.mycollab.common.domain.criteria.MonitorSearchCriteria
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.project.domain.FollowingTicket
import com.mycollab.module.project.domain.ProjectActivityStream
import com.mycollab.module.project.domain.ProjectRelayEmailNotification
import com.mycollab.module.project.domain.SimpleProject
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria
import com.mycollab.module.user.domain.BillingAccount
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.session.RowBounds

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface ProjectMapperExt : ISearchableDAO<ProjectSearchCriteria> {

    fun getTotalActivityStream(@Param("searchCriteria") criteria: ActivityStreamSearchCriteria): Int

    fun getProjectActivityStreams(@Param("searchCriteria") criteria: ActivityStreamSearchCriteria, rowBounds: RowBounds): List<ProjectActivityStream>

    fun getUserProjectKeys(@Param("searchCriteria") criteria: ProjectSearchCriteria): List<Int>

    fun getProjectsUserInvolved(@Param("username") username: String, @Param("sAccountId") sAccountId: Int?): List<SimpleProject>

    fun findProjectById(projectId: Int): SimpleProject

    fun getAccountInfoOfProject(projectId: Int): BillingAccount

    fun getTotalFollowingTickets(@Param("searchCriteria") searchRequest: MonitorSearchCriteria): Int

    fun getProjectFollowingTickets(@Param("searchCriteria") searchRequest: MonitorSearchCriteria, rowBounds: RowBounds): List<FollowingTicket>

    fun findProjectRelayEmailNotifications(): List<ProjectRelayEmailNotification>
}
