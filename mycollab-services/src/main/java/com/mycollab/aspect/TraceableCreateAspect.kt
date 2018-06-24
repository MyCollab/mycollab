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

import com.mycollab.common.ActivityStreamConstants
import com.mycollab.common.domain.ActivityStreamWithBLOBs
import com.mycollab.common.service.ActivityStreamService
import com.mycollab.core.utils.DateTimeUtils
import org.apache.commons.beanutils.PropertyUtils
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.aop.framework.Advised
import org.springframework.stereotype.Component
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Aspect
@Component
class TraceableCreateAspect(private var activityStreamService: ActivityStreamService) {

    @AfterReturning("execution(public * com.mycollab..service..*.saveWithSession(..)) && args(bean, username)")
    fun traceSaveActivity(joinPoint: JoinPoint, bean: Any, username: String) {
        val advised = joinPoint.`this` as Advised
        val cls = advised.targetSource.targetClass!!

        val traceableAnnotation = cls.getAnnotation(Traceable::class.java)
        if (traceableAnnotation != null) {
            try {
                val activity = constructActivity(cls, traceableAnnotation, bean, username,
                        ActivityStreamConstants.ACTION_CREATE)
                activityStreamService.save(activity)
            } catch (e: Exception) {
                LOG.error("Error when save activity for save action of service ${cls.name}", e)
            }
        }
    }

    @AfterReturning("execution(public * com.mycollab..service..*.removeWithSession(..)) && args(bean, username, sAccountId)")
    fun traceDeleteActivity(joinPoint: JoinPoint, bean: Any, username: String, sAccountId: Int?) {
        val advised = joinPoint.`this` as Advised
        val cls = advised.targetSource.targetClass!!

        val traceableAnnotation = cls.getAnnotation(Traceable::class.java)
        if (traceableAnnotation != null) {
            try {
                val activity = constructActivity(cls, traceableAnnotation, bean, username,
                        ActivityStreamConstants.ACTION_DELETE)
                activityStreamService.save(activity)
            } catch (e: Exception) {
                LOG.error("Error when save activity for save action of service ${cls.name}", e)
            }

        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(TraceableCreateAspect::class.java)

        @Throws(IllegalAccessException::class, InvocationTargetException::class, NoSuchMethodException::class)
        internal fun constructActivity(cls: Class<*>, traceableAnnotation: Traceable, bean: Any, username: String, action: String): ActivityStreamWithBLOBs {
            val activity = ActivityStreamWithBLOBs()
            activity.module = ClassInfoMap.getModule(cls)
            activity.type = ClassInfoMap.getType(cls)
            activity.typeid = PropertyUtils.getProperty(bean, traceableAnnotation.idField).toString()
            activity.createdtime = GregorianCalendar().time
            activity.action = action
            activity.saccountid = PropertyUtils.getProperty(bean, "saccountid") as Int
            activity.createduser = username

            val nameObj = PropertyUtils.getProperty(bean, traceableAnnotation.nameField)
            val nameField: String
            nameField = when (nameObj) {
                is Date -> DateTimeUtils.formatDate(nameObj, "MM/dd/yyyy", Locale.US)
                else -> nameObj.toString()
            }
            activity.namefield = nameField

            if ("" != traceableAnnotation.extraFieldName) {
                val extraTypeId = PropertyUtils.getProperty(bean, traceableAnnotation.extraFieldName) as Int
                activity.extratypeid = extraTypeId
            }
            return activity
        }
    }
}