package com.mycollab.common.service

import com.mycollab.cache.IgnoreCacheClass
import com.mycollab.common.domain.RelayEmailNotificationWithBLOBs
import com.mycollab.common.domain.criteria.RelayEmailNotificationSearchCriteria
import com.mycollab.db.persistence.service.IDefaultService

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@IgnoreCacheClass
interface RelayEmailNotificationService : IDefaultService<Int, RelayEmailNotificationWithBLOBs, RelayEmailNotificationSearchCriteria>
