package com.mycollab.module.project.service.impl

import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultSearchService
import com.mycollab.module.project.dao.ProjectFollowingTicketMapperExt
import com.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria
import com.mycollab.module.project.service.ProjectFollowingTicketService
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
class ProjectFollowingTicketServiceImpl(private val projectFollowingTicketMapperExt: ProjectFollowingTicketMapperExt) : DefaultSearchService<FollowingTicketSearchCriteria>(), ProjectFollowingTicketService {

    override val searchMapper: ISearchableDAO<FollowingTicketSearchCriteria>
        get() = projectFollowingTicketMapperExt as ISearchableDAO<FollowingTicketSearchCriteria>

}
