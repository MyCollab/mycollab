package com.mycollab.module.crm.dao

import com.mycollab.common.domain.GroupItem
import com.mycollab.db.persistence.IMassUpdateDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.crm.domain.Opportunity
import com.mycollab.module.crm.domain.SimpleOpportunity
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria
import org.apache.ibatis.annotations.Param

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface OpportunityMapperExt : ISearchableDAO<OpportunitySearchCriteria>, IMassUpdateDAO<Opportunity, OpportunitySearchCriteria> {

    fun findById(opportunityId: Int?): SimpleOpportunity

    fun getSalesStageSummary(@Param("searchCriteria") criteria: OpportunitySearchCriteria): List<GroupItem>

    fun getLeadSourcesSummary(@Param("searchCriteria") criteria: OpportunitySearchCriteria): List<GroupItem>

    fun getPipeline(@Param("searchCriteria") criteria: OpportunitySearchCriteria): List<GroupItem>

    fun findOpportunityAssoWithConvertedLead(@Param("leadId") leadId: Int): SimpleOpportunity
}
