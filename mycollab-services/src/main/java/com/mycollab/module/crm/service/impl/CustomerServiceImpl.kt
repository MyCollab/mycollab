package com.mycollab.module.crm.service.impl

import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.service.DefaultCrudService
import com.mycollab.module.crm.dao.CustomerMapper
import com.mycollab.module.crm.domain.Customer
import com.mycollab.module.crm.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CustomerServiceImpl : DefaultCrudService<Int, Customer>(), CustomerService {

    @Autowired
    private val customerMapper: CustomerMapper? = null

    override val crudMapper: ICrudGenericDAO<Int, Customer>
        get() = customerMapper as ICrudGenericDAO<Int, Customer>
}
