package com.mycollab.module.crm.service.impl

import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.service.DefaultCrudService
import com.mycollab.module.crm.dao.QuoteGroupProductMapper
import com.mycollab.module.crm.dao.QuoteGroupProductMapperExt
import com.mycollab.module.crm.domain.QuoteGroupProduct
import com.mycollab.module.crm.domain.QuoteGroupProductExample
import com.mycollab.module.crm.service.QuoteGroupProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class QuoteGroupServiceImpl : DefaultCrudService<Int, QuoteGroupProduct>(), QuoteGroupProductService {

    @Autowired
    private val quoteGroupProductMapper: QuoteGroupProductMapper? = null

    @Autowired
    private val quoteGroupProductMapperExt: QuoteGroupProductMapperExt? = null

    override val crudMapper: ICrudGenericDAO<Int, QuoteGroupProduct>
        get() = quoteGroupProductMapper as ICrudGenericDAO<Int, QuoteGroupProduct>

    override fun findQuoteGroupByQuoteId(quoteid: Int): List<QuoteGroupProduct> {
        val ex = QuoteGroupProductExample()
        ex.createCriteria().andQuoteidEqualTo(quoteid)
        return quoteGroupProductMapper!!.selectByExample(ex)
    }

    override fun deleteQuoteGroupByQuoteId(quoteid: Int) {
        val ex = QuoteGroupProductExample()
        ex.createCriteria().andQuoteidEqualTo(quoteid)
        quoteGroupProductMapper!!.deleteByExample(ex)
    }

    override fun insertQuoteGroupExt(record: QuoteGroupProduct): Int {
        quoteGroupProductMapperExt!!.insertQuoteGroupExt(record)
        return record.id!!
    }

}
