/**
 * This file is part of mycollab-config.
 *
 * mycollab-config is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-config is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-config.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.configuration.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public class ExceptionFilter extends Filter<ILoggingEvent> {
    private static Class[] blacklistClss;

    static {
        try {
            blacklistClss = new Class[]{Class.forName("org.apache.jackrabbit.core.cluster.ClusterException"),
                    Class.forName("org.springframework.dao.UncategorizedDataAccessException"),
                    Class.forName("org.springframework.transaction.CannotCreateTransactionException"),
                    Class.forName("com.esofthead.mycollab.core.SessionExpireException"),
                    Class.forName("java.net.SocketTimeoutException"),
                    Class.forName("org.apache.commons.mail.EmailException"),
                    Class.forName("java.net.SocketTimeoutException"),
                    Class.forName("java.sql.SQLTransientConnectionException")};
        } catch (Exception e) {
        }
    }

    @Override
    public FilterReply decide(ILoggingEvent event) {
        final IThrowableProxy throwableProxy = event.getThrowableProxy();
        if (throwableProxy == null) {
            return FilterReply.NEUTRAL;
        }

        if (!(throwableProxy instanceof ThrowableProxy)) {
            return FilterReply.NEUTRAL;
        }

        final ThrowableProxy throwableProxyImpl = (ThrowableProxy) throwableProxy;
        final Throwable throwable = throwableProxyImpl.getThrowable();
        for (Class exceptCls : blacklistClss) {
            if (isInstanceInBlackList(exceptCls, throwable)) {
                return FilterReply.DENY;
            }
        }

        return FilterReply.NEUTRAL;
    }

    private static boolean isInstanceInBlackList(Class cls, Throwable throwable) {
        if (cls.isInstance(throwable)) {
            return true;
        }
        if (throwable.getCause() != null) {
            return isInstanceInBlackList(cls, throwable.getCause());
        }
        return false;
    }
}
