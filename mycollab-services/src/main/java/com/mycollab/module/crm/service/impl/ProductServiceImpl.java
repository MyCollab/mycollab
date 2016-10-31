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
package com.mycollab.module.crm.service.impl;

import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.db.persistence.service.DefaultService;
import com.mycollab.module.crm.dao.ProductMapper;
import com.mycollab.module.crm.dao.ProductMapperExt;
import com.mycollab.module.crm.domain.Product;
import com.mycollab.module.crm.domain.criteria.ProductSearchCriteria;
import com.mycollab.module.crm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductServiceImpl extends DefaultService<Integer, Product, ProductSearchCriteria> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductMapperExt productMapperExt;

    @Override
    public ICrudGenericDAO<Integer, Product> getCrudMapper() {
        return productMapper;
    }

    @Override
    public ISearchableDAO<ProductSearchCriteria> getSearchMapper() {
        return productMapperExt;
    }
}
