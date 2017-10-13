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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.community.module.project.service.impl

import com.mycollab.core.MyCollabException
import com.mycollab.module.project.domain.ProjectTicket
import com.mycollab.module.project.service.impl.AbstractProjectTicketServiceImpl
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd
 * @since 5.4.2
 */
@Service
class ProjectTicketServiceImpl : AbstractProjectTicketServiceImpl() {
    override fun updateAssignmentValue(assignment: ProjectTicket, username: String) {
        throw MyCollabException("Not support this operation in the community edition")
    }

    override fun closeSubAssignmentOfMilestone(milestoneId: Int?) {
        throw MyCollabException("Not support this operation in the community edition")
    }
}
