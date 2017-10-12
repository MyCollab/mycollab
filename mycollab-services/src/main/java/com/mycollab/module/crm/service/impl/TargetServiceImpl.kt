/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
