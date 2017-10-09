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
