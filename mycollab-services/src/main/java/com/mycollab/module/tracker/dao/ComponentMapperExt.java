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
package com.mycollab.module.tracker.dao;

import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.module.tracker.domain.SimpleComponent;
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
import org.apache.ibatis.annotations.Param;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
public interface ComponentMapperExt extends ISearchableDAO<ComponentSearchCriteria> {

    SimpleComponent findComponentById(int componentId);

    Double getTotalBillableHours(@Param("componentid") int componentId);

    Double getTotalNonBillableHours(@Param("componentid") int componentId);

    Double getRemainHours(@Param("componentid") int componentId);
}
