package com.mycollab.module.project.view;

import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.view.parameters.ProjectScreenData;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewScope;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class UserDashboardPresenter extends AbstractPresenter<UserDashboardView> {
    private static final long serialVersionUID = 1L;

    public UserDashboardPresenter() {
        super(UserDashboardView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        ProjectModule prjContainer = (ProjectModule) container;
        prjContainer.setContent(view);
        if (data instanceof ProjectScreenData.GotoList) {
            view.showProjectList();
        } else {
            view.showDashboard();
        }
        AppUI.addFragment("project", UserUIContext.getMessage(ProjectI18nEnum.SINGLE));
    }
}
