package com.mycollab.module.crm.dao

import com.mycollab.db.persistence.IMassUpdateDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.crm.domain.Account
import com.mycollab.module.crm.domain.SimpleAccount
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria
import org.apache.ibatis.annotations.Param

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface AccountMapperExt : ISearchableDAO<AccountSearchCriteria>, IMassUpdateDAO<Account, AccountSearchCriteria> {

    fun findById(accountId: Int?): SimpleAccount

    fun findAccountAssoWithConvertedLead(@Param("leadId") leadId: Int?): SimpleAccount
}
