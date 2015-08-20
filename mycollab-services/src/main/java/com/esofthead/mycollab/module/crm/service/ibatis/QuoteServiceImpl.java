/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.crm.service.ibatis;

import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultService;
import com.esofthead.mycollab.module.crm.dao.ProductMapper;
import com.esofthead.mycollab.module.crm.dao.QuoteMapper;
import com.esofthead.mycollab.module.crm.dao.QuoteMapperExt;
import com.esofthead.mycollab.module.crm.domain.*;
import com.esofthead.mycollab.module.crm.domain.criteria.QuoteSearchCriteria;
import com.esofthead.mycollab.module.crm.service.QuoteGroupProductService;
import com.esofthead.mycollab.module.crm.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class QuoteServiceImpl extends DefaultService<Integer, Quote, QuoteSearchCriteria> implements
        QuoteService {

    @Autowired
    private QuoteMapper quoteMapper;
    @Autowired
    private QuoteMapperExt quoteMapperExt;
    @Autowired
    private QuoteGroupProductService quoteGroupProductService;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public ICrudGenericDAO<Integer, Quote> getCrudMapper() {
        return quoteMapper;
    }

    @Override
    public ISearchableDAO<QuoteSearchCriteria> getSearchMapper() {
        return quoteMapperExt;
    }

    public void setProductDAO(ProductMapper productDAO) {
        this.productMapper = productDAO;
    }

    @Override
    public void saveSimpleQuoteGroupProducts(int accountid, int quoteId,
                                             List<SimpleQuoteGroupProduct> entity) {
        quoteGroupProductService.deleteQuoteGroupByQuoteId(quoteId);

        for (SimpleQuoteGroupProduct simpleQuoteGroupProduct : entity) {
            QuoteGroupProduct quoteGroupProduct = simpleQuoteGroupProduct
                    .getQuoteGroupProduct();
            quoteGroupProductService.insertQuoteGroupExt(quoteGroupProduct);

            for (Product quoteProduct : simpleQuoteGroupProduct
                    .getQuoteProducts()) {
                // quoteProduct.setAccountid(accountid);
                quoteProduct.setGroupid(quoteGroupProduct.getId());
                quoteProduct.setStatus("Quoted");
                productMapper.insert(quoteProduct);
            }
        }
    }

    @Override
    public List<SimpleQuoteGroupProduct> getListSimpleQuoteGroupProducts(
            int quoteId) {
        List<QuoteGroupProduct> quoteGroupProducts = quoteGroupProductService
                .findQuoteGroupByQuoteId(quoteId);

        List<SimpleQuoteGroupProduct> result = new ArrayList<SimpleQuoteGroupProduct>();
        for (QuoteGroupProduct quoteGroupProduct : quoteGroupProducts) {
            SimpleQuoteGroupProduct simpleQuoteGroupProduct = new SimpleQuoteGroupProduct();
            simpleQuoteGroupProduct.setQuoteGroupProduct(quoteGroupProduct);

            ProductExample quoteProductEx = new ProductExample();
            quoteProductEx.createCriteria().andGroupidEqualTo(
                    quoteGroupProduct.getId());
            List<Product> quoteProducts = productMapper
                    .selectByExample(quoteProductEx);
            simpleQuoteGroupProduct.setQuoteProducts(quoteProducts);
            result.add(simpleQuoteGroupProduct);
        }
        return result;
    }
}
