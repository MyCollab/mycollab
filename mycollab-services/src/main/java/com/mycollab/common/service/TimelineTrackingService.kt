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
package com.mycollab.common.service

import com.mycollab.common.domain.GroupItem
import com.mycollab.common.domain.TimelineTracking
import com.mycollab.common.domain.criteria.TimelineTrackingSearchCriteria
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.ICrudService

import java.util.Date

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
interface TimelineTrackingService : ICrudService<Int, TimelineTracking> {
    @Cacheable
    fun findTimelineItems(fieldGroup: String, groupVals: List<String>, start: Date, end: Date,
                          @CacheKey criteria: TimelineTrackingSearchCriteria): Map<String, List<GroupItem>>
}
