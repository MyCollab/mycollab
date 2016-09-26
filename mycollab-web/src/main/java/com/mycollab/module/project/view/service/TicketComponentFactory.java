/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.service;

import com.mycollab.module.project.domain.ProjectTicket;
import com.vaadin.ui.AbstractComponent;
import org.vaadin.viritin.layouts.MWindow;

import java.util.Date;

/**
 * @author MyCollab Ltd
 * @since 5.4.2
 */
public interface TicketComponentFactory {

    AbstractComponent createStartDatePopupField(ProjectTicket ticket);

    AbstractComponent createEndDatePopupField(ProjectTicket ticket);

    AbstractComponent createDueDatePopupField(ProjectTicket ticket);

    AbstractComponent createPriorityPopupField(ProjectTicket ticket);

    AbstractComponent createAssigneePopupField(ProjectTicket ticket);

    AbstractComponent createBillableHoursPopupField(ProjectTicket ticket);

    AbstractComponent createNonBillableHoursPopupField(ProjectTicket ticket);

    AbstractComponent createFollowersPopupField(ProjectTicket ticket);

    AbstractComponent createCommentsPopupField(ProjectTicket ticket);

    AbstractComponent createStatusPopupField(ProjectTicket ticket);

    MWindow createNewTicketWindow(Date date, final Integer prjId, final Integer milestoneId, boolean isIncludeMilestone);
}
