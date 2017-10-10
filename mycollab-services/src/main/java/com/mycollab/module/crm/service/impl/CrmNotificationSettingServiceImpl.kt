/**
 * mycollab-services - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.service.impl

import com.mycollab.common.NotificationType
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.ICrudGenericDAO
import com.mycollab.db.persistence.service.DefaultCrudService
import com.mycollab.module.crm.dao.CrmNotificationSettingMapper
import com.mycollab.module.crm.domain.CrmNotificationSetting
import com.mycollab.module.crm.domain.CrmNotificationSettingExample
import com.mycollab.module.crm.service.CrmNotificationSettingService
import org.apache.commons.collections.CollectionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
class CrmNotificationSettingServiceImpl : DefaultCrudService<Int, CrmNotificationSetting>(), CrmNotificationSettingService {

    @Autowired
    private val crmNotificationSettingMapper: CrmNotificationSettingMapper? = null

    override val crudMapper: ICrudGenericDAO<Int, CrmNotificationSetting>
        get() = crmNotificationSettingMapper as ICrudGenericDAO<Int, CrmNotificationSetting>

    @Cacheable
    override fun findNotification(username: String, @CacheKey sAccountId: Int?): CrmNotificationSetting {
        val ex = CrmNotificationSettingExample()
        ex.createCriteria().andUsernameEqualTo(username).andSaccountidEqualTo(sAccountId)
        val notifications = crmNotificationSettingMapper!!.selectByExample(ex)
        if (CollectionUtils.isNotEmpty(notifications)) {
            return notifications[0]
        } else {
            val notification = CrmNotificationSetting()
            notification.saccountid = sAccountId
            notification.username = username
            notification.level = NotificationType.Default.name
            return notification
        }
    }

    @Cacheable
    override fun findNotifications(@CacheKey sAccountId: Int?): List<CrmNotificationSetting> {
        val ex = CrmNotificationSettingExample()
        ex.createCriteria().andSaccountidEqualTo(sAccountId)
        return crmNotificationSettingMapper!!.selectByExample(ex)
    }

}
