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
package com.esofthead.mycollab.module.tracker.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.esofthead.mycollab.core.arguments.NotBindable;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class SimpleBug extends BugWithBLOBs {

	private static final long serialVersionUID = 1L;
	private String loguserFullName;
	private String loguserAvatarId;
	private String assignUserAvatarId;
	private String assignuserFullName;
	private String projectname;

	@NotBindable
	private List<Version> affectedVersions;

	@NotBindable
	private List<Version> fixedVersions;

	@NotBindable
	private List<Component> components;

	private String comment;
	private String milestoneName;

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getLoguserFullName() {
		if (loguserFullName == null || loguserFullName.trim().equals("")) {
			String displayName = getLogby();
			int index = (displayName != null) ? displayName.indexOf("@") : 0;
			if (index > 0) {
				return displayName.substring(0, index);
			}
		}
		return loguserFullName;
	}

	public void setLoguserFullName(String loguserFullName) {
		this.loguserFullName = loguserFullName;
	}

	public String getAssignuserFullName() {
		if (assignuserFullName == null || assignuserFullName.trim().equals("")) {
			String displayName = getAssignuser();
			int index = (displayName != null) ? displayName.indexOf("@") : 0;
			if (index > 0) {
				return displayName.substring(0, index);
			}
		}
		return assignuserFullName;
	}

	public void setAssignuserFullName(String assignuserFullName) {
		this.assignuserFullName = assignuserFullName;
	}

	public List<Version> getAffectedVersions() {
		return affectedVersions;
	}

	public void setAffectedVersions(List<Version> affectedVersions) {
		this.affectedVersions = affectedVersions;
	}

	public List<Version> getFixedVersions() {
		return fixedVersions;
	}

	public void setFixedVersions(List<Version> fixedVersions) {
		this.fixedVersions = fixedVersions;
	}

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setMilestoneName(String milestoneName) {
		this.milestoneName = milestoneName;
	}

	public String getMilestoneName() {
		return milestoneName;
	}

	public String getAssignUserAvatarId() {
		return assignUserAvatarId;
	}

	public void setAssignUserAvatarId(String assignUserAvatarId) {
		this.assignUserAvatarId = assignUserAvatarId;
	}

	public String getLoguserAvatarId() {
		return loguserAvatarId;
	}

	public void setLoguserAvatarId(String loguserAvatarId) {
		this.loguserAvatarId = loguserAvatarId;
	}

	public boolean isCompleted() {
		return BugStatus.Verified.name().equals(getStatus());
	}

	public boolean isOverdue() {
		if (BugStatus.Verified.name().equals(getStatus())) {
			return false;
		}

		if (this.getDuedate() != null) {
			Calendar today = Calendar.getInstance();
			today.set(Calendar.HOUR_OF_DAY, 0);
			Date todayDate = today.getTime();

			return todayDate.after(this.getDuedate());
		} else {
			return false;
		}
	}
}
