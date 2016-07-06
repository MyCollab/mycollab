/**
 * This file is part of mycollab-dao.
 *
 * mycollab-dao is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-dao is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-dao.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.db.persistence;

import com.mycollab.db.arguments.SearchCriteria;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 *
 * @param <R>
 * @param <S>
 */
public interface IMassUpdateDAO<R, S extends SearchCriteria> {
    void updateBySearchCriteria(@Param("record") R record, @Param("searchCriteria") S searchCriteria);
}
