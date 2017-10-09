package com.mycollab.module.crm.dao

import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.crm.domain.criteria.ProductSearchCriteria

interface ProductMapperExt : ISearchableDAO<ProductSearchCriteria>
