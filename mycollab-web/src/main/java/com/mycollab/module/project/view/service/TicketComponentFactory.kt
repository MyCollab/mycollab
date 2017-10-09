package com.mycollab.module.project.view.service

import com.mycollab.module.project.domain.ProjectTicket
import com.vaadin.ui.AbstractComponent
import org.vaadin.viritin.layouts.MWindow

import java.util.Date

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

    fun createNewTicketWindow(date: Date?, prjId: Int?, milestoneId: Int?, isIncludeMilestone: Boolean): MWindow
}
