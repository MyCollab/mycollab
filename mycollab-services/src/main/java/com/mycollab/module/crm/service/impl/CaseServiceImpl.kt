package com.mycollab.module.crm.service.impl

import com.mycollab.aspect.ClassInfo
import com.mycollab.aspect.ClassInfoMap
import com.mycollab.aspect.Traceable
import com.mycollab.aspect.Watchable
import com.mycollab.common.ModuleNameConstants
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.crm.dao.CaseMapper
import com.mycollab.module.crm.dao.CaseMapperExt
import com.mycollab.module.crm.domain.CaseWithBLOBs
import com.mycollab.module.crm.domain.SimpleCase
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria
import com.mycollab.module.crm.service.CaseService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(nameField = "subject")
@Watchable(userFieldName = "assignuser")
class CaseServiceImpl(private val caseMapper: CaseMapper,
                      private val caseMapperExt: CaseMapperExt) : DefaultService<Int, CaseWithBLOBs, CaseSearchCriteria>(), CaseService {

    override val crudMapper: ICrudGenericDAO<Int, CaseWithBLOBs>
        get() = caseMapper as ICrudGenericDAO<Int, CaseWithBLOBs>

    override val searchMapper: ISearchableDAO<CaseSearchCriteria>
        get() = caseMapperExt

    override fun findById(caseId: Int?, sAccountId: Int?): SimpleCase {
        return caseMapperExt.findById(caseId)
    }

    companion object {
        init {
            ClassInfoMap.put(CaseServiceImpl::class.java, ClassInfo(ModuleNameConstants.CRM, CrmTypeConstants.CASE))
        }
    }
}
