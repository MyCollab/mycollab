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

    override fun saveSimpleQuoteGroupProducts(accountid: Int, quoteId: Int, entities: List<SimpleQuoteGroupProduct>) {
        quoteGroupProductService.deleteQuoteGroupByQuoteId(quoteId)

        for (simpleQuoteGroupProduct in entities) {
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
