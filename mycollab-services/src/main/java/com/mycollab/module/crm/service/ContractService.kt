package com.mycollab.module.crm.service

import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.crm.domain.Contract
import com.mycollab.module.crm.domain.criteria.ContractSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface ContractService : IDefaultService<Int, Contract, ContractSearchCriteria>
