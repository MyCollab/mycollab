package com.mycollab.module.crm.dao

import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.crm.domain.SimpleCrmTask
import com.mycollab.module.crm.domain.criteria.CrmTaskSearchCriteria

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface CrmTaskMapperExt : ISearchableDAO<CrmTaskSearchCriteria> {
    fun findById(taskId: Int?): SimpleCrmTask
}
