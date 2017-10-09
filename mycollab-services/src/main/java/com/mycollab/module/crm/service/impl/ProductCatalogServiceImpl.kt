package com.mycollab.module.crm.service.impl

import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.crm.dao.ProductCatalogMapper
import com.mycollab.module.crm.dao.ProductCatalogMapperExt
import com.mycollab.module.crm.domain.ProductCatalog
import com.mycollab.module.crm.domain.criteria.ProductCatalogSearchCriteria
import com.mycollab.module.crm.service.ProductCatalogService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductCatalogServiceImpl(private val productCatalogMapper: ProductCatalogMapper,
                                private val productCatalogMapperExt: ProductCatalogMapperExt) : DefaultService<Int, ProductCatalog, ProductCatalogSearchCriteria>(), ProductCatalogService {

    override val crudMapper: ICrudGenericDAO<Int, ProductCatalog>
        get() = productCatalogMapper as ICrudGenericDAO<Int, ProductCatalog>

    override val searchMapper: ISearchableDAO<ProductCatalogSearchCriteria>
        get() = productCatalogMapperExt

}
