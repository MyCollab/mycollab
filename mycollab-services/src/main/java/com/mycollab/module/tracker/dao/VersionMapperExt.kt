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
package com.mycollab.module.tracker.dao

import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.tracker.domain.SimpleVersion
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria
import org.apache.ibatis.annotations.Param

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
interface VersionMapperExt : ISearchableDAO<VersionSearchCriteria> {

    fun findVersionById(versionId: Int): SimpleVersion?

    fun getTotalBillableHours(@Param("versionid") versionId: Int): Double

    fun getTotalNonBillableHours(@Param("versionid") versionId: Int): Double

    fun getRemainHours(@Param("versionid") versionId: Int): Double
}
