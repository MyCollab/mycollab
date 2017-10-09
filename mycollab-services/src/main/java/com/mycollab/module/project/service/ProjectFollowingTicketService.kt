package com.mycollab.module.project.service

import com.mycollab.db.persistence.service.ISearchableService
import com.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface ProjectFollowingTicketService : ISearchableService<FollowingTicketSearchCriteria>
