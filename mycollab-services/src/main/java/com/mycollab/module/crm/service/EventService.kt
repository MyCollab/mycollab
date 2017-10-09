package com.mycollab.module.crm.service

import com.mycollab.db.persistence.service.ISearchableService
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface EventService : ISearchableService<ActivitySearchCriteria>
