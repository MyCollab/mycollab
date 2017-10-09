package com.mycollab.module.project.domain;

import com.mycollab.module.crm.domain.Account;

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
public class SimpleInvoice extends Invoice {
    private String createUserFullName;
    private String createUserAvatarId;
    private String assignUserFullName;
    private String assignUserAvatarId;
    private Account client;

    public Account getClient() {
        return client;
    }

    public void setClient(Account client) {
        this.client = client;
    }

    public String getCreateUserFullName() {
        return createUserFullName;
    }

    public void setCreateUserFullName(String createUserFullName) {
        this.createUserFullName = createUserFullName;
    }

    public String getCreateUserAvatarId() {
        return createUserAvatarId;
    }

    public void setCreateUserAvatarId(String createUserAvatarId) {
        this.createUserAvatarId = createUserAvatarId;
    }

    public String getAssignUserFullName() {
        return assignUserFullName;
    }

    public void setAssignUserFullName(String assignUserFullName) {
        this.assignUserFullName = assignUserFullName;
    }

    public String getAssignUserAvatarId() {
        return assignUserAvatarId;
    }

    public void setAssignUserAvatarId(String assignUserAvatarId) {
        this.assignUserAvatarId = assignUserAvatarId;
    }
}
