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
import com.mycollab.module.project.dao.ProjectGenericItemMapper
import com.mycollab.module.project.domain.criteria.ProjectGenericItemSearchCriteria
import com.mycollab.module.project.service.ProjectGenericItemService
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
@Service
class ProjectGenericItemServiceImpl(private val projectGenericItemMapper: ProjectGenericItemMapper) : DefaultSearchService<ProjectGenericItemSearchCriteria>(), ProjectGenericItemService {

    override val searchMapper: ISearchableDAO<ProjectGenericItemSearchCriteria>
        get() = projectGenericItemMapper

    override fun getTotalCount(criteria: ProjectGenericItemSearchCriteria): Int {
        return projectGenericItemMapper.getTotalCountFromTask(criteria) +
                projectGenericItemMapper.getTotalCountFromMessage(criteria) +
                projectGenericItemMapper.getTotalCountFromMilestone(criteria) +
                projectGenericItemMapper.getTotalCountFromBug(criteria) +
                projectGenericItemMapper.getTotalCountFromVersion(criteria) +
                projectGenericItemMapper.getTotalCountFromComponent(criteria) +
                projectGenericItemMapper.getTotalCountFromRisk(criteria)
    }
}
