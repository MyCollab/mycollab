package com.mycollab.module.project.view.assignments;

import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.vaadin.event.HasSearchHandlers;
import com.mycollab.vaadin.mvp.LazyPageView;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
public interface CalendarView extends LazyPageView {
    void queryAssignments(ProjectTicketSearchCriteria criteria);

    HasSearchHandlers<ProjectTicketSearchCriteria> getSearchHandlers();
}
