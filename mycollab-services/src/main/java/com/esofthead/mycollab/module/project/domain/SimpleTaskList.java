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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.esofthead.mycollab.core.arguments.NotBindable;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleTaskList extends TaskList {
	private static final long serialVersionUID = 1L;
	private String projectName;
	private String milestoneName;
	private String createdUserAvatarId;
	private String createdUserFullName;
	private String ownerAvatarId;
	private String ownerFullName;

	@NotBindable
	private List<SimpleTask> subTasks = new ArrayList<SimpleTask>();
	private double percentageComplete;
	private int numOpenTasks;
	private int numAllTasks;
	private int numComments;
	private String comment;

	public String getMilestoneName() {
		return milestoneName;
	}

	public void setMilestoneName(String milestoneName) {
		this.milestoneName = milestoneName;
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

	public List<SimpleTask> getSubTasks() {
		return subTasks;
	}

	public void setSubTasks(List<SimpleTask> subTasks) {
		this.subTasks = subTasks;
	}

	public Date getStartDate() {
		Date result = null;
		for (SimpleTask task : subTasks) {
			if (task.getStartdate() != null) {
				if (result == null) {
					result = task.getStartdate();
				} else {
					if (result.after(task.getStartdate())) {
						result = task.getStartdate();
					}
				}
			}
		}
		return result;
	}

	public Date getEndDate() {
		Date result = null;
		for (SimpleTask task : subTasks) {
			if (task.getEnddate() != null) {
				if (result == null) {
					result = task.getEnddate();
				} else {
					if (result.before(task.getEnddate())) {
						result = task.getEnddate();
					}
				}
			}
		}
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SimpleTaskList)) {
			return false;
		} else {
			SimpleTaskList taskList = (SimpleTaskList) o;
			return (taskList.getId() == this.getId() && taskList
					.getGroupindex() == this.getGroupindex());
		}
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 47
				* hash
				+ (this.milestoneName != null ? this.milestoneName.hashCode()
						: 0);
		hash = 47
				* hash
				+ (this.ownerFullName != null ? this.ownerFullName.hashCode()
						: 0);
		return hash;
	}

	public double getPercentageComplete() {
		return percentageComplete;
	}

	public void setPercentageComplete(double percentageComplete) {
		this.percentageComplete = percentageComplete;
	}

	public int getNumOpenTasks() {
		return numOpenTasks;
	}

	public void setNumOpenTasks(int numOpenTasks) {
		this.numOpenTasks = numOpenTasks;
	}

	public int getNumAllTasks() {
		return numAllTasks;
	}

	public void setNumAllTasks(int numAllTasks) {
		this.numAllTasks = numAllTasks;
	}

	public String getOwnerAvatarId() {
		return ownerAvatarId;
	}

	public void setOwnerAvatarId(String ownerAvatarId) {
		this.ownerAvatarId = ownerAvatarId;
	}

	public int getNumComments() {
		return numComments;
	}

	public void setNumComments(int numComments) {
		this.numComments = numComments;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getCreatedUserAvatarId() {
		return createdUserAvatarId;
	}

	public void setCreatedUserAvatarId(String createdUserAvatarId) {
		this.createdUserAvatarId = createdUserAvatarId;
	}

	public String getCreatedUserFullName() {
		return createdUserFullName;
	}

	public void setCreatedUserFullName(String createdUserFullName) {
		this.createdUserFullName = createdUserFullName;
	}
	
	
}
