package com.mycollab.core;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MyCollabThread extends Thread {

    public MyCollabThread(Runnable runnable) {
        super(runnable);
        setExceptionHandler();
    }

    private void setExceptionHandler() {
        ThreadExceptionHandler threadExpceptionHandler = new ThreadExceptionHandler();
        this.setUncaughtExceptionHandler(threadExpceptionHandler);
    }
}
