package com.mycollab.module.crm.service

import com.mycollab.core.cache.CacheArgs
import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.crm.domain.Account
import com.mycollab.module.crm.domain.AccountLead
import com.mycollab.module.crm.domain.SimpleAccount
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface AccountService : IDefaultService<Int, Account, AccountSearchCriteria> {

    @Cacheable
    fun findById(id: Int, @CacheKey accountId: Int): SimpleAccount?

    @CacheEvict
    @CacheArgs(values = arrayOf(LeadService::class))
    fun saveAccountLeadRelationship(associateLeads: List<AccountLead>, @CacheKey accountId: Int?)

    @CacheEvict
    @CacheArgs(values = arrayOf(LeadService::class))
    fun removeAccountLeadRelationship(associateLead: AccountLead, @CacheKey accountId: Int?)

    @Cacheable
    fun findAccountAssoWithConvertedLead(leadId: Int?, @CacheKey accountId: Int?): SimpleAccount
}
