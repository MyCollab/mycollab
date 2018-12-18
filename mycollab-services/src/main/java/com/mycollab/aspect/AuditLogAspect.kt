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

import com.mycollab.cache.service.CacheService
import com.mycollab.common.ActivityStreamConstants
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.domain.AuditLog
import com.mycollab.common.domain.MonitorItem
import com.mycollab.common.domain.RelayEmailNotificationWithBLOBs
import com.mycollab.common.service.ActivityStreamService
import com.mycollab.common.service.AuditLogService
import com.mycollab.common.service.MonitorItemService
import com.mycollab.common.service.RelayEmailNotificationService
import com.mycollab.core.utils.BeanUtility
import org.apache.commons.beanutils.PropertyUtils
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.aop.framework.Advised
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.stereotype.Component
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Aspect
@Component
@Configurable
class AuditLogAspect(private var cacheService: CacheService,
                     private var auditLogService: AuditLogService,
                     private var activityStreamService: ActivityStreamService,
                     private var monitorItemService: MonitorItemService,
                     private var relayEmailNotificationService: RelayEmailNotificationService) {

    @Before("(execution(public * com.mycollab..service..*.updateWithSession(..)) || (execution(public * com.mycollab..service..*.updateSelectiveWithSession(..)))) && args(bean, username)")
    fun traceBeforeUpdateActivity(joinPoint: JoinPoint, bean: Any, username: String) {
        val advised = joinPoint.`this` as Advised
        val cls = advised.targetSource.targetClass!!

        val auditAnnotation = cls.getAnnotation(Traceable::class.java)
        if (auditAnnotation != null) {
            try {
                val typeId = PropertyUtils.getProperty(bean, "id") as Int
                val sAccountId = PropertyUtils.getProperty(bean, "saccountid") as Int
                // store old value to map, wait until the update process
                // successfully then add to log item

                // get old value
                val service = advised.targetSource.target
                val oldValue: Any
                var findMethod = try {
                    cls.getMethod("findById", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                } catch (e: Exception) {
                    cls.getMethod("findByPrimaryKey", Serializable::class.java, Int::class.javaPrimitiveType)
                }

                oldValue = findMethod.invoke(service, typeId, sAccountId)
                val key = bean.toString() + ClassInfoMap.getType(cls) + typeId

                cacheService.putValue(AUDIT_TEMP_CACHE, key, oldValue)
            } catch (e: Exception) {
                LOG.error("Error when save audit for save action of service ${cls.name}", e)
            }

        }
    }

    @AfterReturning("(execution(public * com.mycollab..service..*.updateWithSession(..)) || (execution(public * com.mycollab..service..*.updateSelectiveWithSession(..))))  && args(bean, username)")
    fun traceAfterUpdateActivity(joinPoint: JoinPoint, bean: Any, username: String) {
        val advised = joinPoint.`this` as Advised
        val cls = advised.targetSource.targetClass!!
        val isSelective = "updateSelectiveWithSession" == joinPoint.signature.name

        try {
            val watchableAnnotation = cls.getAnnotation(Watchable::class.java)
            if (watchableAnnotation != null) {
                val monitorType = ClassInfoMap.getType(cls)
                val sAccountId = PropertyUtils.getProperty(bean, "saccountid") as Int
                val typeId = PropertyUtils.getProperty(bean, "id") as Int

                var extraTypeId: Int? = null
                if ("" != watchableAnnotation.extraTypeId) {
                    extraTypeId = PropertyUtils.getProperty(bean, watchableAnnotation.extraTypeId) as Int
                }

                val monitorItem = MonitorItem()
                monitorItem.monitorDate = LocalDateTime.now()
                monitorItem.type = monitorType
                monitorItem.typeid = typeId
                monitorItem.extratypeid = extraTypeId
                monitorItem.user = username
                monitorItem.saccountid = sAccountId
                monitorItemService.saveWithSession(monitorItem, username)

                // check whether the current user is in monitor list, if not add him in
                if (watchableAnnotation.userFieldName != "") {
                    val moreUser = PropertyUtils.getProperty(bean, watchableAnnotation.userFieldName) as? String
                    if (moreUser != null && moreUser != username) {
                        monitorItem.id = null
                        monitorItem.user = moreUser
                        monitorItemService.saveWithSession(monitorItem, moreUser)
                    }
                }
            }

            val traceableAnnotation = cls.getAnnotation(Traceable::class.java)
            if (traceableAnnotation != null) {
                try {
                    val classInfo = ClassInfoMap.getClassInfo(cls)
                    val changeSet = getChangeSet(this, cls, bean, classInfo!!.getExcludeHistoryFields(), isSelective)
                    if (changeSet != null) {
                        val activity = TraceableCreateAspect.constructActivity(cls,
                                traceableAnnotation, bean, username, ActivityStreamConstants.ACTION_UPDATE)
                        val activityStreamId = activityStreamService.save(activity)

                        val sAccountId = PropertyUtils.getProperty(bean, "saccountid") as Int
                        val auditLogId = saveAuditLog(this, cls, bean, changeSet, username, sAccountId, activityStreamId)

                        val typeId = PropertyUtils.getProperty(bean, "id") as Int
                        // Save notification email
                        val relayNotification = RelayEmailNotificationWithBLOBs()
                        relayNotification.changeby = username
                        relayNotification.changecomment = ""
                        relayNotification.saccountid = sAccountId
                        relayNotification.type = ClassInfoMap.getType(cls)
                        relayNotification.typeid = "" + typeId
                        if (auditLogId != null) {
                            relayNotification.extratypeid = auditLogId
                        }
                        relayNotification.action = MonitorTypeConstants.UPDATE_ACTION

                        relayEmailNotificationService.saveWithSession(relayNotification, username)
                    }
                } catch (e: Exception) {
                    LOG.error("Error when save activity for save action of service ${cls.name}", e)
                }

            }
        } catch (e: Exception) {
            LOG.error("Error when save audit for save action of service ${cls.name} and bean: ${BeanUtility.printBeanObj(bean)}", e)
        }

    }

    companion object {
        private val LOG = LoggerFactory.getLogger(AuditLogAspect::class.java)

        private const val AUDIT_TEMP_CACHE = "AUDIT_TEMP_CACHE"

        fun getChangeSet(auditLogAspect: AuditLogAspect, targetCls: Class<*>, bean: Any, excludeHistoryFields: List<String>, isSelective: Boolean): String? {
            return try {
                val typeId = PropertyUtils.getProperty(bean, "id") as Int
                val key = "$bean${ClassInfoMap.getType(targetCls)}$typeId"

                val oldValue = auditLogAspect.cacheService.getValue(AUDIT_TEMP_CACHE, key)
                if (oldValue != null) {
                    AuditLogUtil.getChangeSet(oldValue, bean, excludeHistoryFields, isSelective)
                } else null
            } catch (e: Exception) {
                LOG.error("Error while generate changeset", e)
                null
            }
        }

        fun saveAuditLog(auditLogAspect: AuditLogAspect, targetCls: Class<*>, bean: Any, changeSet: String, username: String, sAccountId: Int?,
                         activityStreamId: Int?): Int? {
            try {
                val typeId = PropertyUtils.getProperty(bean, "id") as Int
                val auditLog = AuditLog()
                auditLog.posteduser = username
                auditLog.module = ClassInfoMap.getModule(targetCls)
                auditLog.type = ClassInfoMap.getType(targetCls)
                auditLog.typeid = typeId
                auditLog.saccountid = sAccountId
                auditLog.posteddate = LocalDateTime.now()
                auditLog.changeset = changeSet
                auditLog.objectClass = bean.javaClass.name
                if (activityStreamId != null) {
                    auditLog.activitylogid = activityStreamId
                }

                return auditLogAspect.auditLogService.saveWithSession(auditLog, "")
            } catch (e: Exception) {
                LOG.error("Error when save audit for save action of service ${targetCls.name} and bean: ${BeanUtility.printBeanObj(bean)} and changeset is $changeSet", e)
                return null
            }
        }
    }
}
