package com.mycollab.module.crm.service.impl

import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.crm.dao.TargetMapper
import com.mycollab.module.crm.dao.TargetMapperExt
import com.mycollab.module.crm.domain.SimpleTarget
import com.mycollab.module.crm.domain.Target
import com.mycollab.module.crm.domain.criteria.TargetSearchCriteria
import com.mycollab.module.crm.service.TargetService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TargetServiceImpl(private val targetMapper: TargetMapper,
                        private val targetMapperExt: TargetMapperExt) : DefaultService<Int, Target, TargetSearchCriteria>(), TargetService {

    override val crudMapper: ICrudGenericDAO<Int, Target>
        get() = targetMapper as ICrudGenericDAO<Int, Target>

    override val searchMapper: ISearchableDAO<TargetSearchCriteria>
        get() = targetMapperExt

    override fun findTargetById(targetId: Int): SimpleTarget {
        return targetMapperExt.findTargetById(targetId)
    }

}
