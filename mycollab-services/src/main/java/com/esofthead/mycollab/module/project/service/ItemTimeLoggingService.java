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

import com.esofthead.mycollab.core.cache.CacheEvict;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.cache.Cacheable;
import com.esofthead.mycollab.core.persistence.service.IDefaultService;
import com.esofthead.mycollab.module.project.domain.ItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public interface ItemTimeLoggingService extends
		IDefaultService<Integer, ItemTimeLogging, ItemTimeLoggingSearchCriteria> {

	@Cacheable
	Double getTotalHoursByCriteria(
			@CacheKey ItemTimeLoggingSearchCriteria criteria);

	@CacheEvict
	void batchSaveTimeLogging(List<ItemTimeLogging> timeLoggings,
			@CacheKey int sAccountId);

	@Cacheable
	Double getTotalBillableHoursByTaskList(int taskListId, @CacheKey int sAccountId);

	@Cacheable
	Double getTotalNonBillableHoursByTaskList(int taskListId, @CacheKey int sAccountId);

	@Cacheable
	Double getRemainHoursByTaskList(int taskListId, @CacheKey int sAccountId);

	@Cacheable
	Double getTotalBillableHoursByMilestone(int milestoneId, @CacheKey int sAccountId);

	@Cacheable
	Double getTotalNonBillableHoursByMilestone(int milestoneId, @CacheKey int sAccountId);

	@Cacheable
	Double getRemainHoursByMilestone(int milestoneId, @CacheKey int sAccountId);

	@Cacheable
	Double getTotalBillableHoursByComponent(int componentId, @CacheKey int sAccountId);

	@Cacheable
	Double getTotalNonBillableHoursByComponent(int componentId, @CacheKey int sAccountId);

	@Cacheable
	Double getRemainHoursByComponent(int componentId, @CacheKey int sAccountId);

	@Cacheable
	Double getTotalBillableHoursByVersion(int versionId, @CacheKey int sAccountId);

	@Cacheable
	Double getTotalNonBillableHoursByVersion(int versionId, @CacheKey int sAccountId);

	@Cacheable
	Double getRemainHoursByVersion(int versionId, @CacheKey int sAccountId);
}
