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
package com.mycollab.module.project.domain;

import com.mycollab.core.arguments.NotBindable;
import com.mycollab.core.reporting.NotInReport;
import com.mycollab.security.PermissionMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleProjectRole extends ProjectRole {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(SimpleProjectRole.class);

    private String permissionVal;

    @NotBindable
    @NotInReport
    private PermissionMap permissionMap;

    public String getPermissionVal() {
        return permissionVal;
    }

    public void setPermissionVal(String permissionVal) {
        this.permissionVal = permissionVal;
    }

    public PermissionMap getPermissionMap() {
        if (permissionMap == null) {

            if (StringUtils.isBlank(permissionVal)) {
                permissionMap = new PermissionMap();
            } else {
                try {
                    permissionMap = PermissionMap.fromJsonString(permissionVal);
                } catch (Exception e) {
                    LOG.error("Error while get permission", e);
                }
            }
        }
        return permissionMap;
    }
}
