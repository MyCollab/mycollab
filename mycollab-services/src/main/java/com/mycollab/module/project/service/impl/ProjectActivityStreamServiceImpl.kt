package com.mycollab.module.project.service.impl

import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria
import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.module.project.dao.ProjectMapperExt
import com.mycollab.module.project.domain.ProjectActivityStream
import com.mycollab.module.project.service.ProjectActivityStreamService
import org.apache.ibatis.session.RowBounds
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProjectActivityStreamServiceImpl(private val projectMapperExt: ProjectMapperExt) : ProjectActivityStreamService {

    override fun getTotalActivityStream(criteria: ActivityStreamSearchCriteria): Int {
        return projectMapperExt.getTotalActivityStream(criteria)
    }

    override fun getProjectActivityStreams(searchRequest: BasicSearchRequest<ActivityStreamSearchCriteria>): List<ProjectActivityStream> {
        return projectMapperExt.getProjectActivityStreams(searchRequest.searchCriteria,
                RowBounds((searchRequest.currentPage - 1) * searchRequest.numberOfItems,
                        searchRequest.numberOfItems))
    }
}
