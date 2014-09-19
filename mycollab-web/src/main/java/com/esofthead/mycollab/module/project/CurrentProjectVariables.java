/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project;

import static com.esofthead.mycollab.common.MyCollabSession.CURRENT_PROJECT;
import static com.esofthead.mycollab.common.MyCollabSession.PROJECT_MEMBER;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.MyCollabSession;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.project.dao.ProjectRolePermissionMapper;
import com.esofthead.mycollab.module.project.domain.ProjectCustomizeView;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.ProjectRolePermission;
import com.esofthead.mycollab.module.project.domain.ProjectRolePermissionExample;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;

;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class CurrentProjectVariables {
	private static Logger log = LoggerFactory
			.getLogger(CurrentProjectVariables.class);

	private static final String CURRENT_PAGE_VAR = "project_page";

	public static SimpleProject getProject() {
		return (SimpleProject) MyCollabSession.getVariable(CURRENT_PROJECT);
	}

	public static void setProject(SimpleProject project) {
		MyCollabSession.putVariable(CURRENT_PROJECT, project);

		// get member permission
		ProjectMemberService prjMemberService = ApplicationContextUtil
				.getSpringBean(ProjectMemberService.class);
		SimpleProjectMember prjMember = prjMemberService.findMemberByUsername(
				AppContext.getUsername(), project.getId(),
				AppContext.getAccountId());
		if (prjMember != null) {
			if (((prjMember.getIsadmin() != null && prjMember.getIsadmin() == Boolean.FALSE) || (prjMember
					.getIsadmin() == null))
					&& prjMember.getProjectroleid() != null) {
				ProjectRolePermissionExample ex = new ProjectRolePermissionExample();
				ex.createCriteria()
						.andRoleidEqualTo(prjMember.getProjectroleid())
						.andProjectidEqualTo(
								CurrentProjectVariables.getProjectId());
				ProjectRolePermissionMapper rolePermissionMapper = ApplicationContextUtil
						.getSpringBean(ProjectRolePermissionMapper.class);
				List<ProjectRolePermission> rolePermissions = rolePermissionMapper
						.selectByExampleWithBLOBs(ex);
				if (!rolePermissions.isEmpty()) {
					ProjectRolePermission rolePer = rolePermissions.get(0);
					PermissionMap permissionMap = PermissionMap
							.fromJsonString(rolePer.getRoleval());
					prjMember.setPermissionMaps(permissionMap);
				}
			}

			setProjectMember(prjMember);
		} else if (!AppContext.isAdmin()) {
			throw new MyCollabException("You are not belong to this project");
		}
	}

	private static void setProjectMember(SimpleProjectMember prjMember) {
		MyCollabSession.putVariable(PROJECT_MEMBER, prjMember);
	}

	private static SimpleProjectMember getProjectMember() {
		return (SimpleProjectMember) MyCollabSession
				.getVariable(PROJECT_MEMBER);
	}

	public static boolean isAdmin() {
		if (AppContext.isAdmin()) {
			return true;
		}
		ProjectMember member = (ProjectMember) MyCollabSession
				.getVariable(PROJECT_MEMBER);
		if (member != null && member.getIsadmin() != null) {
			return member.getIsadmin();
		}
		return false;
	}

	public static boolean isProjectArchived() {
		return getProject().isProjectArchived();
	}

	public static boolean canRead(String permissionItem) {
		if (isAdmin()) {
			return true;
		}

		try {
			PermissionMap permissionMap = getProjectMember()
					.getPermissionMaps();
			if (permissionMap == null) {
				return false;
			} else {
				return permissionMap.canRead(permissionItem);
			}
		} catch (Exception e) {
			log.error("Error while checking permission", e);
			return false;
		}
	}

	public static boolean canWrite(String permissionItem) {
		if (isProjectArchived()) {
			return false;
		}

		if (isAdmin()) {
			return true;
		}

		try {
			PermissionMap permissionMap = getProjectMember()
					.getPermissionMaps();
			if (permissionMap == null) {
				return false;
			} else {
				return permissionMap.canWrite(permissionItem);
			}
		} catch (Exception e) {
			log.error("Error while checking permission", e);
			return false;
		}
	}

	public static boolean canAccess(String permissionItem) {
		if (isProjectArchived()) {
			return false;
		}

		if (isAdmin()) {
			return true;
		}

		try {
			PermissionMap permissionMap = getProjectMember()
					.getPermissionMaps();
			if (permissionMap == null) {
				return false;
			} else {
				return permissionMap.canAccess(permissionItem);
			}
		} catch (Exception e) {
			log.error("Error while checking permission", e);
			return false;
		}
	}

	public static ProjectCustomizeView getFeatures() {
		ProjectCustomizeView customizeView = getProject().getCustomizeView();
		if (customizeView == null) {
			customizeView = new ProjectCustomizeView();
			customizeView.setProjectid(CurrentProjectVariables.getProjectId());
			customizeView.setDisplaybug(true);
			customizeView.setDisplaymessage(true);
			customizeView.setDisplaymilestone(true);
			customizeView.setDisplaypage(true);
			customizeView.setDisplayproblem(true);
			customizeView.setDisplayrisk(true);
			customizeView.setDisplaystandup(true);
			customizeView.setDisplaytask(true);
			customizeView.setDisplaytimelogging(true);
			customizeView.setDisplayfile(true);
		}
		return customizeView;
	}

	public static boolean hasMessageFeature() {
		return getFeatures().getDisplaymessage();
	}

	public static boolean hasPhaseFeature() {
		return getFeatures().getDisplaymilestone();
	}

	public static boolean hasTaskFeature() {
		return getFeatures().getDisplaytask();
	}

	public static boolean hasBugFeature() {
		return getFeatures().getDisplaybug();
	}

	public static boolean hasPageFeature() {
		return getFeatures().getDisplaypage();
	}

	public static boolean hasProblemFeature() {
		return getFeatures().getDisplayproblem();
	}

	public static boolean hasRiskFeature() {
		return getFeatures().getDisplayrisk();
	}

	public static boolean hasFileFeature() {
		return getFeatures().getDisplayfile();
	}

	public static boolean hasTimeFeature() {
		return getFeatures().getDisplaytimelogging();
	}

	public static boolean hasStandupFeature() {
		return getFeatures().getDisplaystandup();
	}

	public static String getCurrentPagePath() {
		String path = (String) MyCollabSession.getVariable(CURRENT_PAGE_VAR);
		if (path == null) {
			path = getBasePagePath();
			setCurrentPagePath(path);
		}

		return path;
	}

	public static String getBasePagePath() {
		return String.format("%d/project/%d/.page", AppContext.getAccountId(),
				getProjectId());
	}

	public static void setCurrentPagePath(String path) {
		MyCollabSession.putVariable(CURRENT_PAGE_VAR, path);
	}

	public static int getProjectId() {
		SimpleProject project = getProject();
		if (project != null) {
			return project.getId();
		} else {
			return -1;
		}

	}
}
