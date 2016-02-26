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
package com.esofthead.mycollab.common.domain;

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
