package com.mycollab.module.crm.domain;

import com.mycollab.core.utils.StringUtils;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class SimpleCase extends CaseWithBLOBs {
	private static final long serialVersionUID = 1L;

	private String createdUserAvatarId;

	private String createdUserFullName;

	private String assignUserAvatarId;

	private String assignUserFullName;

	private String accountName;

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

	public String getAssignUserFullName() {
		if (StringUtils.isBlank(assignUserFullName)) {
			return StringUtils.extractNameFromEmail(getAssignuser());
		}
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

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

    public boolean isCompleted() {
        return "Closed".equals(getStatus()) || "Rejected".equals(getStatus());
    }
}
