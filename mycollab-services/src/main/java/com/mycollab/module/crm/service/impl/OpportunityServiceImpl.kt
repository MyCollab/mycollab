/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.service.impl

import com.mycollab.aspect.ClassInfo
import com.mycollab.aspect.ClassInfoMap
import com.mycollab.aspect.Traceable
import com.mycollab.aspect.Watchable
import com.mycollab.common.ModuleNameConstants
import com.mycollab.common.domain.GroupItem
import com.mycollab.core.cache.CacheKey
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.crm.dao.OpportunityLeadMapper
import com.mycollab.module.crm.dao.OpportunityMapper
import com.mycollab.module.crm.dao.OpportunityMapperExt
import com.mycollab.module.crm.domain.*
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria
import com.mycollab.module.crm.service.ContactService
import com.mycollab.module.crm.service.OpportunityService
import com.mycollab.spring.AppContextUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(nameField = "opportunityname")
@Watchable(userFieldName = "assignuser")
class OpportunityServiceImpl(private val opportunityMapper: OpportunityMapper,
                             private val opportunityMapperExt: OpportunityMapperExt,
                             private val opportunityLeadMapper: OpportunityLeadMapper) : DefaultService<Int, Opportunity, OpportunitySearchCriteria>(), OpportunityService {

    override val crudMapper: ICrudGenericDAO<Int, Opportunity>
        get() = opportunityMapper as ICrudGenericDAO<Int, Opportunity>

    override val searchMapper: ISearchableDAO<OpportunitySearchCriteria>
        get() = opportunityMapperExt

    override fun findById(opportunityId: Int, sAccountId: Int): SimpleOpportunity? =
            opportunityMapperExt.findById(opportunityId)

    override fun saveWithSession(record: Opportunity, username: String?): Int {
        val result = super.saveWithSession(record, username)
        if (record.extraData != null && record.extraData is SimpleContact) {
            val associateOpportunity = ContactOpportunity()
            associateOpportunity.opportunityid = record.id
            associateOpportunity.contactid = (record.extraData as SimpleContact).id
            associateOpportunity.createdtime = GregorianCalendar().time
            val contactService = AppContextUtil.getSpringBean(ContactService::class.java)
            contactService.saveContactOpportunityRelationship(listOf(associateOpportunity),
                    record.saccountid)
        }
        return result
    }

    override fun getSalesStageSummary(criteria: OpportunitySearchCriteria): List<GroupItem> =
            opportunityMapperExt.getSalesStageSummary(criteria)

    override fun getLeadSourcesSummary(criteria: OpportunitySearchCriteria): List<GroupItem> =
            opportunityMapperExt.getLeadSourcesSummary(criteria)

    override fun getPipeline(@CacheKey criteria: OpportunitySearchCriteria): List<GroupItem> =
            opportunityMapperExt.getPipeline(criteria)

    override fun saveOpportunityLeadRelationship(associateLeads: List<OpportunityLead>, sAccountId: Int) {
        for (associateLead in associateLeads) {
            val ex = OpportunityLeadExample()
            ex.createCriteria().andOpportunityidEqualTo(associateLead.opportunityid)
                    .andLeadidEqualTo(associateLead.leadid)
            if (opportunityLeadMapper.countByExample(ex) == 0L) {
                opportunityLeadMapper.insert(associateLead)
            }
        }
    }

    override fun removeOpportunityLeadRelationship(associateLead: OpportunityLead, sAccountId: Int) {
        val ex = OpportunityLeadExample()
        ex.createCriteria().andOpportunityidEqualTo(associateLead.opportunityid)
                .andLeadidEqualTo(associateLead.leadid)
        opportunityLeadMapper.deleteByExample(ex)
    }

    override fun findOpportunityAssoWithConvertedLead(leadId: Int, @CacheKey accountId: Int): SimpleOpportunity? =
            opportunityMapperExt.findOpportunityAssoWithConvertedLead(leadId)

    companion object {

        init {
            ClassInfoMap.put(OpportunityServiceImpl::class.java, ClassInfo(ModuleNameConstants.CRM, CrmTypeConstants.OPPORTUNITY))
        }
    }
}
