package com.mycollab.configuration.logging;

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
    private static Class[] blacklistClasses;

    static {
        try {
            blacklistClasses = new Class[]{
                    Class.forName("org.apache.jackrabbit.core.cluster.ClusterException"),
                    Class.forName("org.springframework.dao.UncategorizedDataAccessException"),
                    Class.forName("org.springframework.transaction.CannotCreateTransactionException"),
                    Class.forName("com.mycollab.core.SessionExpireException"),
                    Class.forName("java.net.SocketTimeoutException"),
                    Class.forName("org.apache.commons.mail.EmailException"),
                    Class.forName("java.net.SocketTimeoutException"),
                    Class.forName("java.sql.SQLTransientConnectionException"),
                    Class.forName("com.mysql.jdbc.exceptions.jdbc4.CommunicationsException"),
                    Class.forName("com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException")};
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
        for (Class exceptCls : blacklistClasses) {
            if (isInstanceInBlackList(exceptCls, throwable)) {
                return FilterReply.DENY;
            }
        }

        return FilterReply.NEUTRAL;
    }

    private static boolean isInstanceInBlackList(Class cls, Throwable throwable) {
        return cls.isInstance(throwable) || throwable.getCause() != null && isInstanceInBlackList(cls, throwable.getCause());
    }
}
