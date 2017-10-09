package com.mycollab.module.crm.service.impl

import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.crm.dao.ProductMapper
import com.mycollab.module.crm.dao.ProductMapperExt
import com.mycollab.module.crm.domain.Product
import com.mycollab.module.crm.domain.criteria.ProductSearchCriteria
import com.mycollab.module.crm.service.ProductService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductServiceImpl(private val productMapper: ProductMapper,
                         private val productMapperExt: ProductMapperExt) : DefaultService<Int, Product, ProductSearchCriteria>(), ProductService {

    override val crudMapper: ICrudGenericDAO<Int, Product>
        get() = productMapper as ICrudGenericDAO<Int, Product>

    override val searchMapper: ISearchableDAO<ProductSearchCriteria>
        get() = productMapperExt
}
