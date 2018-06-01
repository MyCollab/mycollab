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
import com.mycollab.module.crm.dao.AccountLeadMapper
import com.mycollab.module.crm.dao.AccountMapper
import com.mycollab.module.crm.dao.AccountMapperExt
import com.mycollab.module.crm.domain.*
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria
import com.mycollab.module.crm.service.AccountService
import com.mycollab.module.crm.service.CampaignService
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
@Traceable(nameField = "accountname")
@Watchable(userFieldName = "assignuser")
open class AccountServiceImpl(private val accountMapper: AccountMapper,
                         private val accountMapperExt: AccountMapperExt,
                         private val accountLeadMapper: AccountLeadMapper) : DefaultService<Int, Account, AccountSearchCriteria>(), AccountService {

    override val crudMapper: ICrudGenericDAO<Int, Account>
        get() = accountMapper as ICrudGenericDAO<Int, Account>

    override val searchMapper: ISearchableDAO<AccountSearchCriteria>
        get() = accountMapperExt

    override fun findById(id: Int, accountId: Int): SimpleAccount? = accountMapperExt.findById(id)

    override fun saveWithSession(record: Account, username: String?): Int {
        val result = super.saveWithSession(record, username)

        if (record.extraData != null && record.extraData is SimpleCampaign) {
            val assoAccount = CampaignAccount()
            assoAccount.accountid = record.id
            assoAccount.campaignid = (record.extraData as SimpleCampaign).id
            assoAccount.createdtime = GregorianCalendar().time

            val campaignService = AppContextUtil.getSpringBean(CampaignService::class.java)
            campaignService.saveCampaignAccountRelationship(listOf(assoAccount), record.saccountid)
        }
        return result
    }

    override fun saveAccountLeadRelationship(associateLeads: List<AccountLead>, accountId: Int) {
        for (associateLead in associateLeads) {
            val ex = AccountLeadExample()
            ex.createCriteria().andAccountidEqualTo(associateLead.accountid)
                    .andLeadidEqualTo(associateLead.leadid)
            if (accountLeadMapper.countByExample(ex) == 0L) {
                associateLead.createtime = GregorianCalendar().time
                accountLeadMapper.insert(associateLead)
            }
        }
    }

    override fun removeAccountLeadRelationship(associateLead: AccountLead, accountId: Int) {
        val ex = AccountLeadExample()
        ex.createCriteria().andAccountidEqualTo(associateLead.accountid).andLeadidEqualTo(associateLead.leadid)
        accountLeadMapper.deleteByExample(ex)
    }

    override fun findAccountAssoWithConvertedLead(leadId: Int, accountId: Int): SimpleAccount? =
            accountMapperExt.findAccountAssoWithConvertedLead(leadId)

    companion object {
        init {
            ClassInfoMap.put(AccountServiceImpl::class.java, ClassInfo(ModuleNameConstants.CRM, CrmTypeConstants.ACCOUNT))
        }
    }

}
