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

package com.esofthead.mycollab.module.user.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.arguments.NotBindable;
import com.esofthead.mycollab.core.reporting.NotInReport;
import com.esofthead.mycollab.security.PermissionMap;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleRole extends Role {
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(SimpleRole.class);

	public static final String ADMIN = "Administrator";
	public static final String EMPLOYEE = "Employee";
	public static final String GUEST = "Guest";

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

			if (permissionVal == null || "".equals(permissionVal)) {
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

    public boolean isSystemRole() {
        return (getIssystemrole() != null && getIssystemrole() == Boolean.TRUE);
    }
}
