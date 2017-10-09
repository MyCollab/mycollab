package com.mycollab.module.crm.service

import com.mycollab.core.cache.CacheArgs
import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria
import com.mycollab.module.crm.domain.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface CampaignService : IDefaultService<Int, CampaignWithBLOBs, CampaignSearchCriteria> {

    @Cacheable
    fun findById(campaignId: Int, @CacheKey sAccountId: Int): SimpleCampaign?

    @CacheEvict
    @CacheArgs(values = arrayOf(AccountService::class))
    fun saveCampaignAccountRelationship(associateAccounts: List<CampaignAccount>, @CacheKey sAccountId: Int?)

    @CacheEvict
    @CacheArgs(values = arrayOf(AccountService::class))
    fun removeCampaignAccountRelationship(associateAccount: CampaignAccount, @CacheKey sAccountId: Int?)

    @CacheEvict
    @CacheArgs(values = arrayOf(ContactService::class))
    fun saveCampaignContactRelationship(associateContacts: List<CampaignContact>, @CacheKey sAccountId: Int?)

    @CacheEvict
    @CacheArgs(values = arrayOf(ContactService::class))
    fun removeCampaignContactRelationship(associateContact: CampaignContact, @CacheKey sAccountId: Int?)

    @CacheEvict
    @CacheArgs(values = arrayOf(LeadService::class))
    fun saveCampaignLeadRelationship(associateLeads: List<CampaignLead>, @CacheKey sAccountId: Int?)

    @CacheEvict
    @CacheArgs(values = arrayOf(LeadService::class))
    fun removeCampaignLeadRelationship(associateLead: CampaignLead, @CacheKey sAccountId: Int?)
}
