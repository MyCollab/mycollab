package com.mycollab.module.project.service

import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.project.domain.Invoice
import com.mycollab.module.project.domain.SimpleInvoice
import com.mycollab.module.project.domain.criteria.InvoiceSearchCriteria

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
interface InvoiceService : IDefaultService<Int, Invoice, InvoiceSearchCriteria> {
    @Cacheable
    fun findById(invoiceId: Int, @CacheKey sAccountId: Int): SimpleInvoice?

}
