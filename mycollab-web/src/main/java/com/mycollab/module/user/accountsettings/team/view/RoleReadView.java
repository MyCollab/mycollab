package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.module.user.domain.Role;
import com.mycollab.module.user.domain.SimpleRole;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
public interface RoleReadView extends IPreviewView<SimpleRole> {

    HasPreviewFormHandlers<Role> getPreviewFormHandlers();
}
