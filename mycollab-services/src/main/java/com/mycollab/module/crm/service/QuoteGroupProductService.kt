package com.mycollab.module.crm.service

import com.mycollab.module.crm.domain.QuoteGroupProduct

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface QuoteGroupProductService {
    fun findQuoteGroupByQuoteId(quoteid: Int): List<QuoteGroupProduct>

    fun deleteQuoteGroupByQuoteId(quoteid: Int)

    fun insertQuoteGroupExt(record: QuoteGroupProduct): Int
}
