package com.mycollab.module.project.view.reports;

import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
public class UserWorkloadReportPresenter extends AbstractPresenter<UserWorkloadReportView> {
    public UserWorkloadReportPresenter() {
        super(UserWorkloadReportView.class);
    }

    @Override
    protected void viewAttached() {
        view.getSearchHandlers().addSearchHandler(this::doSearch);
    }

    public void doSearch(ProjectTicketSearchCriteria searchCriteria) {
        view.queryTickets(searchCriteria);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        IReportContainer reportContainer = (IReportContainer) container;
        reportContainer.addView(view);

        ProjectTicketSearchCriteria searchCriteria = new ProjectTicketSearchCriteria();
        view.queryTickets(searchCriteria);

        ReportBreadcrumb breadCrumb = ViewManager.getCacheComponent(ReportBreadcrumb.class);
        breadCrumb.gotoUserWorkloadReport();
    }
}
