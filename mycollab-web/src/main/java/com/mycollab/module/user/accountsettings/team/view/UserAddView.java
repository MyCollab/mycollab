package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.vaadin.event.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.IFormAddView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface UserAddView extends IFormAddView<SimpleUser> {

    HasEditFormHandlers<SimpleUser> getEditFormHandlers();

    void editItem(SimpleUser item, boolean isBasicForm);
}
