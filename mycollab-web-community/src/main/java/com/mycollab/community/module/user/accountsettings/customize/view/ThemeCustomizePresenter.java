package com.mycollab.community.module.user.accountsettings.customize.view;

import com.mycollab.module.user.accountsettings.customize.view.IThemeCustomizePresenter;
import com.mycollab.module.user.accountsettings.customize.view.IThemeCustomizeView;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public class ThemeCustomizePresenter extends AbstractPresenter<IThemeCustomizeView> implements IThemeCustomizePresenter {
    public ThemeCustomizePresenter() {
        super(IThemeCustomizeView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {

    }
}
