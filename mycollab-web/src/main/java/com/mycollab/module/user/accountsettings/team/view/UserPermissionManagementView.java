package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.vaadin.mvp.PageView;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public interface UserPermissionManagementView extends PageView {
    Component gotoSubView(String name);
}
