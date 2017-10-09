package com.mycollab.module.project.domain.criteria

import com.mycollab.common.domain.criteria.MonitorSearchCriteria
import com.mycollab.db.arguments.StringSearchField

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class FollowingTicketSearchCriteria : MonitorSearchCriteria() {
    var name: StringSearchField? = null
}