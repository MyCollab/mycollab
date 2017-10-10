/**
 * mycollab-services - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
