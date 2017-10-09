package com.mycollab.module.crm.dao

import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.crm.domain.criteria.ContractSearchCriteria

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface ContractMapperExt : ISearchableDAO<ContractSearchCriteria>
