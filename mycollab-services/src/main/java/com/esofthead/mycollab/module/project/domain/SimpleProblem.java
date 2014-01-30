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
package com.esofthead.mycollab.module.project.domain;

public class SimpleProblem extends Problem {
	private static final long serialVersionUID = 1L;

	private String raisedByUserAvatarId;

	private String raisedByUserFullName;

	private String assignUserAvatarId;

	private String assignedUserFullName;

	public String getRaisedByUserAvatarId() {
		return raisedByUserAvatarId;
	}

	public void setRaisedByUserAvatarId(String raisedByUserAvatarId) {
		this.raisedByUserAvatarId = raisedByUserAvatarId;
	}

	public String getAssignUserAvatarId() {
		return assignUserAvatarId;
	}

	public void setAssignUserAvatarId(String assignUserAvatarId) {
		this.assignUserAvatarId = assignUserAvatarId;
	}

	public String getRaisedByUserFullName() {
		return raisedByUserFullName;
	}

	public void setRaisedByUserFullName(String raisedByUserFullName) {
		this.raisedByUserFullName = raisedByUserFullName;
	}

	public String getAssignedUserFullName() {
		return assignedUserFullName;
	}

	public void setAssignedUserFullName(String assignedUserFullName) {
		this.assignedUserFullName = assignedUserFullName;
	}
}
