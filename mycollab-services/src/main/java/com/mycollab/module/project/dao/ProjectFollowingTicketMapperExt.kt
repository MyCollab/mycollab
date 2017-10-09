package com.mycollab.module.project.dao

import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface ProjectFollowingTicketMapperExt : ISearchableDAO<FollowingTicketSearchCriteria>
