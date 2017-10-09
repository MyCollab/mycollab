package com.mycollab.module.project.view;

import com.mycollab.common.ModuleNameConstants;
import com.mycollab.shell.view.MainView;
import com.mycollab.shell.view.ShellUrlResolver;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectModulePresenter extends AbstractPresenter<ProjectModule> {
    private static final long serialVersionUID = 1L;

    public ProjectModulePresenter() {
        super(ProjectModule.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        MainView mainView = (MainView) container;
        mainView.addModule(view);

        String[] params = (String[]) data.getParams();
        if (params == null || params.length == 0) {
            UserDashboardPresenter presenter = PresenterResolver.getPresenter(UserDashboardPresenter.class);
            presenter.go(view, null);
        } else {
            ShellUrlResolver.ROOT.getSubResolver("project").handle(params);
        }

        UserUIContext.updateLastModuleVisit(ModuleNameConstants.PRJ);
    }
}
