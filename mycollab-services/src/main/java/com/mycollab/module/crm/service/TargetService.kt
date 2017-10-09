package com.mycollab.module.crm.service

import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.crm.domain.SimpleTarget
import com.mycollab.module.crm.domain.Target
import com.mycollab.module.crm.domain.criteria.TargetSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface TargetService : IDefaultService<Int, Target, TargetSearchCriteria> {
    fun findTargetById(targetId: Int): SimpleTarget
}
