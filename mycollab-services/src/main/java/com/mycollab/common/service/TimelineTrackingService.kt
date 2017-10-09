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
