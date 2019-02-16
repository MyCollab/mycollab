package com.mycollab.community.module.project.view.reports;

import com.mycollab.module.project.view.BoardContainer;
import com.mycollab.module.project.view.parameters.ReportScreenData;
import com.mycollab.module.project.view.parameters.StandupScreenData;
import com.mycollab.module.project.view.reports.*;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
public class ReportPresenter extends AbstractPresenter<IReportContainer> implements IReportPresenter {
    public ReportPresenter() {
        super(IReportContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        BoardContainer boardContainer = (BoardContainer) container;
        boardContainer.gotoSubView("Reports", view);

        if (data instanceof StandupScreenData.Search) {
            StandupListPresenter presenter = PresenterResolver.getPresenter(StandupListPresenter.class);
            presenter.go(view, data);
        } else if (data instanceof ReportScreenData.GotoUserWorkload) {
            UserWorkloadReportPresenter userWorkloadReportPresenter = PresenterResolver.getPresenter(UserWorkloadReportPresenter.class);
            userWorkloadReportPresenter.go(view, data);
        } else {
            view.showDashboard();
            ReportBreadcrumb breadcrumb = ViewManager.getCacheComponent(ReportBreadcrumb.class);
            breadcrumb.gotoReportDashboard();
        }
    }
}
