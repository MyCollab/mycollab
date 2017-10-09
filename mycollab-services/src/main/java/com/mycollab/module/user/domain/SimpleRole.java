package com.mycollab.module.user.domain;

import com.mycollab.core.arguments.NotBindable;
import com.mycollab.core.reporting.NotInReport;
import com.mycollab.security.PermissionMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
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
        return Boolean.TRUE.equals(getIssystemrole());
    }
}
