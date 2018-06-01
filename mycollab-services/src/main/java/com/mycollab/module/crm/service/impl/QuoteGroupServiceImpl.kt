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
open class QuoteGroupServiceImpl : DefaultCrudService<Int, QuoteGroupProduct>(), QuoteGroupProductService {

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
