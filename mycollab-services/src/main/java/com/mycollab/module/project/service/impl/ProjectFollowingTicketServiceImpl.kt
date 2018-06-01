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

import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultSearchService
import com.mycollab.module.project.dao.ProjectFollowingTicketMapperExt
import com.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria
import com.mycollab.module.project.service.ProjectFollowingTicketService
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
open class ProjectFollowingTicketServiceImpl(private val projectFollowingTicketMapperExt: ProjectFollowingTicketMapperExt) : DefaultSearchService<FollowingTicketSearchCriteria>(), ProjectFollowingTicketService {

    override val searchMapper: ISearchableDAO<FollowingTicketSearchCriteria>
        get() = projectFollowingTicketMapperExt
}
