/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.service;

import java.util.List;

import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.cache.Cacheable;
import com.esofthead.mycollab.core.persistence.service.IDefaultService;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public interface ProjectTaskService extends
		IDefaultService<Integer, Task, TaskSearchCriteria> {

	@Cacheable
	SimpleTask findById(Integer taskId, @CacheKey Integer sAccountId);

	@Cacheable
	List<SimpleTask> findSubTasksOfGroup(Integer taskgroupId, @CacheKey Integer sAccountId);

	@Cacheable
	List<SimpleTask> findSubTasks(Integer parentTaskId, @CacheKey Integer sAccountId);

	@Cacheable
	SimpleTask findByProjectAndTaskKey(Integer taskkey, String projectShortName,
			@CacheKey Integer sAccountId);

	@Cacheable
	List<GroupItem> getPrioritySummary(@CacheKey TaskSearchCriteria criteria);

	@Cacheable
	List<GroupItem> getAssignedDefectsSummary(
			@CacheKey TaskSearchCriteria criteria);
}
