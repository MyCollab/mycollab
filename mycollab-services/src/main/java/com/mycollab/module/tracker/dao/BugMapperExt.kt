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
package com.mycollab.module.tracker.dao

import com.mycollab.common.domain.GroupItem
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.tracker.domain.SimpleBug
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria
import org.apache.ibatis.annotations.Param

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface BugMapperExt : ISearchableDAO<BugSearchCriteria> {

    fun getBugById(bugId: Int): SimpleBug?

    fun findByProjectAndBugKey(@Param("bugkey") bugKey: Int, @Param("prjShortName") projectShortName: String,
                               @Param("sAccountId") sAccountId: Int): SimpleBug?

    fun getStatusSummary(@Param("searchCriteria") criteria: BugSearchCriteria): List<GroupItem>

    fun getPrioritySummary(@Param("searchCriteria") criteria: BugSearchCriteria): List<GroupItem>

    fun getAssignedDefectsSummary(@Param("searchCriteria") criteria: BugSearchCriteria): List<GroupItem>

    fun getResolutionDefectsSummary(@Param("searchCriteria") criteria: BugSearchCriteria): List<GroupItem>

    fun getReporterDefectsSummary(@Param("searchCriteria") criteria: BugSearchCriteria): List<GroupItem>

    fun getVersionDefectsSummary(@Param("searchCriteria") criteria: BugSearchCriteria): List<GroupItem>

    fun getComponentDefectsSummary(@Param("searchCriteria") criteria: BugSearchCriteria): List<GroupItem>

    fun getMaxKey(projectId: Int): Int?
}
