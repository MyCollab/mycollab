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
package com.esofthead.mycollab.module.project.dao;

import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericItemSearchCriteria;
import org.apache.ibatis.annotations.Param;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public interface ProjectGenericItemMapper extends ISearchableDAO<ProjectGenericItemSearchCriteria> {

    int getTotalCountFromRisk(@Param("searchCriteria") ProjectGenericItemSearchCriteria criteria);

    int getTotalCountFromBug(@Param("searchCriteria") ProjectGenericItemSearchCriteria criteria);

    int getTotalCountFromVersion(@Param("searchCriteria") ProjectGenericItemSearchCriteria criteria);

    int getTotalCountFromComponent(@Param("searchCriteria") ProjectGenericItemSearchCriteria criteria);

    int getTotalCountFromTask(@Param("searchCriteria") ProjectGenericItemSearchCriteria criteria);

    int getTotalCountFromMessage(@Param("searchCriteria") ProjectGenericItemSearchCriteria criteria);

    int getTotalCountFromMilestone(@Param("searchCriteria") ProjectGenericItemSearchCriteria criteria);
}