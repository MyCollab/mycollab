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
package com.mycollab.module.project.service

import com.mycollab.common.domain.GroupItem
import com.mycollab.db.arguments.SearchCriteria
import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.IMassUpdateDAO
import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.project.domain.SimpleTask
import com.mycollab.module.project.domain.Task
import com.mycollab.module.project.domain.criteria.TaskSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface ProjectTaskService : IDefaultService<Int, Task, TaskSearchCriteria> {

    @Cacheable
    fun findById(taskId: Int, @CacheKey sAccountId: Int): SimpleTask?

    @Cacheable
    fun findSubTasks(parentTaskId: Int, @CacheKey sAccountId: Int, orderField: SearchCriteria.OrderField): List<SimpleTask>

    @Cacheable
    fun findByProjectAndTaskKey(taskKey: Int, projectShortName: String, @CacheKey sAccountId: Int): SimpleTask?

    @Cacheable
    fun getPrioritySummary(@CacheKey criteria: TaskSearchCriteria): List<GroupItem>

    fun getCountOfOpenSubTasks(taskId: Int): Int

    @CacheEvict
    fun massUpdateTaskStatuses(parentTaskId: Int, status: String, @CacheKey sAccountId: Int)

    @Cacheable
    fun getStatusSummary(@CacheKey criteria: TaskSearchCriteria): List<GroupItem>

    @Cacheable
    fun getAssignedTasksSummary(@CacheKey criteria: TaskSearchCriteria): List<GroupItem>

    @CacheEvict
    fun massUpdateTaskIndexes(mapIndexes: List<Map<String, Int>>, @CacheKey sAccountId: Int)

    fun massUpdateStatuses(oldStatus: String, newStatus: String, projectId: Int, @CacheKey sAccountId: Int)
}
