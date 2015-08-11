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
package com.esofthead.mycollab.module.tracker.service;

import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.core.cache.CacheEvict;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.cache.Cacheable;
import com.esofthead.mycollab.core.persistence.service.IDefaultService;
import com.esofthead.mycollab.module.tracker.domain.BugStatusGroupItem;
import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;

import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface BugService extends IDefaultService<Integer, BugWithBLOBs, BugSearchCriteria> {

    @Cacheable
    SimpleBug findById(Integer bugId, @CacheKey Integer sAccountId);

    @Cacheable
    SimpleBug findByProjectAndBugKey(Integer bugKey, String projectShortName, @CacheKey Integer sAccountId);

    @Cacheable
    List<GroupItem> getStatusSummary(@CacheKey BugSearchCriteria criteria);

    @Cacheable
    List<GroupItem> getPrioritySummary(@CacheKey BugSearchCriteria criteria);

    @Cacheable
    List<GroupItem> getAssignedDefectsSummary(@CacheKey BugSearchCriteria criteria);

    @Cacheable
    List<GroupItem> getResolutionDefectsSummary(@CacheKey BugSearchCriteria criteria);

    @Cacheable
    List<BugStatusGroupItem> getBugStatusGroupItemBaseComponent(@CacheKey BugSearchCriteria criteria);

    @Cacheable
    List<GroupItem> getReporterDefectsSummary(@CacheKey BugSearchCriteria criteria);

    @Cacheable
    List<GroupItem> getVersionDefectsSummary(@CacheKey BugSearchCriteria criteria);

    @Cacheable
    List<GroupItem> getComponentDefectsSummary(@CacheKey BugSearchCriteria searchCriteria);

    @CacheEvict
    void massUpdateBugIndexes(List<Map<String, Integer>> mapIndexes, @CacheKey Integer sAccountId);
}
