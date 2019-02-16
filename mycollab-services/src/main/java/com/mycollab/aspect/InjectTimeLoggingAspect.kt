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


import org.apache.commons.beanutils.PropertyUtils
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Aspect
@Component
class InjectTimeLoggingAspect {

    @Before("execution(public * com.mycollab..service..*.saveWithSession(..)) && args(bean, username)")
    fun injectDateForSaveMethod(joinPoint: JoinPoint, bean: Any, username: String) {
        try {
            LOG.debug("Set createtime and lastupdatedtime if enable")
            PropertyUtils.setProperty(bean, "createdtime", LocalDateTime.now())
            PropertyUtils.setProperty(bean, "lastupdatedtime", LocalDateTime.now())
        } catch (e: Exception) {
        }

    }

    @Before("(execution(public * com.mycollab..service..*.updateWithSession(..)) || (execution(public * com.mycollab..service..*.updateSelectiveWithSession(..))))  && args(bean, username)")
    fun injectDateForUpdateMethod(joinPoint: JoinPoint, bean: Any, username: String) {
        try {
            LOG.debug("Set createtime and lastupdatedtime if enable")
            PropertyUtils.setProperty(bean, "lastupdatedtime", LocalDateTime.now())
        } catch (e: Exception) {
        }

    }

    companion object {
        private val LOG = LoggerFactory.getLogger(InjectTimeLoggingAspect::class.java)
    }
}