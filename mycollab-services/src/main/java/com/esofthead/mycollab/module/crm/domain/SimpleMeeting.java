/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.crm.domain;

import java.util.List;

import com.esofthead.mycollab.core.utils.StringUtils;

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
		if (createdUserFullName == null
				|| createdUserFullName.trim().equals("")) {
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
