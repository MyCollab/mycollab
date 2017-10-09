package com.mycollab.core.utils;

/**
 *@author MyCollab Ltd
 * @since 5.2.0
 */
public class ExceptionUtils {
    public static <T> T getExceptionType(Throwable e, Class<T> exceptionType) {
        if (exceptionType.isAssignableFrom(e.getClass())) {
            return (T) e;
        } else if (e.getCause() != null) {
            return getExceptionType(e.getCause(), exceptionType);
        } else {
            return null;
        }
    }
}
