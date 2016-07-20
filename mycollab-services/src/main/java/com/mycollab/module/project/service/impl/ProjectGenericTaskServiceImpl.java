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
package com.mycollab.module.project.service.impl;

import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.db.persistence.service.DefaultSearchService;
import com.mycollab.module.project.dao.ProjectGenericTaskMapper;
import com.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.mycollab.module.project.service.ProjectGenericTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
public class ProjectGenericTaskServiceImpl extends DefaultSearchService<ProjectGenericTaskSearchCriteria> implements ProjectGenericTaskService {

    @Autowired
    private ProjectGenericTaskMapper projectGenericTaskMapper;

    @Override
    public ISearchableDAO<ProjectGenericTaskSearchCriteria> getSearchMapper() {
        return projectGenericTaskMapper;
    }

    @Override
    public Integer getTotalCount(ProjectGenericTaskSearchCriteria criteria) {
        return projectGenericTaskMapper.getTotalCountFromRisk(criteria)
                + projectGenericTaskMapper.getTotalCountFromBug(criteria)
                + projectGenericTaskMapper.getTotalCountFromTask(criteria)
                + projectGenericTaskMapper.getTotalCountFromMilestone(criteria);
    }

    @Override
    public List<Map> getAccountsHasOverdueAssignments(ProjectGenericTaskSearchCriteria searchCriteria) {
        return projectGenericTaskMapper.getAccountsHasOverdueAssignments(searchCriteria);
    }

    @Override
    public List<Integer> getProjectsHasOverdueAssignments(ProjectGenericTaskSearchCriteria searchCriteria) {
        return projectGenericTaskMapper.getProjectsHasOverdueAssignments(searchCriteria);
    }
}
