/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.service

import com.mycollab.module.project.domain.ProjectTicket
import com.vaadin.ui.AbstractComponent
import org.vaadin.viritin.layouts.MWindow
import java.time.LocalDateTime

/**
 * @author MyCollab Ltd
 * @since 5.4.2
 */
interface TicketComponentFactory {

    fun createStartDatePopupField(ticket: ProjectTicket): AbstractComponent

    fun createEndDatePopupField(ticket: ProjectTicket): AbstractComponent

    fun createDueDatePopupField(ticket: ProjectTicket): AbstractComponent

    fun createPriorityPopupField(ticket: ProjectTicket): AbstractComponent

    fun createAssigneePopupField(ticket: ProjectTicket): AbstractComponent

    fun createBillableHoursPopupField(ticket: ProjectTicket): AbstractComponent

    fun createNonBillableHoursPopupField(ticket: ProjectTicket): AbstractComponent

    fun createFollowersPopupField(ticket: ProjectTicket): AbstractComponent

    fun createCommentsPopupField(ticket: ProjectTicket): AbstractComponent

    fun createStatusPopupField(ticket: ProjectTicket): AbstractComponent

    fun createNewTicketWindow(date: LocalDateTime?, prjId: Int?, milestoneId: Int?, isIncludeMilestone: Boolean): MWindow
}
