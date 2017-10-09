package com.mycollab.module.crm.service

import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.crm.domain.Lead
import com.mycollab.module.crm.domain.Opportunity
import com.mycollab.module.crm.domain.SimpleLead
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface LeadService : IDefaultService<Int, Lead, LeadSearchCriteria> {
    @Cacheable
    fun findById(leadId: Int, @CacheKey sAccountId: Int): SimpleLead?

    @CacheEvict
    fun convertLead(lead: SimpleLead, opportunity: Opportunity?, convertUser: String)

    fun findConvertedLeadOfAccount(accountId: Int?, @CacheKey sAccountId: Int?): SimpleLead

    fun findConvertedLeadOfContact(contactId: Int?, @CacheKey sAccountId: Int?): SimpleLead

    fun findConvertedLeadOfOpportunity(opportunity: Int?, @CacheKey sAccountId: Int?): SimpleLead
}
