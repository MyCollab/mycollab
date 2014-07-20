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
package com.esofthead.mycollab.module.ecm.domain;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class SimpleContentActivityLog extends ContentActivityLogWithBLOBs {
	private static final long serialVersionUID = 1L;

	private String userFullName;
	private String userAvatarId;

	private ContentActivityLogAction logAction;

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getUserAvatarId() {
		return userAvatarId;
	}

	public void setUserAvatarId(String userAvatarId) {
		this.userAvatarId = userAvatarId;
	}

	public ContentActivityLogAction getLogAction() {
		if (logAction == null) {
			logAction = ContentActivityLogAction.fromString(this
					.getActiondesc());
		}

		return logAction;
	}
}
