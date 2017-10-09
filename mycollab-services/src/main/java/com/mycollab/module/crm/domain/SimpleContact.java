package com.mycollab.module.crm.domain;

import com.mycollab.core.utils.StringUtils;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class SimpleContact extends Contact {

	private static final long serialVersionUID = 1L;

	private String createdUserAvatarId;
	private String createdUserFullName;

	private String assignUserFullName;
	private String assignUserAvatarId;

	private String contactName;
	private String accountName;

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
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

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAssignUserAvatarId() {
		return assignUserAvatarId;
	}

	public void setAssignUserAvatarId(String assignUserAvatarId) {
		this.assignUserAvatarId = assignUserAvatarId;
	}

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

	public String getDisplayName() {
		StringBuffer result = new StringBuffer();
		String prefix = getPrefix();
		if (prefix != null) {
			result.append(prefix).append(" ");
		}

		String first = getFirstname();
		if (first != null) {
			result.append(first).append(" ");
		}
		result.append(getLastname());
		return result.toString().trim();
	}
}
