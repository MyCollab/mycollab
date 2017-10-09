package com.mycollab.module.project.esb

import com.google.common.eventbus.AllowConcurrentEvents
import com.google.common.eventbus.Subscribe
import com.mycollab.common.dao.MonitorItemMapper
import com.mycollab.common.domain.MonitorItemExample
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.project.domain.ProjectMember
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class DeleteProjectMemberCommand(private val monitorItemMapper: MonitorItemMapper)  : GenericCommand() {

    @AllowConcurrentEvents
    @Subscribe
    fun deleteProjectMember(event: DeleteProjectMemberEvent) {
        removeAssociateWatchers(event.members)
    }

    private fun removeAssociateWatchers(members: Array<ProjectMember>) {
        members.forEach { member ->
            val monitorEx =  MonitorItemExample()
                    monitorEx.createCriteria().andExtratypeidEqualTo(member.projectid).andUserEqualTo(member.username)
                            .andSaccountidEqualTo(member.saccountid)
            monitorItemMapper.deleteByExample(monitorEx)
        }
    }
}