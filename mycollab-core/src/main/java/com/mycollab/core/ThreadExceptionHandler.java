package com.mycollab.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class ThreadExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ThreadExceptionHandler.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LOG.error(String.format("Exception when run thread: %s with exception: %s", t.getName(), e.getMessage()));
    }

}
