package com.mycollab.module.crm.domain;

import java.util.List;

import com.mycollab.core.utils.StringUtils;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class SimpleMeeting extends MeetingWithBLOBs {
	private static final long serialVersionUID = 1L;

	private String contactTypeName;

	private String createdUserFullName;

	private List<MeetingInvitee> meetingInvitees;

	public String getContactTypeName() {
		return contactTypeName;
	}

	public void setContactTypeName(String contactTypeName) {
		this.contactTypeName = contactTypeName;
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

	public List<MeetingInvitee> getMeetingInvitees() {
		return meetingInvitees;
	}

	public void setMeetingInvitees(List<MeetingInvitee> meetingInvitees) {
		this.meetingInvitees = meetingInvitees;
	}
}
