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

import java.util.Date;

import com.esofthead.mycollab.core.arguments.NotBindable;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.security.PermissionMap;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleProjectMember extends ProjectMember {
	private static final long serialVersionUID = 1L;

	private String memberAvatarId;

	private String memberFullName;

	private String roleName;

	private int projectRoleId;

	@NotBindable
	private PermissionMap permissionMaps;

	private int numOpenTasks;

	private int numOpenBugs;

	private String projectName;

	private String email;

	private Date lastAccessTime;

	public String getMemberFullName() {
		return getDisplayName();
	}

	public void setMemberFullName(String memberFullName) {
		this.memberFullName = memberFullName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public PermissionMap getPermissionMaps() {
		return permissionMaps;
	}

	public void setPermissionMaps(PermissionMap permissionMaps) {
		this.permissionMaps = permissionMaps;
	}

	public int getNumOpenTasks() {
		return numOpenTasks;
	}

	public void setNumOpenTasks(int numOpenTasks) {
		this.numOpenTasks = numOpenTasks;
	}

	public int getNumOpenBugs() {
		return numOpenBugs;
	}

	public void setNumOpenBugs(int numOpenBugs) {
		this.numOpenBugs = numOpenBugs;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public String getMemberAvatarId() {
		return memberAvatarId;
	}

	public void setMemberAvatarId(String memberAvatarId) {
		this.memberAvatarId = memberAvatarId;
	}

	public Date getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(Date lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public int getProjectRoleId() {
		return projectRoleId;
	}

	public void setProjectRoleId(int projectRoleId) {
		this.projectRoleId = projectRoleId;
	}

	public String getDisplayName() {
		if (memberFullName == null || memberFullName.trim().equals("")) {
			return StringUtils.extractNameFromEmail(getUsername());
		}
		return memberFullName;
	}
}
