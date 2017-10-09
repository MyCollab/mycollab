package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.domain.User;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.IPreviewView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface UserReadView extends IPreviewView<SimpleUser> {

    HasPreviewFormHandlers<User> getPreviewFormHandlers();

}
