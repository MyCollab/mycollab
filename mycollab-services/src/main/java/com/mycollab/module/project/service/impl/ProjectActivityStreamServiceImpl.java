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

import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.module.project.dao.ProjectMapperExt;
import com.mycollab.module.project.domain.ProjectActivityStream;
import com.mycollab.module.project.service.ProjectActivityStreamService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectActivityStreamServiceImpl implements ProjectActivityStreamService {

    @Autowired
    private ProjectMapperExt projectMapperExt;

    @Override
    public int getTotalActivityStream(ActivityStreamSearchCriteria criteria) {
        return projectMapperExt.getTotalActivityStream(criteria);
    }

    @Override
    public List<ProjectActivityStream> getProjectActivityStreams(BasicSearchRequest<ActivityStreamSearchCriteria> searchRequest) {
        return projectMapperExt.getProjectActivityStreams(searchRequest.getSearchCriteria(),
                new RowBounds((searchRequest.getCurrentPage() - 1) * searchRequest.getNumberOfItems(),
                        searchRequest.getNumberOfItems()));
    }
}
