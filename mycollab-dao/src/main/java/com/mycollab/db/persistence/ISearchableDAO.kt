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
package com.mycollab.db.persistence

import com.mycollab.db.arguments.SearchCriteria
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.session.RowBounds

/**
 * @param <S>
 * @author MyCollab Ltd.
 * @since 1.0
</S> */
interface ISearchableDAO<S : SearchCriteria> {

    /**
     * @param criteria
     * @return
     */
    fun getTotalCount(@Param("searchCriteria") criteria: S): Int

    /**
     * @param criteria
     * @param rowBounds
     * @return
     */
    fun findPageableListByCriteria(@Param("searchCriteria") criteria: S, rowBounds: RowBounds): List<*>

    /**
     * @param criteria
     * @return
     */
    fun getNextItemKey(@Param("searchCriteria") criteria: S): Int?

    /**
     * @param criteria
     * @return
     */
    fun getPreviousItemKey(@Param("searchCriteria") criteria: S): Int?

    /**
     * @param criteria
     */
    fun removeByCriteria(@Param("searchCriteria") criteria: S)
}
