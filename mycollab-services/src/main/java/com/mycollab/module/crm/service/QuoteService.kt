package com.mycollab.module.crm.service

import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.crm.domain.Quote
import com.mycollab.module.crm.domain.SimpleQuoteGroupProduct
import com.mycollab.module.crm.domain.criteria.QuoteSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface QuoteService : IDefaultService<Int, Quote, QuoteSearchCriteria> {

    fun saveSimpleQuoteGroupProducts(accountid: Int, quoteId: Int, entity: List<SimpleQuoteGroupProduct>)

    fun getListSimpleQuoteGroupProducts(quoteId: Int): List<SimpleQuoteGroupProduct>
}
