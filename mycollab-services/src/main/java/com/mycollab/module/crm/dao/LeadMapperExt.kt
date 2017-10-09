package com.mycollab.module.crm.dao

import com.mycollab.db.persistence.IMassUpdateDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.crm.domain.Lead
import com.mycollab.module.crm.domain.SimpleLead
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria
import org.apache.ibatis.annotations.Param

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface LeadMapperExt : ISearchableDAO<LeadSearchCriteria>, IMassUpdateDAO<Lead, LeadSearchCriteria> {

    fun findById(leadId: Int?): SimpleLead

    fun findConvertedLeadOfAccount(@Param("accountId") accountId: Int?): SimpleLead

    fun findConvertedLeadOfContact(@Param("contactId") contactId: Int?): SimpleLead

    fun findConvertedLeadOfOpportunity(@Param("opportunityId") opportunity: Int?): SimpleLead
}
