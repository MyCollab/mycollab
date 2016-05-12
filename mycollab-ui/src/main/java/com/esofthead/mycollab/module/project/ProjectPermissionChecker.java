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

import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.security.PermissionMap;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * @author MyCollab Ltd
 * @since 5.2.8
 */
public class ProjectPermissionChecker {
    private static ProjectMemberService getMemberService() {
        return AppContextUtil.getSpringBean(ProjectMemberService.class);
    }

    public static boolean canWrite(Integer prjId, String permissionItem) {
        SimpleProjectMember member = getMemberService().findMemberByUsername(AppContext.getUsername(), prjId, AppContext.getAccountId());
        if (member != null) {
            if (member.isProjectOwner()) {
                return true;
            } else {
                PermissionMap permissionMap = member.getPermissionMaps();
                return (permissionMap != null) && permissionMap.canWrite(permissionItem);
            }
        } else {
            return false;
        }
    }
}
