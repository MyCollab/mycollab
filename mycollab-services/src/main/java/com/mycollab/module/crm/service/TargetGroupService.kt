package com.mycollab.module.crm.service

import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.crm.domain.TargetGroup
import com.mycollab.module.crm.domain.criteria.TargetGroupSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface TargetGroupService : IDefaultService<Int, TargetGroup, TargetGroupSearchCriteria>
