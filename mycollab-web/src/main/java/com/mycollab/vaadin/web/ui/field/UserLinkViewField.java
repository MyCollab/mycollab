package com.mycollab.vaadin.web.ui.field;

import com.mycollab.vaadin.web.ui.UserLink;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class UserLinkViewField extends CustomField {
    private static final long serialVersionUID = 1L;

    private String username;
    private String userAvatarId;
    private String fullName;

    public UserLinkViewField(String username, String userAvatarId, String fullName) {
        this.username = username;
        this.userAvatarId = userAvatarId;
        this.fullName = fullName;
    }

    @Override
    public Class<?> getType() {
        return Object.class;
    }

    @Override
    protected Component initContent() {
        return new UserLink(username, userAvatarId, fullName);
    }
}
