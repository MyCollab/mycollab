package com.mycollab.module.user.accountsettings.view;

import com.mycollab.common.ModuleNameConstants;
import com.mycollab.shell.view.MainView;
import com.mycollab.shell.view.ShellUrlResolver;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class AccountModulePresenter extends AbstractPresenter<AccountModule> {
    private static final long serialVersionUID = 1L;

    public AccountModulePresenter() {
        super(AccountModule.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        UserUIContext.updateLastModuleVisit(ModuleNameConstants.ACCOUNT);
        MainView mainView = (MainView) container;
        mainView.addModule(view);

        String[] params = (String[]) data.getParams();
        if (params == null || params.length == 0) {
            view.gotoUserProfilePage();
        } else {
            ShellUrlResolver.ROOT.getSubResolver("account").handle(params);
        }
    }
}
