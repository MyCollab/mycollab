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
        members.forEach {
            val monitorEx =  MonitorItemExample()
            monitorEx.createCriteria().andExtratypeidEqualTo(it.projectid).andUserEqualTo(it.username)
                    .andSaccountidEqualTo(it.saccountid)
            monitorItemMapper.deleteByExample(monitorEx)
        }
    }
}