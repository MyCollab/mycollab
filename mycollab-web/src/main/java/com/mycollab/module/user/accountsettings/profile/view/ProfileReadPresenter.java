package com.mycollab.module.user.accountsettings.profile.view;

import com.mycollab.module.user.accountsettings.view.AccountSettingBreadcrumb;
import com.mycollab.module.user.domain.User;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProfileReadPresenter extends AbstractPresenter<ProfileReadView> {
    private static final long serialVersionUID = 1L;

    public ProfileReadPresenter() {
        super(ProfileReadView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        ProfileContainer profileContainer = (ProfileContainer) container;
        profileContainer.removeAllComponents();
        profileContainer.addComponent(view);
        User currentUser = UserUIContext.getUser();
        view.previewItem(currentUser);

        AccountSettingBreadcrumb breadcrumb = ViewManager.getCacheComponent(AccountSettingBreadcrumb.class);
        breadcrumb.gotoProfile();
    }
}
