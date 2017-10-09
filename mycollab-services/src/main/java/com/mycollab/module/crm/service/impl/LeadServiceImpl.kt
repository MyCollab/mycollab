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
import com.mycollab.module.crm.dao.LeadMapper
import com.mycollab.module.crm.dao.LeadMapperExt
import com.mycollab.module.crm.domain.*
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria
import com.mycollab.module.crm.service.*
import com.mycollab.spring.AppContextUtil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
@Traceable(nameField = "lastname")
@Watchable(userFieldName = "assignuser")
class LeadServiceImpl(private val leadMapper: LeadMapper,
                      private val leadMapperExt: LeadMapperExt) : DefaultService<Int, Lead, LeadSearchCriteria>(), LeadService {

    override val crudMapper: ICrudGenericDAO<Int, Lead>
        get() = leadMapper as ICrudGenericDAO<Int, Lead>

    override val searchMapper: ISearchableDAO<LeadSearchCriteria>
        get() = leadMapperExt

    override fun findById(leadId: Int, sAccountId: Int): SimpleLead? {
        return leadMapperExt.findById(leadId)
    }

    override fun saveWithSession(lead: Lead, username: String?): Int {
        val result = super.saveWithSession(lead, username)
        if (lead.extraData != null && lead.extraData is SimpleCampaign) {
            val associateLead = CampaignLead()
            associateLead.campaignid = (lead.extraData as SimpleCampaign).id
            associateLead.leadid = lead.id
            associateLead.createdtime = GregorianCalendar().time

            val campaignService = AppContextUtil.getSpringBean(CampaignService::class.java)
            campaignService.saveCampaignLeadRelationship(listOf(associateLead), lead.saccountid)
        } else if (lead.extraData != null && lead.extraData is SimpleOpportunity) {
            val associateLead = OpportunityLead()
            associateLead.opportunityid = (lead.extraData as SimpleOpportunity).id
            associateLead.leadid = lead.id
            associateLead.createdtime = GregorianCalendar().time

            val opportunityService = AppContextUtil.getSpringBean(OpportunityService::class.java)
            opportunityService.saveOpportunityLeadRelationship(listOf(associateLead), lead.saccountid)
        }
        return result
    }

    override fun convertLead(lead: SimpleLead, opportunity: Opportunity?, convertUser: String) {
        LOG.debug("Create new account and save it")
        val account = Account()
        account.accountname = lead.accountname
        account.numemployees = lead.noemployees
        account.industry = lead.industry
        account.email = lead.email
        account.phoneoffice = lead.officephone
        account.fax = lead.fax
        account.website = lead.website
        account.assignuser = lead.assignuser
        account.description = lead.description
        account.saccountid = lead.saccountid

        val accountService = AppContextUtil.getSpringBean(AccountService::class.java)
        val accountId = accountService.saveWithSession(account, convertUser)

        LOG.debug("Create account lead relationship")
        val accLead = AccountLead()
        accLead.accountid = accountId
        accLead.leadid = lead.id
        accLead.isconvertrel = true

        accountService.saveAccountLeadRelationship(listOf(accLead), lead.saccountid)

        LOG.debug("Create new contact and save it")
        val contact = Contact()
        contact.prefix = lead.prefixname
        contact.firstname = lead.firstname
        contact.lastname = lead.lastname
        contact.title = lead.title
        contact.department = lead.department
        contact.accountid = accountId
        contact.saccountid = lead.saccountid
        contact.leadsource = lead.source
        contact.assignuser = lead.assignuser
        contact.officephone = lead.officephone
        contact.email = lead.email
        contact.saccountid = lead.saccountid

        val contactService = AppContextUtil.getSpringBean(ContactService::class.java)
        val contactId = contactService.saveWithSession(contact, convertUser)
        LOG.debug("Create contact lead relationship")
        val contactLead = ContactLead()
        contactLead.contactid = contactId
        contactLead.leadid = lead.id
        contactLead.isconvertrel = true
        contactService.saveContactLeadRelationship(listOf(contactLead), lead.saccountid)

        if (opportunity != null) {
            opportunity.accountid = accountId
            opportunity.saccountid = lead.saccountid
            val opportunityService = AppContextUtil.getSpringBean(OpportunityService::class.java)
            val opportunityId = opportunityService.saveWithSession(opportunity, convertUser)

            LOG.debug("Create new opportunity contact relationship")
            val oppContact = ContactOpportunity()
            oppContact.contactid = contactId
            oppContact.opportunityid = opportunityId
            contactService.saveContactOpportunityRelationship(listOf(oppContact), lead.saccountid)

            LOG.debug("Create new opportunity lead relationship")
            val oppLead = OpportunityLead()
            oppLead.leadid = lead.id
            oppLead.opportunityid = opportunityId
            oppLead.isconvertrel = true
            oppLead.createdtime = GregorianCalendar().time
            opportunityService.saveOpportunityLeadRelationship(listOf(oppLead), lead.saccountid)
        }

    }

    override fun findConvertedLeadOfAccount(accountId: Int?, @CacheKey sAccountId: Int?): SimpleLead {
        return leadMapperExt.findConvertedLeadOfAccount(accountId)
    }

    override fun findConvertedLeadOfContact(contactId: Int?, @CacheKey sAccountId: Int?): SimpleLead {
        return leadMapperExt.findConvertedLeadOfContact(contactId)
    }

    override fun findConvertedLeadOfOpportunity(opportunity: Int?, @CacheKey sAccountId: Int?): SimpleLead {
        return leadMapperExt.findConvertedLeadOfOpportunity(opportunity)
    }

    companion object {
        init {
            ClassInfoMap.put(LeadServiceImpl::class.java, ClassInfo(ModuleNameConstants.CRM, CrmTypeConstants.LEAD))
        }

        private val LOG = LoggerFactory.getLogger(LeadServiceImpl::class.java)
    }
}
