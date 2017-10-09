package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.module.user.domain.Role;
import com.mycollab.security.PermissionMap;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface RoleAddView extends IFormAddView<Role> {

    HasEditFormHandlers<Role> getEditFormHandlers();

    PermissionMap getPermissionMap();

}
