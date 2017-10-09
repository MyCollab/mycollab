package com.mycollab.module.project.view.ticket;

import com.mycollab.module.project.domain.ProjectTicket;
import com.vaadin.ui.CssLayout;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
abstract public class TicketGroupOrderComponent extends CssLayout {
    public TicketGroupOrderComponent() {
        this.setWidth("100%");
    }

    abstract public void insertTickets(List<ProjectTicket> tickets);
}
