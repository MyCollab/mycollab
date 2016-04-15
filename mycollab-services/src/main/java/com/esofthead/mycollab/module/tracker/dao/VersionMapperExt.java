/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.esofthead.mycollab.module.tracker.dao;

import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.module.tracker.domain.SimpleVersion;
import com.esofthead.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import org.apache.ibatis.annotations.Param;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
public interface VersionMapperExt extends ISearchableDAO<VersionSearchCriteria> {

    SimpleVersion findVersionById(int versionId);

    Double getTotalBillableHours(@Param("versionid") int versionId);

    Double getTotalNonBillableHours(@Param("versionid") int versionId);

    Double getRemainHours(@Param("versionid") int versionId);
}
