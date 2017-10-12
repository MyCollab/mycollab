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
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.crm.dao.*
import com.mycollab.module.crm.domain.*
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria
import com.mycollab.module.crm.service.CampaignService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(nameField = "campaignname")
@Watchable(userFieldName = "assignuser")
class CampaignServiceImpl(private val campaignMapper: CampaignMapper,
                          private val campaignMapperExt: CampaignMapperExt,
                          private val campaignAccountMapper: CampaignAccountMapper,
                          private val campaignContactMapper: CampaignContactMapper,
                          private val campaignLeadMapper: CampaignLeadMapper) : DefaultService<Int, CampaignWithBLOBs, CampaignSearchCriteria>(), CampaignService {

    override val crudMapper: ICrudGenericDAO<Int, CampaignWithBLOBs>
        get() = campaignMapper as ICrudGenericDAO<Int, CampaignWithBLOBs>

    override val searchMapper: ISearchableDAO<CampaignSearchCriteria>
        get() = campaignMapperExt

    override fun findById(campaignId: Int, sAccountUd: Int): SimpleCampaign? {
        return campaignMapperExt.findById(campaignId)
    }

    override fun saveWithSession(campaign: CampaignWithBLOBs, username: String?): Int {
        val result = super.saveWithSession(campaign, username)
        if (campaign.extraData != null && campaign.extraData is SimpleLead) {
            val associateLead = CampaignLead()
            associateLead.campaignid = campaign.id
            associateLead.leadid = (campaign.extraData as SimpleLead).id
            associateLead.createdtime = GregorianCalendar().time
            this.saveCampaignLeadRelationship(listOf(associateLead), campaign.saccountid)
        }
        return result
    }

    override fun saveCampaignAccountRelationship(associateAccounts: List<CampaignAccount>, sAccountId: Int?) {
        for (associateAccount in associateAccounts) {
            val ex = CampaignAccountExample()
            ex.createCriteria()
                    .andAccountidEqualTo(associateAccount.accountid)
                    .andCampaignidEqualTo(associateAccount.campaignid)
            if (campaignAccountMapper.countByExample(ex) == 0L) {
                campaignAccountMapper.insert(associateAccount)
            }
        }
    }

    override fun removeCampaignAccountRelationship(associateAccount: CampaignAccount, sAccountId: Int?) {
        val ex = CampaignAccountExample()
        ex.createCriteria()
                .andAccountidEqualTo(associateAccount.accountid)
                .andCampaignidEqualTo(associateAccount.campaignid)
        campaignAccountMapper.deleteByExample(ex)
    }

    override fun saveCampaignContactRelationship(associateContacts: List<CampaignContact>, sAccountId: Int?) {
        for (associateContact in associateContacts) {
            val ex = CampaignContactExample()
            ex.createCriteria()
                    .andCampaignidEqualTo(associateContact.campaignid)
                    .andContactidEqualTo(associateContact.contactid)
            if (campaignContactMapper.countByExample(ex) == 0L) {
                campaignContactMapper.insert(associateContact)
            }
        }
    }

    override fun removeCampaignContactRelationship(associateContact: CampaignContact, sAccountId: Int?) {
        val ex = CampaignContactExample()
        ex.createCriteria()
                .andCampaignidEqualTo(associateContact.campaignid)
                .andContactidEqualTo(associateContact.contactid)
        campaignContactMapper.deleteByExample(ex)
    }

    override fun saveCampaignLeadRelationship(associateLeads: List<CampaignLead>, sAccountId: Int?) {
        for (associateLead in associateLeads) {
            val ex = CampaignLeadExample()
            ex.createCriteria()
                    .andCampaignidEqualTo(associateLead.campaignid)
                    .andLeadidEqualTo(associateLead.leadid)
            if (campaignLeadMapper.countByExample(ex) == 0L) {
                campaignLeadMapper.insert(associateLead)
            }
        }
    }

    override fun removeCampaignLeadRelationship(associateLead: CampaignLead, sAccountId: Int?) {
        val ex = CampaignLeadExample()
        ex.createCriteria().andCampaignidEqualTo(associateLead.campaignid)
                .andLeadidEqualTo(associateLead.leadid)
        campaignLeadMapper.deleteByExample(ex)
    }

    companion object {

        init {
            ClassInfoMap.put(CampaignServiceImpl::class.java, ClassInfo(ModuleNameConstants.CRM, CrmTypeConstants.CAMPAIGN))
        }
    }
}
