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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.module.tracker.service.impl

import com.mycollab.aspect.ClassInfo
import com.mycollab.aspect.ClassInfoMap
import com.mycollab.aspect.Traceable
import com.mycollab.common.ModuleNameConstants
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultService
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.tracker.dao.VersionMapper
import com.mycollab.module.tracker.dao.VersionMapperExt
import com.mycollab.module.tracker.domain.SimpleVersion
import com.mycollab.module.tracker.domain.Version
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria
import com.mycollab.module.tracker.service.VersionService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
@Service
@Transactional
@Traceable(nameField = "name", extraFieldName = "projectid")
open class VersionServiceImpl(private val versionMapper: VersionMapper,
                         private val versionMapperExt: VersionMapperExt) : DefaultService<Int, Version, VersionSearchCriteria>(), VersionService {

    override val crudMapper: ICrudGenericDAO<Int, Version>
        get() = versionMapper as ICrudGenericDAO<Int, Version>

    override val searchMapper: ISearchableDAO<VersionSearchCriteria>
        get() = versionMapperExt

    override fun findById(versionId: Int, sAccountId: Int): SimpleVersion? =
            versionMapperExt.findVersionById(versionId)

    companion object {
        init {
            ClassInfoMap.put(VersionServiceImpl::class.java, ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.BUG_VERSION))
        }
    }
}
