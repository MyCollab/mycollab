package com.mycollab.common.dao

import com.mycollab.common.domain.Client
import com.mycollab.common.domain.SimpleClient
import com.mycollab.common.domain.criteria.ClientSearchCriteria
import com.mycollab.db.persistence.IMassUpdateDAO
import com.mycollab.db.persistence.ISearchableDAO

interface ClientMapperExt : ISearchableDAO<ClientSearchCriteria>, IMassUpdateDAO<Client, ClientSearchCriteria> {

    fun findById(accountId: Int): SimpleClient?
}