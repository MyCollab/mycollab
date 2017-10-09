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
