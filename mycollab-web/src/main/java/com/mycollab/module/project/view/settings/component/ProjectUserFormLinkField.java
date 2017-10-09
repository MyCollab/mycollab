package com.mycollab.module.project.view.settings.component;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectUserFormLinkField extends CustomField {
    private static final long serialVersionUID = 1L;

    private Integer projectId;
    private String username;
    private String userAvatarId;
    private String displayName;

    public ProjectUserFormLinkField(Integer projectId, String username, String userAvatarId, String displayName) {
        this.projectId = projectId;
        this.username = username;
        this.userAvatarId = userAvatarId;
        this.displayName = displayName;
    }

    @Override
    public Class<?> getType() {
        return Object.class;
    }

    @Override
    protected Component initContent() {
        return new ProjectUserLink(projectId, username, userAvatarId, displayName);
    }
}
