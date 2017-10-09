package com.mycollab.module.crm.service.impl

import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.crm.dao.TargetGroupMapper
import com.mycollab.module.crm.dao.TargetGroupMapperExt
import com.mycollab.module.crm.domain.TargetGroup
import com.mycollab.module.crm.domain.criteria.TargetGroupSearchCriteria
import com.mycollab.module.crm.service.TargetGroupService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TargetGroupServiceImpl(private val targetGroupMapper: TargetGroupMapper,
                             private val targetGroupMapperExt: TargetGroupMapperExt) : DefaultService<Int, TargetGroup, TargetGroupSearchCriteria>(), TargetGroupService {

    override val crudMapper: ICrudGenericDAO<Int, TargetGroup>
        get() = targetGroupMapper as ICrudGenericDAO<Int, TargetGroup>

    override val searchMapper: ISearchableDAO<TargetGroupSearchCriteria>
        get() = targetGroupMapperExt

}
