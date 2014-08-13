package com.esofthead.mycollab.mobile.module.project;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.MyCollabSession;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.project.dao.ProjectRolePermissionMapper;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.ProjectRolePermission;
import com.esofthead.mycollab.module.project.domain.ProjectRolePermissionExample;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
 *
 */
public class CurrentProjectVariables {
	private static Logger log = LoggerFactory
			.getLogger(CurrentProjectVariables.class);

	private static final String CURRENT_PAGE_VAR = "project_page";

	public static SimpleProject getProject() {
		return (SimpleProject) MyCollabSession
				.getVariable(MyCollabSession.CURRENT_PROJECT);
	}

	public static void setProject(SimpleProject project) {
		MyCollabSession.putVariable(MyCollabSession.CURRENT_PROJECT, project);

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
		MyCollabSession.putVariable(MyCollabSession.PROJECT_MEMBER, prjMember);
	}

	private static SimpleProjectMember getProjectMember() {
		return (SimpleProjectMember) MyCollabSession
				.getVariable(MyCollabSession.PROJECT_MEMBER);
	}

	private static boolean isAdmin() {
		if (AppContext.isAdmin()) {
			return true;
		}
		ProjectMember member = (ProjectMember) MyCollabSession
				.getVariable(MyCollabSession.PROJECT_MEMBER);
		if (member != null && member.getIsadmin() != null) {
			return member.getIsadmin();
		}
		return false;
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

	public static String getCurrentPagePath() {
		String path = (String) MyCollabSession.getVariable(CURRENT_PAGE_VAR);
		if (path == null) {
			String newPath = String.format("%d/project/%d/.page",
					AppContext.getAccountId(), getProjectId());
			setCurrentPagePath(newPath);
			return newPath;
		} else {
			return path;
		}
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
