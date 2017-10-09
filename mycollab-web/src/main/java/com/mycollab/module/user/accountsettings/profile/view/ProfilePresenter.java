package com.mycollab.module.user.accountsettings.profile.view;

import com.mycollab.core.MyCollabException;
import com.mycollab.module.user.accountsettings.view.AccountModule;
import com.mycollab.module.user.ui.SettingUIConstants;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProfilePresenter extends AbstractPresenter<ProfileContainer> {
    private static final long serialVersionUID = 1L;

    public ProfilePresenter() {
        super(ProfileContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        AccountModule accountContainer = (AccountModule) container;
        accountContainer.gotoSubView(SettingUIConstants.PROFILE);

        AbstractPresenter<?> presenter;
        if (data == null) {
            presenter = PresenterResolver.getPresenter(ProfileReadPresenter.class);
        } else {
            throw new MyCollabException("Do not support screen data");
        }

        presenter.go(view, data);
    }

}
