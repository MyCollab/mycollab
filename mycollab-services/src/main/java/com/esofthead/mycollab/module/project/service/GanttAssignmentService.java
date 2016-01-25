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


import com.esofthead.mycollab.core.cache.CacheEvict;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.cache.Cacheable;
import com.esofthead.mycollab.core.persistence.service.IService;
import com.esofthead.mycollab.module.project.domain.AssignWithPredecessors;
import com.esofthead.mycollab.module.project.domain.TaskPredecessor;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.0.0
 */
public interface GanttAssignmentService extends IService {
    @Cacheable
    List<AssignWithPredecessors> getTaskWithPredecessors(List<Integer> projectIds, @CacheKey Integer sAccountId);

    @CacheEvict
    void massUpdateGanttItems(List<AssignWithPredecessors> ganttItems, @CacheKey Integer sAccountId);

    @CacheEvict void massDeleteGanttItems(List<AssignWithPredecessors> ganttItems, @CacheKey Integer sAccountId);

    @CacheEvict
    void massUpdatePredecessors(Integer taskSourceId, List<TaskPredecessor> predecessors, @CacheKey Integer sAccountId);

    @CacheEvict
    void massDeletePredecessors(List<TaskPredecessor> predecessors, @CacheKey Integer sAccountId);
}
