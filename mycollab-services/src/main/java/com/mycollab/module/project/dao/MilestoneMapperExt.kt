package com.mycollab.module.project.dao

import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.project.domain.SimpleMilestone
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria
import org.apache.ibatis.annotations.Param

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface MilestoneMapperExt : ISearchableDAO<MilestoneSearchCriteria> {

    fun findById(milestoneId: Int?): SimpleMilestone

    fun getTotalBillableHours(@Param("milestoneid") milestoneId: Int): Double?

    fun getTotalNonBillableHours(@Param("milestoneid") milestoneId: Int): Double?

    fun getRemainHours(@Param("milestoneid") milestoneId: Int): Double?
}
