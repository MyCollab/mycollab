package com.mycollab.module.crm.view;

import com.mycollab.common.ModuleNameConstants;
import com.mycollab.shell.view.MainView;
import com.mycollab.shell.view.ShellUrlResolver;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CrmModulePresenter extends AbstractPresenter<CrmModule> {
    private static final long serialVersionUID = 1L;

    public CrmModulePresenter() {
        super(CrmModule.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        MainView mainView = (MainView) container;
        mainView.addModule(view);

        String[] params = (String[]) data.getParams();
        if (params == null || params.length == 0) {
            view.gotoCrmDashboard();
        } else {
            ShellUrlResolver.ROOT.getSubResolver("crm").handle(params);
        }

        UserUIContext.updateLastModuleVisit(ModuleNameConstants.CRM);
    }
}
