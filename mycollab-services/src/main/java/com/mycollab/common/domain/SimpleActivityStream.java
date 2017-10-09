package com.mycollab.common.domain;

import com.mycollab.core.utils.StringUtils;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleActivityStream extends ActivityStreamWithBLOBs {
    private static final long serialVersionUID = 1L;

    private String createdUserAvatarId;
    private String createdUserFullName;
    private SimpleAuditLog assoAuditLog;

    public String getCreatedUserAvatarId() {
        return createdUserAvatarId;
    }

    public void setCreatedUserAvatarId(String createdUserAvatarId) {
        this.createdUserAvatarId = createdUserAvatarId;
    }

    public String getCreatedUserFullName() {
        if (StringUtils.isBlank(createdUserFullName)) {
            return StringUtils.extractNameFromEmail(getCreateduser());
        }
        return createdUserFullName;
    }

    public void setCreatedUserFullName(String createdUserFullName) {
        this.createdUserFullName = createdUserFullName;
    }

    public SimpleAuditLog getAssoAuditLog() {
        return assoAuditLog;
    }

    public void setAssoAuditLog(SimpleAuditLog assoAuditLog) {
        this.assoAuditLog = assoAuditLog;
    }
}
