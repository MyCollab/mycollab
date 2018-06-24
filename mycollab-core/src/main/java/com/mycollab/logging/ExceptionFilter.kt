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
package com.mycollab.logging

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.ThrowableProxy
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
class ExceptionFilter : Filter<ILoggingEvent>() {

    override fun decide(event: ILoggingEvent): FilterReply {
        val throwableProxy = (event.throwableProxy ?: return FilterReply.NEUTRAL) as? ThrowableProxy ?: return FilterReply.NEUTRAL

        val throwable = throwableProxy.throwable
        blacklistClasses.forEach {
            if (isInstanceInBlackList(it, throwable)) {
                return FilterReply.DENY
            }
        }

        return FilterReply.NEUTRAL
    }

    companion object {
        private var blacklistClasses: Array<Class<*>> = try {
            arrayOf(Class.forName("org.apache.jackrabbit.core.cluster.ClusterException"),
                    Class.forName("org.springframework.dao.UncategorizedDataAccessException"),
                    Class.forName("org.springframework.transaction.CannotCreateTransactionException"),
                    Class.forName("com.mycollab.core.SessionExpireException"),
                    Class.forName("java.net.SocketTimeoutException"),
                    Class.forName("org.apache.commons.mail.EmailException"),
                    Class.forName("java.net.SocketTimeoutException"),
                    Class.forName("java.sql.SQLTransientConnectionException"),
                    Class.forName("com.mysql.jdbc.exceptions.jdbc4.CommunicationsException"),
                    Class.forName("com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException"))
        } catch (e: Exception) {
            arrayOf()
        }

        private fun isInstanceInBlackList(cls: Class<*>, throwable: Throwable): Boolean {
            return cls.isInstance(throwable) || throwable.cause != null && isInstanceInBlackList(cls, throwable.cause as Throwable)
        }
    }
}
