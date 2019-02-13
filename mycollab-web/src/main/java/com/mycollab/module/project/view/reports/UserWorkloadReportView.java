package com.mycollab.module.project.view.reports;

import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.vaadin.event.HasSearchHandlers;
import com.mycollab.vaadin.mvp.PageView;

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
public interface UserWorkloadReportView extends PageView {

    void queryTickets(ProjectTicketSearchCriteria searchCriteria);

    ProjectTicketSearchCriteria getCriteria();

    HasSearchHandlers<ProjectTicketSearchCriteria> getSearchHandlers();
}
