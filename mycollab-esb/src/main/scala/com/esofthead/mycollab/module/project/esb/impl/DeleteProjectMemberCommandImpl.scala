/**
 * This file is part of mycollab-esb.
 *
 * mycollab-esb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-esb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-esb.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
  * This file is part of mycollab-esb.
  *
  * mycollab-esb is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * mycollab-esb is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with mycollab-esb.  If not, see <http://www.gnu.org/licenses/>.
  */
package com.esofthead.mycollab.module.project.esb.impl

import com.esofthead.mycollab.common.dao.MonitorItemMapper
import com.esofthead.mycollab.common.domain.MonitorItemExample
import com.esofthead.mycollab.common.service.MonitorItemService
import com.esofthead.mycollab.module.GenericCommand
import com.esofthead.mycollab.module.project.domain.ProjectMember
import com.esofthead.mycollab.module.project.esb.DeleteProjectMemberEvent
import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd.
  * @since 1.0
  */
@Component class DeleteProjectMemberCommandImpl extends GenericCommand {
  @Autowired private val monitorItemMapper: MonitorItemMapper = null

  @AllowConcurrentEvents
  @Subscribe
  def deleteProjectMember(event: DeleteProjectMemberEvent): Unit = {
    removeAssociateWatchers(event.members)
  }

  private def removeAssociateWatchers(members: Array[ProjectMember]): Unit = {
    for (member <- members) {
      val monitorEx = new MonitorItemExample
      monitorEx.createCriteria().andExtratypeidEqualTo(member.getProjectid).andUserEqualTo(member.getUsername)
        .andSaccountidEqualTo(member.getSaccountid)
      monitorItemMapper.deleteByExample(monitorEx)
    }
  }
}