package com.mycollab.module.crm.service

import com.mycollab.db.persistence.service.ICrudService
import com.mycollab.module.crm.domain.Customer

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface CustomerService : ICrudService<Int, Customer>
