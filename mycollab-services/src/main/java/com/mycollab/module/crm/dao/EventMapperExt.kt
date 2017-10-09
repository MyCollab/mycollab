package com.mycollab.module.crm.dao

import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria
import org.apache.ibatis.annotations.Param

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface EventMapperExt : ISearchableDAO<ActivitySearchCriteria> {
    fun getTotalCountFromTask(@Param("searchCriteria") criteria: ActivitySearchCriteria): Int

    fun getTotalCountFromCall(@Param("searchCriteria") criteria: ActivitySearchCriteria): Int

    fun getTotalCountFromMeeting(@Param("searchCriteria") criteria: ActivitySearchCriteria): Int
}
