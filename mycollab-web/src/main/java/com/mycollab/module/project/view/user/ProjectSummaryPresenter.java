package com.mycollab.module.project.view.user;

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
public class ProjectSummaryPresenter extends AbstractPresenter<ProjectSummaryView> {
    private static final long serialVersionUID = 1L;

    public ProjectSummaryPresenter() {
        super(ProjectSummaryView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        ProjectDashboardContainer projectViewContainer = (ProjectDashboardContainer) container;
        projectViewContainer.setContent(view);
        view.lazyLoadView();
    }
}
