package com.mycollab.mobile.module.project.view.ticket;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.i18n.TicketI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class TicketListDisplayView extends AbstractMobilePageView {
    private Integer milestoneId;
    private final DefaultPagedBeanList<ProjectTicketService, ProjectTicketSearchCriteria, ProjectTicket> ticketList;

    public TicketListDisplayView(Integer milestoneId) {
        this.milestoneId = milestoneId;
        ticketList = new DefaultPagedBeanList<>(AppContextUtil.getSpringBean(ProjectTicketService.class), new
                TicketRowDisplayHandler());
        this.setContent(ticketList);
        displayTickets();
    }

    private void displayTickets() {
        ProjectTicketSearchCriteria criteria = new ProjectTicketSearchCriteria();
        criteria.setMilestoneId(NumberSearchField.equal(milestoneId));
        criteria.setTypes(CurrentProjectVariables.getRestrictedTicketTypes());
        Integer numTickets = ticketList.search(criteria);
        this.setCaption(UserUIContext.getMessage(TicketI18nEnum.OPT_TICKETS_VALUE, numTickets));
    }
}
