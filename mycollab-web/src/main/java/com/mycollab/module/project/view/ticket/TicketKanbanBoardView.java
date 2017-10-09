package com.mycollab.module.project.view.ticket;

import com.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.mycollab.module.project.view.IKanbanView;
import com.mycollab.vaadin.event.HasSearchHandlers;
import com.mycollab.vaadin.mvp.PageView;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public interface TicketKanbanBoardView extends PageView, IKanbanView {
    HasSearchHandlers<TaskSearchCriteria> getSearchHandlers();

    void display();

    void queryTask(TaskSearchCriteria searchCriteria);
}
