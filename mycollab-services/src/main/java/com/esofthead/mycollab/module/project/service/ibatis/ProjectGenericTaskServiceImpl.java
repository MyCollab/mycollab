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

package com.esofthead.mycollab.module.project.service.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultSearchService;
import com.esofthead.mycollab.module.project.dao.ProjectGenericTaskMapper;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTaskCount;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
public class ProjectGenericTaskServiceImpl extends
		DefaultSearchService<ProjectGenericTaskSearchCriteria> implements
		ProjectGenericTaskService {

	@Autowired
	protected ProjectGenericTaskMapper projectGenericTaskMapper;

	@Override
	public ISearchableDAO<ProjectGenericTaskSearchCriteria> getSearchMapper() {
		return projectGenericTaskMapper;
	}

	@Override
	public int getTotalCount(ProjectGenericTaskSearchCriteria criteria) {
		return projectGenericTaskMapper.getTotalCountFromProblem(criteria)
				+ projectGenericTaskMapper.getTotalCountFromRisk(criteria)
				+ projectGenericTaskMapper.getTotalCountFromBug(criteria)
				+ projectGenericTaskMapper.getTotalCountFromTask(criteria);
	}

	@Override
	public List<ProjectGenericTaskCount> findPagableTaskCountListByCriteria(
			SearchRequest<ProjectGenericTaskSearchCriteria> searchRequest) {
		return projectGenericTaskMapper.findPagableTaskCountListByCriteria(
				searchRequest.getSearchCriteria(),
				new RowBounds((searchRequest.getCurrentPage() - 1)
						* searchRequest.getNumberOfItems(), searchRequest
						.getNumberOfItems()));
	}
}
