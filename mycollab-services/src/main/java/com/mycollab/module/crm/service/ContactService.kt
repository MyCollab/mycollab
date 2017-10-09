package com.mycollab.module.crm.service

import com.mycollab.core.cache.CacheArgs
import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria
import com.mycollab.module.crm.domain.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface ContactService : IDefaultService<Int, Contact, ContactSearchCriteria> {

    @Cacheable
    fun findById(contactId: Int, @CacheKey sAccountId: Int): SimpleContact?

    @CacheEvict
    @CacheArgs(values = arrayOf(OpportunityService::class, ContactOpportunityService::class))
    fun removeContactOpportunityRelationship(associateOpportunity: ContactOpportunity, @CacheKey sAccountId: Int?)

    @CacheEvict
    @CacheArgs(values = arrayOf(OpportunityService::class, ContactOpportunityService::class))
    fun saveContactOpportunityRelationship(associateOpportunities: List<ContactOpportunity>, @CacheKey accountId: Int?)

    @CacheEvict
    @CacheArgs(values = arrayOf(LeadService::class))
    fun saveContactLeadRelationship(associateLeads: List<ContactLead>, @CacheKey accountId: Int?)

    @CacheEvict
    @CacheArgs(values = arrayOf(CaseService::class))
    fun saveContactCaseRelationship(associateCases: List<ContactCase>, @CacheKey accountId: Int?)

    @CacheEvict
    @CacheArgs(values = arrayOf(CaseService::class))
    fun removeContactCaseRelationship(associateCase: ContactCase, @CacheKey sAccountId: Int?)

    @CacheEvict
    @Cacheable
    fun findContactAssoWithConvertedLead(leadId: Int, @CacheKey accountId: Int?): SimpleContact
}
