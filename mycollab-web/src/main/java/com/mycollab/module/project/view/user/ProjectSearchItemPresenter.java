package com.mycollab.module.project.view.user;

import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public class ProjectSearchItemPresenter extends AbstractPresenter<ProjectSearchItemsView> {

    public ProjectSearchItemPresenter() {
        super(ProjectSearchItemsView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        ProjectDashboardContainer projectViewContainer = (ProjectDashboardContainer) container;
        projectViewContainer.setContent(view);
        String params = (String) data.getParams();
        view.displayResults(params);

        ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
        breadcrumb.gotoSearchProjectItems();
    }
}
