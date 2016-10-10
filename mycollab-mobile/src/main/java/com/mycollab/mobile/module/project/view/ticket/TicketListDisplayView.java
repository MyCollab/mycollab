/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.view.ticket;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.i18n.TicketI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.BeanList;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class TicketListDisplayView extends AbstractMobilePageView {
    private Integer milestoneId;
    private final BeanList<ProjectTicketService, ProjectTicketSearchCriteria, ProjectTicket> ticketList;

    public TicketListDisplayView(Integer milestoneId) {
        this.milestoneId = milestoneId;
        ticketList = new BeanList<>(AppContextUtil.getSpringBean(ProjectTicketService.class), new TicketRowDisplayHandler());
        ticketList.setDisplayEmptyListText(false);
        this.setContent(ticketList);
        displayTickets();
    }

    private void displayTickets() {
        ProjectTicketSearchCriteria criteria = new ProjectTicketSearchCriteria();
        criteria.setMilestoneId(NumberSearchField.equal(milestoneId));
        criteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK,
                ProjectTypeConstants.RISK));
        int numTickets = ticketList.setSearchCriteria(criteria);
        this.setCaption(UserUIContext.getMessage(TicketI18nEnum.OPT_TICKETS_VALUE, numTickets));
    }
}
