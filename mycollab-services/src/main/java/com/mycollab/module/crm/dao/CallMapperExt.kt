package com.mycollab.module.crm.dao

import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.crm.domain.SimpleCall
import com.mycollab.module.crm.domain.criteria.CallSearchCriteria

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface CallMapperExt : ISearchableDAO<CallSearchCriteria> {

    fun findById(callId: Int?): SimpleCall
}
