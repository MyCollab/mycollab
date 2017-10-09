package com.mycollab.module.project.view.file;

import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class FileDashboardPresenter extends AbstractPresenter<FileDashboardView> {
    private static final long serialVersionUID = 1L;

    public FileDashboardPresenter() {
        super(FileDashboardView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        FileContainer projectViewContainer = (FileContainer) container;
        projectViewContainer.setContent(view);

        view.displayProjectFiles();

        ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
        breadcrumb.gotoFileList();
    }
}