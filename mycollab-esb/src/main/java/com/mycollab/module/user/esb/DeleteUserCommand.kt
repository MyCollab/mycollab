package com.mycollab.module.user.esb

import com.google.common.eventbus.AllowConcurrentEvents
import com.google.common.eventbus.Subscribe
import com.mycollab.cache.CleanCacheEvent
import com.mycollab.common.dao.MonitorItemMapper
import com.mycollab.common.domain.MonitorItemExample
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.project.ProjectMemberStatusConstants
import com.mycollab.module.project.dao.ProjectMemberMapper
import com.mycollab.module.project.domain.ProjectMember
import com.mycollab.module.project.domain.ProjectMemberExample
import com.mycollab.module.project.service.ProjectMemberService
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class DeleteUserCommand(private val projectMemberMapper: ProjectMemberMapper,
                        private val monitorItemMapper: MonitorItemMapper) : GenericCommand() {

    @AllowConcurrentEvents
    @Subscribe
    fun execute(event: DeleteUserEvent) {
        removeProjectInvolvement(event)
        removeUserMonitorItems(event)
        asyncEventBus.post(CleanCacheEvent(event.sAccountId, arrayOf(ProjectMemberService::class.java)))
    }

    private fun removeProjectInvolvement(event: DeleteUserEvent) {
        val ex = ProjectMemberExample()
        ex.createCriteria().andStatusNotIn(listOf(ProjectMemberStatusConstants.INACTIVE)).
                andSaccountidEqualTo(event.sAccountId).andUsernameEqualTo(event.username)
        val projectMember = ProjectMember()
        projectMember.status = ProjectMemberStatusConstants.INACTIVE
        projectMemberMapper.updateByExampleSelective(projectMember, ex)
    }

    private fun removeUserMonitorItems(event: DeleteUserEvent) {
        val ex = MonitorItemExample()
        ex.createCriteria().andSaccountidEqualTo(event.sAccountId).andUserEqualTo(event.username)
        monitorItemMapper.deleteByExample(ex)
    }
}