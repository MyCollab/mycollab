package com.mycollab.module.crm.domain;

import com.mycollab.core.utils.StringUtils;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class SimpleCall extends CallWithBLOBs {

	private static final long serialVersionUID = 1L;
	private String assignUserFullName;
	private String assignUserAvatarId;

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
}
