/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.common.domain;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleMonitorItem extends MonitorItem {
    private static final long serialVersionUID = 1L;

    private String userAvatarId;
    private String userFullname;

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public String getUserFullname() {
        if (userFullname == null || userFullname.trim().equals("")) {
            String displayName = getUser();
            int index = (displayName != null) ? displayName.indexOf("@") : 0;
            if (index > 0) {
                return displayName.substring(0, index);
            }
        }
        return userFullname;
    }

    public String getUserAvatarId() {
        return userAvatarId;
    }

    public void setUserAvatarId(String userAvatarId) {
        this.userAvatarId = userAvatarId;
    }
}
