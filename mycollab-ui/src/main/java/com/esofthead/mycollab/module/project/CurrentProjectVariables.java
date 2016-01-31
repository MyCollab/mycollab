/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project;

import com.esofthead.mycollab.core.SecureAccessException;
import com.esofthead.mycollab.module.project.dao.ProjectRolePermissionMapper;
import com.esofthead.mycollab.module.project.domain.*;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.MyCollabSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.esofthead.mycollab.vaadin.ui.MyCollabSession.CURRENT_PROJECT;
import static com.esofthead.mycollab.vaadin.ui.MyCollabSession.PROJECT_MEMBER;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CurrentProjectVariables {
    private static final Logger LOG = LoggerFactory.getLogger(CurrentProjectVariables.class);

    private static final String CURRENT_PAGE_VAR = "project_page";
    private static final String TOOGLE_MENU_FLAG = "toogleProjectMenu";

    public static SimpleProject getProject() {
        return (SimpleProject) MyCollabSession.getVariable(CURRENT_PROJECT);
    }

    public static void setProject(SimpleProject project) {
        MyCollabSession.putVariable(CURRENT_PROJECT, project);

        // get member permission
        ProjectMemberService prjMemberService = ApplicationContextUtil.getSpringBean(ProjectMemberService.class);
        SimpleProjectMember prjMember = prjMemberService.findMemberByUsername(AppContext.getUsername(), project.getId(), AppContext.getAccountId());
        if (prjMember != null) {
            if (!prjMember.isProjectOwner()) {
                if (prjMember.getProjectroleid() == null) {
                    throw new SecureAccessException("You are not belong to this project");
                }
                ProjectRolePermissionExample ex = new ProjectRolePermissionExample();
                ex.createCriteria().andRoleidEqualTo(prjMember.getProjectroleid()).andProjectidEqualTo(CurrentProjectVariables.getProjectId());
                ProjectRolePermissionMapper rolePermissionMapper = ApplicationContextUtil.getSpringBean(ProjectRolePermissionMapper.class);
                List<ProjectRolePermission> rolePermissions = rolePermissionMapper.selectByExampleWithBLOBs(ex);
                if (!rolePermissions.isEmpty()) {
                    ProjectRolePermission rolePer = rolePermissions.get(0);
                    PermissionMap permissionMap = PermissionMap.fromJsonString(rolePer.getRoleval());
                    prjMember.setPermissionMaps(permissionMap);
                }
            }

            setProjectMember(prjMember);
            if (getProjectToogleMenu() == null) {
                setProjectToogleMenu(true);
            }
        } else if (!AppContext.isAdmin()) {
            throw new SecureAccessException("You are not belong to this project");
        }
    }

    public static Boolean getProjectToogleMenu() {
        return (Boolean) MyCollabSession.getVariable(TOOGLE_MENU_FLAG);
    }

    public static void setProjectToogleMenu(boolean visibility) {
        MyCollabSession.putVariable(TOOGLE_MENU_FLAG, new Boolean(visibility));
    }

    private static void setProjectMember(SimpleProjectMember prjMember) {
        MyCollabSession.putVariable(PROJECT_MEMBER, prjMember);
    }

    private static SimpleProjectMember getProjectMember() {
        return (SimpleProjectMember) MyCollabSession.getVariable(PROJECT_MEMBER);
    }

    public static boolean isAdmin() {
        if (AppContext.isAdmin()) {
            return true;
        }
        ProjectMember member = (ProjectMember) MyCollabSession.getVariable(PROJECT_MEMBER);
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
            PermissionMap permissionMap = getProjectMember().getPermissionMaps();
            return (permissionMap != null) && permissionMap.canRead(permissionItem);
        } catch (Exception e) {
            LOG.error("Error while checking permission", e);
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
            PermissionMap permissionMap = getProjectMember().getPermissionMaps();
            return (permissionMap != null) && permissionMap.canWrite(permissionItem);
        } catch (Exception e) {
            LOG.error("Error while checking permission", e);
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
            PermissionMap permissionMap = getProjectMember().getPermissionMaps();
            return (permissionMap != null) && permissionMap.canAccess(permissionItem);
        } catch (Exception e) {
            LOG.error("Error while checking permission", e);
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
        return String.format("%d/project/%d/.page", AppContext.getAccountId(), getProjectId());
    }

    public static void setCurrentPagePath(String path) {
        MyCollabSession.putVariable(CURRENT_PAGE_VAR, path);
    }

    public static int getProjectId() {
        SimpleProject project = getProject();
        return (project != null) ? project.getId() : -1;
    }

    public static String getShortName() {
        SimpleProject project = getProject();
        return (project != null) ? project.getShortname() : "";
    }
}
