package com.mycollab.module.crm.service.impl

import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.crm.dao.ContractMapper
import com.mycollab.module.crm.dao.ContractMapperExt
import com.mycollab.module.crm.domain.Contract
import com.mycollab.module.crm.domain.criteria.ContractSearchCriteria
import com.mycollab.module.crm.service.ContractService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ContractServiceImpl(private val contractMapper: ContractMapper,
                          private val contractMapperExt: ContractMapperExt) : DefaultService<Int, Contract, ContractSearchCriteria>(), ContractService {

    override val crudMapper: ICrudGenericDAO<Int, Contract>
        get() = contractMapper as ICrudGenericDAO<Int, Contract>

    override val searchMapper: ISearchableDAO<ContractSearchCriteria>
        get() = contractMapperExt
}
