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

import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultSearchService;
import com.esofthead.mycollab.module.project.dao.ProjectGenericItemMapper;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericItemSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectGenericItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
@Service
public class ProjectGenericItemServiceImpl extends DefaultSearchService<ProjectGenericItemSearchCriteria> implements ProjectGenericItemService {

    @Autowired
    private ProjectGenericItemMapper projectGenericItemMapper;

    @Override
    public ISearchableDAO<ProjectGenericItemSearchCriteria> getSearchMapper() {
        return projectGenericItemMapper;
    }

    @Override
    public Integer getTotalCount(ProjectGenericItemSearchCriteria criteria) {
        return projectGenericItemMapper.getTotalCountFromTask(criteria) +
                projectGenericItemMapper.getTotalCountFromMessage(criteria) +
                projectGenericItemMapper.getTotalCountFromMilestone(criteria) +
                projectGenericItemMapper.getTotalCountFromBug(criteria) +
                projectGenericItemMapper.getTotalCountFromVersion(criteria) +
                projectGenericItemMapper.getTotalCountFromComponent(criteria) +
                projectGenericItemMapper.getTotalCountFromRisk(criteria);
    }
}
