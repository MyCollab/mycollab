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

import com.esofthead.mycollab.module.GenericCommandHandler
import com.esofthead.mycollab.module.project.esb.DeleteProjectMemberEvent
import com.google.common.eventbus.{AllowConcurrentEvents, Subscribe}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.stereotype.Component

object DeleteProjectMemberCommandImpl {
    private val LOG: Logger = LoggerFactory.getLogger(classOf[DeleteProjectMemberCommandImpl])
}

@Component class DeleteProjectMemberCommandImpl extends GenericCommandHandler {
    
    @AllowConcurrentEvents
    @Subscribe
    def deleteProjectMember(event: DeleteProjectMemberEvent): Unit = {
        DeleteProjectMemberCommandImpl.LOG.debug("Remove project member has username {}, project member id {} and project id {}",
            Array(event.username, event.projectMemberId, event.projectId))
    }
}