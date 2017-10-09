package com.mycollab.module.project.view.ticket;

import com.mycollab.core.SecureAccessException;
import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.project.view.ProjectGenericListPresenter;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class TicketDashboardPresenter extends ProjectGenericListPresenter<TicketDashboardView, ProjectTicketSearchCriteria, ProjectTicket> {
    private static final long serialVersionUID = 1L;

    private ProjectTicketService projectTicketService;

    public TicketDashboardPresenter() {
        super(TicketDashboardView.class);
        projectTicketService = AppContextUtil.getSpringBean(ProjectTicketService.class);
    }

    @Override
    public void doSearch(ProjectTicketSearchCriteria searchCriteria) {
        view.queryTickets(searchCriteria);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canReadTicket()) {
            TicketContainer ticketContainer = (TicketContainer) container;
            ticketContainer.setContent(view);
            String query = (data != null && data.getParams() instanceof String) ? (String) data.getParams() : "";
            view.displayView(query);

            ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            breadCrumb.gotoTicketDashboard(query);
        } else {
            throw new SecureAccessException();
        }
    }

    @Override
    public ISearchableService<ProjectTicketSearchCriteria> getSearchService() {
        return projectTicketService;
    }

    @Override
    protected void deleteSelectedItems() {

    }
}
