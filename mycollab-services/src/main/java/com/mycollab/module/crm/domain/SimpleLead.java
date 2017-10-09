package com.mycollab.module.crm.domain;

import com.mycollab.core.utils.StringUtils;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class SimpleLead extends Lead {
	private static final long serialVersionUID = 1L;

	private String createdUserAvatarId;

	private String createdUserFullName;

	private String assignUserAvatarId;

	private String assignUserFullName;

	private String campaignName;

	private String leadName;

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

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getAssignUserAvatarId() {
		return assignUserAvatarId;
	}

	public void setAssignUserAvatarId(String assignUserAvatarId) {
		this.assignUserAvatarId = assignUserAvatarId;
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

	public void setLeadName(String leadName) {
		this.leadName = leadName;
	}

	public String getLeadName() {
		return leadName;
	}
}
