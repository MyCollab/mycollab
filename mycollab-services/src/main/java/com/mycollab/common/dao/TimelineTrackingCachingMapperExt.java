package com.mycollab.common.dao;

import com.mycollab.common.domain.GroupItem;
import com.mycollab.common.domain.criteria.TimelineTrackingSearchCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
public interface TimelineTrackingCachingMapperExt {
    List<GroupItem> findTimelineItems(@Param("groupVals") List<String> groupVals,
                                      @Param("dates") List<Date> dates,
                                      @Param("searchCriteria") TimelineTrackingSearchCriteria criteria);
}
