package com.mycollab.module.page.domain;

import com.mycollab.core.arguments.NotBindable;

import javax.validation.constraints.NotNull;
import java.util.Calendar;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class Page extends PageResource {

    @NotNull(message = "Subject must be not null")
    private String subject;

    @NotNull(message = "Content must be not null")
    private String content;

    @NotBindable
    private boolean isLock = false;

    @NotBindable
    private boolean isNew = true;

    private String category;

    private String status;

    private String lastUpdatedUser;

    @NotBindable
    private Calendar lastUpdatedTime;

    public Page() {
        super();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean isLock) {
        this.isLock = isLock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public String getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public void setLastUpdatedUser(String lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }

    public Calendar getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Calendar lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
