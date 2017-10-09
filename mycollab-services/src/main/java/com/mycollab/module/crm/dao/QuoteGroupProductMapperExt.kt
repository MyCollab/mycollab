package com.mycollab.module.crm.dao

import com.mycollab.module.crm.domain.QuoteGroupProduct

interface QuoteGroupProductMapperExt {
    fun insertQuoteGroupExt(record: QuoteGroupProduct): Int
}
