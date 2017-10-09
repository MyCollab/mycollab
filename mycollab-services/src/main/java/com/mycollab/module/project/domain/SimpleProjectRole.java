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
