package com.mycollab.module.project.view;

import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewScope;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd
 * @since 5.2.4
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class UserProjectDashboardPresenter extends AbstractPresenter<UserProjectDashboardView> {
    public UserProjectDashboardPresenter() {
        super(UserProjectDashboardView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData data) {
        ComponentContainer componentContainer = (ComponentContainer) container;
        componentContainer.removeAllComponents();
        componentContainer.addComponent(view);
        view.lazyLoadView();
    }
}
