/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.aspect

import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.domain.MonitorItem
import com.mycollab.common.domain.RelayEmailNotificationWithBLOBs
import com.mycollab.common.service.MonitorItemService
import com.mycollab.common.service.RelayEmailNotificationService
import org.apache.commons.beanutils.PropertyUtils
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.aop.framework.Advised
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Aspect
@Component
@Configurable
class MonitorItemAspect(private var monitorItemService: MonitorItemService,
                        private var relayEmailNotificationService: RelayEmailNotificationService) {

    @AfterReturning("execution(public * com.mycollab..service..*.saveWithSession(..)) && args(bean, username)")
    fun traceSaveActivity(joinPoint: JoinPoint, bean: Any, username: String) {
        val advised = joinPoint.`this` as Advised
        val cls = advised.targetSource.targetClass!!
        try {
            val watchableAnnotation = cls.getAnnotation(Watchable::class.java)
            if (watchableAnnotation != null) {
                val sAccountId = PropertyUtils.getProperty(bean, "saccountid") as Int
                val typeId = PropertyUtils.getProperty(bean, "id") as Int
                var extraTypeId: Int? = null
                if ("" != watchableAnnotation.extraTypeId) {
                    extraTypeId = PropertyUtils.getProperty(bean, watchableAnnotation.extraTypeId) as Int
                }

                val monitorItem = MonitorItem()
                monitorItem.monitorDate = GregorianCalendar().time
                monitorItem.type = ClassInfoMap.getType(cls)
                monitorItem.typeid = typeId
                monitorItem.extratypeid = extraTypeId
                monitorItem.user = username
                monitorItem.saccountid = sAccountId

                monitorItemService.saveWithSession(monitorItem, username)

                if (watchableAnnotation.userFieldName != "") {
                    val moreUser = PropertyUtils.getProperty(bean, watchableAnnotation.userFieldName) as? String
                    if (moreUser != null && moreUser != username) {
                        monitorItem.id = null
                        monitorItem.user = moreUser
                        monitorItemService.saveWithSession(monitorItem, moreUser)
                    }
                }
            }

            val traceAnnotation = cls.getAnnotation(Traceable::class.java)
            if (traceAnnotation != null) {
                val sAccountId = PropertyUtils.getProperty(bean, "saccountid") as Int
                val typeId = PropertyUtils.getProperty(bean, "id") as Int
                val relayNotification = RelayEmailNotificationWithBLOBs()
                relayNotification.changeby = username
                relayNotification.changecomment = ""
                relayNotification.saccountid = sAccountId
                relayNotification.type = ClassInfoMap.getType(cls)
                relayNotification.action = MonitorTypeConstants.CREATE_ACTION
                relayNotification.typeid = "$typeId"
                relayEmailNotificationService.saveWithSession(relayNotification, username)
                // Save notification item
            }
        } catch (e: Exception) {
            LOG.error("Error when save relay email notification for save action of service ${cls.name}", e)
        }

    }

    companion object {
        private val LOG = LoggerFactory.getLogger(MonitorItemAspect::class.java)
    }
}
