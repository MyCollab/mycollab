package com.mycollab.module.crm.service

import com.mycollab.common.domain.GroupItem
import com.mycollab.core.cache.CacheArgs
import com.mycollab.core.cache.CacheEvict
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.crm.domain.Opportunity
import com.mycollab.module.crm.domain.OpportunityLead
import com.mycollab.module.crm.domain.SimpleOpportunity
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface OpportunityService : IDefaultService<Int, Opportunity, OpportunitySearchCriteria> {

    @Cacheable
    fun findById(opportunityId: Int, @CacheKey sAccountId: Int): SimpleOpportunity?

    @Cacheable
    fun getSalesStageSummary(@CacheKey criteria: OpportunitySearchCriteria): List<GroupItem>

    @Cacheable
    fun getPipeline(@CacheKey criteria: OpportunitySearchCriteria): List<GroupItem>

    @Cacheable
    fun getLeadSourcesSummary(@CacheKey criteria: OpportunitySearchCriteria): List<GroupItem>

    @CacheEvict
    @CacheArgs(values = arrayOf(LeadService::class))
    fun saveOpportunityLeadRelationship(associateLeads: List<OpportunityLead>, @CacheKey sAccountId: Int?)

    @CacheEvict
    @CacheArgs(values = arrayOf(LeadService::class))
    fun removeOpportunityLeadRelationship(associateLead: OpportunityLead, @CacheKey sAccountId: Int?)

    @Cacheable
    fun findOpportunityAssoWithConvertedLead(leadId: Int?, @CacheKey accountId: Int?): SimpleOpportunity
}
