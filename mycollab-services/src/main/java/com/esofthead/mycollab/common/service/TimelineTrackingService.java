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
package com.esofthead.mycollab.common.service;

import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.common.domain.TimelineTracking;
import com.esofthead.mycollab.common.domain.criteria.TimelineTrackingSearchCriteria;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.cache.Cacheable;
import com.esofthead.mycollab.core.persistence.service.ICrudService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
public interface TimelineTrackingService extends ICrudService<Integer, TimelineTracking> {
    @Cacheable
    Map<String, List<GroupItem>> findTimelineItems(String fieldGroup, List<String> groupVals, Date start, Date end,
                                                   @CacheKey TimelineTrackingSearchCriteria criteria);
}
