package com.mycollab.common.service.impl

import com.mycollab.aspect.ClassInfo
import com.mycollab.aspect.ClassInfoMap
import com.mycollab.aspect.Traceable
import com.mycollab.common.ModuleNameConstants
import com.mycollab.common.dao.ClientMapper
import com.mycollab.common.dao.ClientMapperExt
import com.mycollab.common.domain.Client
import com.mycollab.common.domain.SimpleClient
import com.mycollab.common.domain.criteria.ClientSearchCriteria
import com.mycollab.common.service.ClientService
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.project.ProjectTypeConstants
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
@Traceable(nameField = "name")
class ClientServiceImpl(private val accountMapper: ClientMapper,
                        private val accountMapperExt: ClientMapperExt) : DefaultService<Int, Client, ClientSearchCriteria>(), ClientService {

    override val crudMapper: ICrudGenericDAO<Int, Client>
        get() = accountMapper as ICrudGenericDAO<Int, Client>

    override val searchMapper: ISearchableDAO<ClientSearchCriteria>
        get() = accountMapperExt

    override fun findById(id: Int, sAccountId: Int): SimpleClient? = accountMapperExt.findById(id)

    companion object {
        init {
            ClassInfoMap.put(ClientServiceImpl::class.java, ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.CLIENT))
        }
    }
}