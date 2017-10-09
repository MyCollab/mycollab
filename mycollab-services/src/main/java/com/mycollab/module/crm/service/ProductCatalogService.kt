package com.mycollab.module.crm.service

import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.crm.domain.ProductCatalog
import com.mycollab.module.crm.domain.criteria.ProductCatalogSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface ProductCatalogService : IDefaultService<Int, ProductCatalog, ProductCatalogSearchCriteria>
