package com.mycollab.community.module.user.accountsettings.customize.view;

import com.mycollab.module.user.accountsettings.customize.view.IThemeCustomizeView;
import com.mycollab.module.user.domain.AccountTheme;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.NotPresentedView;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
@ViewComponent
public class ThemeCustomizeView extends NotPresentedView implements IThemeCustomizeView {
    @Override
    public void customizeTheme(AccountTheme accountTheme) {

    }
}
