package com.mycollab.module.crm.service.impl

import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.crm.dao.ProductMapper
import com.mycollab.module.crm.dao.QuoteMapper
import com.mycollab.module.crm.dao.QuoteMapperExt
import com.mycollab.module.crm.domain.ProductExample
import com.mycollab.module.crm.domain.Quote
import com.mycollab.module.crm.domain.SimpleQuoteGroupProduct
import com.mycollab.module.crm.domain.criteria.QuoteSearchCriteria
import com.mycollab.module.crm.service.QuoteGroupProductService
import com.mycollab.module.crm.service.QuoteService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class QuoteServiceImpl(private val quoteMapper: QuoteMapper,
                       private val quoteMapperExt: QuoteMapperExt,
                       private val quoteGroupProductService: QuoteGroupProductService,
                       private var productMapper: ProductMapper) : DefaultService<Int, Quote, QuoteSearchCriteria>(), QuoteService {

    override val crudMapper: ICrudGenericDAO<Int, Quote>
        get() = quoteMapper as ICrudGenericDAO<Int, Quote>

    override val searchMapper: ISearchableDAO<QuoteSearchCriteria>
        get() = quoteMapperExt

    fun setProductDAO(productDAO: ProductMapper) {
        this.productMapper = productDAO
    }

    override fun saveSimpleQuoteGroupProducts(accountid: Int, quoteId: Int, entity: List<SimpleQuoteGroupProduct>) {
        quoteGroupProductService.deleteQuoteGroupByQuoteId(quoteId)

        for (simpleQuoteGroupProduct in entity) {
            val quoteGroupProduct = simpleQuoteGroupProduct.quoteGroupProduct
            quoteGroupProductService.insertQuoteGroupExt(quoteGroupProduct)

            for (quoteProduct in simpleQuoteGroupProduct.quoteProducts) {
                // quoteProduct.setAccountid(accountid);
                quoteProduct.groupid = quoteGroupProduct.id
                quoteProduct.status = "Quoted"
                productMapper.insert(quoteProduct)
            }
        }
    }

    override fun getListSimpleQuoteGroupProducts(quoteId: Int): List<SimpleQuoteGroupProduct> {
        val quoteGroupProducts = quoteGroupProductService.findQuoteGroupByQuoteId(quoteId)

        val result = ArrayList<SimpleQuoteGroupProduct>()
        for (quoteGroupProduct in quoteGroupProducts) {
            val simpleQuoteGroupProduct = SimpleQuoteGroupProduct()
            simpleQuoteGroupProduct.quoteGroupProduct = quoteGroupProduct

            val quoteProductEx = ProductExample()
            quoteProductEx.createCriteria().andGroupidEqualTo(quoteGroupProduct.id)
            val quoteProducts = productMapper.selectByExample(quoteProductEx)
            simpleQuoteGroupProduct.quoteProducts = quoteProducts
            result.add(simpleQuoteGroupProduct)
        }
        return result
    }
}
