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
import com.mycollab.core.cache.CacheKey
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.crm.dao.*
import com.mycollab.module.crm.domain.*
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria
import com.mycollab.module.crm.service.CampaignService
import com.mycollab.module.crm.service.ContactService
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
@Traceable(nameField = "lastname")
@Watchable(userFieldName = "assignuser")
open class ContactServiceImpl(private val contactMapper: ContactMapper,
                         private val contactMapperExt: ContactMapperExt,
                         private val contactOpportunityMapper: ContactOpportunityMapper,
                         private val contactCaseMapper: ContactCaseMapper,
                         private val contactLeadMapper: ContactLeadMapper) : DefaultService<Int, Contact, ContactSearchCriteria>(), ContactService {

    override val crudMapper: ICrudGenericDAO<Int, Contact>
        get() = contactMapper as ICrudGenericDAO<Int, Contact>

    override val searchMapper: ISearchableDAO<ContactSearchCriteria>
        get() = contactMapperExt

    override fun findById(contactId: Int, sAccountId: Int): SimpleContact? = contactMapperExt.findById(contactId)

    override fun removeContactOpportunityRelationship(associateOpportunity: ContactOpportunity, sAccountId: Int) {
        val ex = ContactOpportunityExample()
        ex.createCriteria().andContactidEqualTo(associateOpportunity.contactid)
                .andOpportunityidEqualTo(associateOpportunity.opportunityid)
        contactOpportunityMapper.deleteByExample(ex)
    }

    override fun saveContactOpportunityRelationship(associateOpportunities: List<ContactOpportunity>, sAccountId: Int) {
        for (assoOpportunity in associateOpportunities) {
            val ex = ContactOpportunityExample()
            ex.createCriteria()
                    .andContactidEqualTo(assoOpportunity.contactid)
                    .andOpportunityidEqualTo(assoOpportunity.opportunityid)
            if (contactOpportunityMapper.countByExample(ex) == 0L) {
                assoOpportunity.createdtime = GregorianCalendar().time
                contactOpportunityMapper.insert(assoOpportunity)
            } else {
                contactOpportunityMapper.updateByExampleSelective(assoOpportunity, ex)
            }
        }
    }

    override fun saveContactCaseRelationship(associateCases: List<ContactCase>, sAccountId: Int) {
        for (associateCase in associateCases) {
            val ex = ContactCaseExample()
            ex.createCriteria()
                    .andContactidEqualTo(associateCase.contactid)
                    .andCaseidEqualTo(associateCase.caseid)
            if (contactCaseMapper.countByExample(ex) == 0L) {
                associateCase.createdtime = GregorianCalendar().time
                contactCaseMapper.insert(associateCase)
            }
        }
    }

    override fun removeContactCaseRelationship(associateCase: ContactCase, sAccountId: Int) {
        val ex = ContactCaseExample()
        ex.createCriteria().andContactidEqualTo(associateCase.contactid).andCaseidEqualTo(associateCase.caseid)
        contactCaseMapper.deleteByExample(ex)
    }

    override fun saveContactLeadRelationship(associateLeads: List<ContactLead>, @CacheKey sAccountId: Int) {
        for (associateLead in associateLeads) {
            val ex = ContactLeadExample()
            ex.createCriteria()
                    .andContactidEqualTo(associateLead.contactid)
                    .andLeadidEqualTo(associateLead.leadid)
            if (contactLeadMapper.countByExample(ex) == 0L) {
                contactLeadMapper.insert(associateLead)
            }
        }
    }

    override fun findContactAssoWithConvertedLead(leadId: Int, @CacheKey sAccountId: Int): SimpleContact? =
            contactMapperExt.findContactAssoWithConvertedLead(leadId)

    override fun saveWithSession(record: Contact, username: String?): Int {
        val result = super.saveWithSession(record, username)
        if (record.extraData != null && record.extraData is SimpleCampaign) {
            val associateContact = CampaignContact()
            associateContact.campaignid = (record.extraData as SimpleCampaign).id
            associateContact.contactid = record.id
            associateContact.createdtime = GregorianCalendar().time

            val campaignService = AppContextUtil.getSpringBean(CampaignService::class.java)
            campaignService.saveCampaignContactRelationship(listOf(associateContact), record.saccountid)
        } else if (record.extraData != null && record.extraData is SimpleOpportunity) {
            val associateContact = ContactOpportunity()
            associateContact.contactid = record.id
            associateContact.opportunityid = (record.extraData as SimpleOpportunity).id
            associateContact.createdtime = GregorianCalendar().time

            this.saveContactOpportunityRelationship(listOf(associateContact), record.saccountid)
        } else if (record.extraData != null && record.extraData is SimpleCase) {
            val associateCase = ContactCase()
            associateCase.contactid = record.id
            associateCase.caseid = (record.extraData as SimpleCase).id
            associateCase.createdtime = GregorianCalendar().time

            this.saveContactCaseRelationship(listOf(associateCase), record.saccountid)
        }
        return result
    }

    companion object {
        init {
            ClassInfoMap.put(ContactServiceImpl::class.java, ClassInfo(ModuleNameConstants.CRM, CrmTypeConstants.CONTACT))
        }
    }
}
