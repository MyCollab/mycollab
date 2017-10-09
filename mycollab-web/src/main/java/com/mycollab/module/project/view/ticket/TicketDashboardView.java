package com.mycollab.module.project.view.ticket;

import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.vaadin.mvp.PageView;
import com.mycollab.vaadin.web.ui.IListView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface TicketDashboardView extends IListView<ProjectTicketSearchCriteria, ProjectTicket>, PageView {
    void displayView(String query);

    void queryTickets(ProjectTicketSearchCriteria searchCriteria);

    ProjectTicketSearchCriteria getCriteria();
}
