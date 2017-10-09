package com.mycollab.module.page.domain;

import com.mycollab.core.arguments.NotBindable;

import java.util.Calendar;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class PageResource {
    @NotBindable
    private Calendar createdTime;

    @NotBindable
    private String createdUser;

    @NotBindable
    protected String path = "";

    public Calendar getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Calendar createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
