package com.mycollab.module.project.view.settings;

import com.mycollab.module.project.domain.ProjectRole;
import com.mycollab.security.PermissionMap;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ProjectRoleAddView extends IFormAddView<ProjectRole> {

    HasEditFormHandlers<ProjectRole> getEditFormHandlers();

    PermissionMap getPermissionMap();
}
