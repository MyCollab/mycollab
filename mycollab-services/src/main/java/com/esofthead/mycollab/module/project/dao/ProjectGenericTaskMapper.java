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

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTaskCount;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ProjectGenericTaskMapper extends
		ISearchableDAO<ProjectGenericTaskSearchCriteria> {
	int getTotalCountFromProblem(
			@Param("searchCriteria") ProjectGenericTaskSearchCriteria criteria);

	int getTotalCountFromRisk(
			@Param("searchCriteria") ProjectGenericTaskSearchCriteria criteria);

	int getTotalCountFromBug(
			@Param("searchCriteria") ProjectGenericTaskSearchCriteria criteria);

	int getTotalCountFromTask(
			@Param("searchCriteria") ProjectGenericTaskSearchCriteria criteria);

	List<ProjectGenericTaskCount> findPagableTaskCountListByCriteria(
			@Param("searchCriteria") ProjectGenericTaskSearchCriteria criteria,
			RowBounds rowBounds);
}
