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

/**
 * 
 * @author MyCollab Ltd.
 */
public class SimpleMilestone extends Milestone {
	private static final long serialVersionUID = 1L;

	public static final String STATUS_INPROGRESS = "In Progress";
	public static final String STATUS_FUTURE = "Future";
	public static final String STATUS_CLOSE = "Closed";

	private String ownerAvatarId;
	private String ownerFullName;

	private int numOpenTasks;
	private int numTasks;

	private int numOpenBugs;
	private int numBugs;

	private String projectName;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getOwnerFullName() {
		if (ownerFullName == null || ownerFullName.trim().equals("")) {
			String displayName = getOwner();
			int index = (displayName != null) ? displayName.indexOf("@") : 0;
			if (index > 0) {
				return displayName.substring(0, index);
			}
		}
		return ownerFullName;
	}

	public void setOwnerFullName(String ownerFullName) {
		this.ownerFullName = ownerFullName;
	}

	public int getNumOpenTasks() {
		return numOpenTasks;
	}

	public void setNumOpenTasks(int numOpenTasks) {
		this.numOpenTasks = numOpenTasks;
	}

	public int getNumTasks() {
		return numTasks;
	}

	public void setNumTasks(int numTasks) {
		this.numTasks = numTasks;
	}

	public int getNumOpenBugs() {
		return numOpenBugs;
	}

	public void setNumOpenBugs(int numOpenBugs) {
		this.numOpenBugs = numOpenBugs;
	}

	public int getNumBugs() {
		return numBugs;
	}

	public void setNumBugs(int numBugs) {
		this.numBugs = numBugs;
	}

	public String getOwnerAvatarId() {
		return ownerAvatarId;
	}

	public void setOwnerAvatarId(String ownerAvatarId) {
		this.ownerAvatarId = ownerAvatarId;
	}
}
