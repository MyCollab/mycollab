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
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ProjectGenericTaskMapper extends ISearchableDAO<ProjectGenericTaskSearchCriteria> {

    Integer getTotalCountFromRisk(@Param("searchCriteria") ProjectGenericTaskSearchCriteria criteria);

    Integer getTotalCountFromBug(@Param("searchCriteria") ProjectGenericTaskSearchCriteria criteria);

    Integer getTotalCountFromTask(@Param("searchCriteria") ProjectGenericTaskSearchCriteria criteria);

    Integer getTotalCountFromMilestone(@Param("searchCriteria") ProjectGenericTaskSearchCriteria criteria);

    List<Map> getAccountsHasOverdueAssignments(@Param("searchCriteria") ProjectGenericTaskSearchCriteria criteria);

    List<Integer> getProjectsHasOverdueAssignments(@Param("searchCriteria") ProjectGenericTaskSearchCriteria criteria);
}
