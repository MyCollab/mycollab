package com.mycollab.module.project.view;

import com.mycollab.module.project.view.client.IClientPresenter;
import com.mycollab.module.project.view.parameters.*;
import com.mycollab.module.project.view.reports.IReportPresenter;
import com.mycollab.module.project.view.user.ProjectSearchItemPresenter;
import com.mycollab.vaadin.mvp.IPresenter;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

public class BoardContainerPresenter extends AbstractPresenter<BoardContainer> {

    public BoardContainerPresenter() {
        super(BoardContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        ProjectModule module = (ProjectModule) container;
        module.setContent(view);

        IPresenter<?> presenter;
        if (data instanceof ProjectScreenData.GotoList) {
            presenter = PresenterResolver.getPresenter(ProjectListPresenter.class);

        } else if (data instanceof ReportScreenData.GotoConsole || data instanceof ReportScreenData.GotoWeeklyTiming
                || data instanceof ReportScreenData.GotoUserWorkload || data instanceof ReportScreenData.GotoTimesheet
                || data instanceof StandupScreenData.Search) {
            presenter = PresenterResolver.getPresenter(IReportPresenter.class);
        } else if (data instanceof ClientScreenData.Add || data instanceof ClientScreenData.Edit || data instanceof ClientScreenData.Read || data instanceof ClientScreenData.Search) {
            presenter = PresenterResolver.getPresenter(IClientPresenter.class);
        } else if (data instanceof ProjectModuleScreenData.SearchItem) {
            presenter = PresenterResolver.getPresenter(ProjectSearchItemPresenter.class);
        } else {
            presenter = PresenterResolver.getPresenter(UserProjectDashboardPresenter.class);
        }
        presenter.go(view, data);
    }
}
