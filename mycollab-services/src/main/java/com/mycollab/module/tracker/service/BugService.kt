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
package com.mycollab.module.tracker.service

import com.mycollab.common.domain.GroupItem
import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.tracker.domain.BugWithBLOBs
import com.mycollab.module.tracker.domain.SimpleBug
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface BugService : IDefaultService<Int, BugWithBLOBs, BugSearchCriteria> {

    @Cacheable
    fun findById(bugId: Int, @CacheKey sAccountId: Int): SimpleBug?

    @Cacheable
    fun findByProjectAndBugKey(bugKey: Int, projectShortName: String, @CacheKey sAccountId: Int): SimpleBug?

    @Cacheable
    fun getStatusSummary(@CacheKey criteria: BugSearchCriteria): List<GroupItem>

    @Cacheable
    fun getPrioritySummary(@CacheKey criteria: BugSearchCriteria): List<GroupItem>

    @Cacheable
    fun getAssignedDefectsSummary(@CacheKey criteria: BugSearchCriteria): List<GroupItem>

    @Cacheable
    fun getResolutionDefectsSummary(@CacheKey criteria: BugSearchCriteria): List<GroupItem>

    @Cacheable
    fun getReporterDefectsSummary(@CacheKey criteria: BugSearchCriteria): List<GroupItem>

    @Cacheable
    fun getVersionDefectsSummary(@CacheKey criteria: BugSearchCriteria): List<GroupItem>

    @Cacheable
    fun getComponentDefectsSummary(@CacheKey criteria: BugSearchCriteria): List<GroupItem>

    @CacheEvict
    fun massUpdateBugIndexes(mapIndexes: List<Map<String, Int>>, @CacheKey sAccountId: Int)
}
