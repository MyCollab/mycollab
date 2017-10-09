package com.mycollab.module.project.view.ticket;

import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.vaadin.web.ui.WebThemes;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
class SimpleListOrderComponent extends TicketGroupOrderComponent {
    SimpleListOrderComponent() {
        this.addStyleName(WebThemes.BORDER_LIST);
    }

    @Override
    public void insertTickets(List<ProjectTicket> tickets) {
        for (ProjectTicket ticket : tickets) {
            this.addComponent(new TicketRowRenderer(ticket));
        }
    }
}