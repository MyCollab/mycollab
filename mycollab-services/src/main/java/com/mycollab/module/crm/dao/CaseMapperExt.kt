package com.mycollab.module.crm.dao

import com.mycollab.db.persistence.IMassUpdateDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.crm.domain.CaseWithBLOBs
import com.mycollab.module.crm.domain.SimpleCase
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface CaseMapperExt : ISearchableDAO<CaseSearchCriteria>, IMassUpdateDAO<CaseWithBLOBs, CaseSearchCriteria> {

    fun findById(caseId: Int?): SimpleCase
}
