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
