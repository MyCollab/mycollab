/**
 * This file is part of mycollab-dao.
 *
 * mycollab-dao is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-dao is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-dao.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.security;

import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.core.utils.JsonDeSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Map contains all permissions in MyCollab, it is used to all permissions if
 * logged in user.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class PermissionMap extends ValuedBean {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> perMap = new HashMap<>();

    /**
     * @param permissionItem
     * @param value
     */
    public void addPath(String permissionItem, Integer value) {
        perMap.put(permissionItem, value);
    }

    /**
     * @param permissionItem
     * @return
     */
    public Integer getPermissionFlag(String permissionItem) {
        Object value = perMap.get(permissionItem);
        if (value == null) {
            return AccessPermissionFlag.NO_ACCESS;
        }

        return (Integer) value;
    }

    /**
     * @param permissionItem
     * @return
     */
    public Integer get(String permissionItem) {
        return perMap.get(permissionItem);
    }

    /**
     * @param permissionItem
     * @return
     */
    public boolean canBeYes(String permissionItem) {
        Object value = perMap.get(permissionItem);
        if (value == null) {
            return false;
        } else {
            return BooleanPermissionFlag.beTrue((Integer) value);
        }
    }

    /**
     * @param permissionItem
     * @return
     */
    public boolean canBeFalse(String permissionItem) {
        Object value = perMap.get(permissionItem);
        if (value == null) {
            return false;
        } else {
            return BooleanPermissionFlag.beFalse((Integer) value);
        }
    }

    /**
     * @param permissionItem
     * @return
     */
    public boolean canRead(String permissionItem) {
        Object value = perMap.get(permissionItem);
        if (value == null) {
            return false;
        } else {
            return AccessPermissionFlag.canRead((Integer) value);
        }
    }

    /**
     * @param permissionItem
     * @return
     */
    public boolean canWrite(String permissionItem) {
        Object value = perMap.get(permissionItem);
        if (value == null) {
            return false;
        } else {
            return AccessPermissionFlag.canWrite((Integer) value);
        }
    }

    /**
     * @param permissionItem
     * @return
     */
    public boolean canAccess(String permissionItem) {
        Object value = perMap.get(permissionItem);
        if (value == null) {
            return false;
        } else {
            return AccessPermissionFlag.canAccess((Integer) value);
        }
    }

    /**
     * @return
     */
    public String toJsonString() {
        return JsonDeSerializer.toJson(this);
    }

    /**
     * @param json
     * @return
     */
    public static PermissionMap fromJsonString(String json) {
        return JsonDeSerializer.fromJson(json, PermissionMap.class);
    }

    /**
     * @return
     */
    public static PermissionMap buildAdminPermissionCollection() {
        PermissionMap permissionMap = new PermissionMap();
        for (PermissionDefItem element : RolePermissionCollections.CRM_PERMISSIONS_ARR) {
            permissionMap.addPath(element.getKey(), AccessPermissionFlag.ACCESS);
        }

        for (final PermissionDefItem element : RolePermissionCollections.ACCOUNT_PERMISSION_ARR) {
            if (element.getKey().equals(RolePermissionCollections.ACCOUNT_BILLING) ||
                    element.getKey().equals(RolePermissionCollections.ACCOUNT_THEME)) {
                permissionMap.addPath(element.getKey(), BooleanPermissionFlag.TRUE);
            } else {
                permissionMap.addPath(element.getKey(), AccessPermissionFlag.ACCESS);
            }
        }

        for (final PermissionDefItem element : RolePermissionCollections.PROJECT_PERMISSION_ARR) {
            permissionMap.addPath(element.getKey(), BooleanPermissionFlag.TRUE);
        }

        for (final PermissionDefItem element : RolePermissionCollections.DOCUMENT_PERMISSION_ARR) {
            permissionMap
                    .addPath(element.getKey(), AccessPermissionFlag.ACCESS);
        }
        return permissionMap;
    }

    /**
     * @return
     */
    public static PermissionMap buildEmployeePermissionCollection() {
        PermissionMap permissionMap = new PermissionMap();
        for (PermissionDefItem element : RolePermissionCollections.CRM_PERMISSIONS_ARR) {
            permissionMap.addPath(element.getKey(),
                    AccessPermissionFlag.READ_ONLY);
        }

        for (final PermissionDefItem element : RolePermissionCollections.ACCOUNT_PERMISSION_ARR) {
            if (element.getKey().equals(RolePermissionCollections.ACCOUNT_BILLING) ||
                    element.getKey().equals(RolePermissionCollections.ACCOUNT_THEME)) {
                permissionMap.addPath(element.getKey(), BooleanPermissionFlag.FALSE);
            } else {
                permissionMap.addPath(element.getKey(), AccessPermissionFlag.READ_ONLY);
            }
        }

        for (PermissionDefItem element : RolePermissionCollections.PROJECT_PERMISSION_ARR) {
            permissionMap.addPath(element.getKey(), BooleanPermissionFlag.FALSE);
        }

        for (PermissionDefItem element : RolePermissionCollections.DOCUMENT_PERMISSION_ARR) {
            permissionMap.addPath(element.getKey(), AccessPermissionFlag.READ_WRITE);
        }
        return permissionMap;
    }

    /**
     * @return
     */
    public static PermissionMap buildGuestPermissionCollection() {
        PermissionMap permissionMap = new PermissionMap();
        for (PermissionDefItem element : RolePermissionCollections.CRM_PERMISSIONS_ARR) {
            permissionMap.addPath(element.getKey(), AccessPermissionFlag.NO_ACCESS);
        }

        for (PermissionDefItem element : RolePermissionCollections.ACCOUNT_PERMISSION_ARR) {
            if (element.getKey().equals(RolePermissionCollections.ACCOUNT_BILLING) ||
                    element.getKey().equals(RolePermissionCollections.ACCOUNT_THEME)) {
                permissionMap.addPath(element.getKey(), BooleanPermissionFlag.FALSE);
            } else {
                permissionMap.addPath(element.getKey(), AccessPermissionFlag.NO_ACCESS);
            }
        }

        for (PermissionDefItem element : RolePermissionCollections.PROJECT_PERMISSION_ARR) {
            permissionMap.addPath(element.getKey(), BooleanPermissionFlag.FALSE);
        }

        for (PermissionDefItem element : RolePermissionCollections.DOCUMENT_PERMISSION_ARR) {
            permissionMap.addPath(element.getKey(), AccessPermissionFlag.NO_ACCESS);
        }
        return permissionMap;
    }
}
