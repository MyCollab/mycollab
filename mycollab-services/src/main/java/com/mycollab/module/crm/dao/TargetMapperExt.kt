package com.mycollab.module.crm.dao

import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.crm.domain.SimpleTarget
import com.mycollab.module.crm.domain.criteria.TargetSearchCriteria

interface TargetMapperExt : ISearchableDAO<TargetSearchCriteria> {

    fun findTargetById(targetId: Int): SimpleTarget
}
