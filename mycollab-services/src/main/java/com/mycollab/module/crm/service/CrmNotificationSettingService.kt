package com.mycollab.module.crm.service

import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.ICrudService
import com.mycollab.module.crm.domain.CrmNotificationSetting

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface CrmNotificationSettingService : ICrudService<Int, CrmNotificationSetting> {
    @Cacheable
    fun findNotification(username: String, @CacheKey sAccountId: Int?): CrmNotificationSetting

    @Cacheable
    fun findNotifications(@CacheKey sAccountId: Int?): List<CrmNotificationSetting>
}
