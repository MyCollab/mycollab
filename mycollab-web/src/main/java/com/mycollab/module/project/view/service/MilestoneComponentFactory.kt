package com.mycollab.module.project.view.service

import com.mycollab.module.project.domain.SimpleMilestone
import com.vaadin.ui.AbstractComponent

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
interface MilestoneComponentFactory {
    fun createMilestoneAssigneePopupField(milestone: SimpleMilestone, isDisplayName: Boolean): AbstractComponent

    fun createStartDatePopupField(milestone: SimpleMilestone): AbstractComponent

    fun createEndDatePopupField(milestone: SimpleMilestone): AbstractComponent

    fun createBillableHoursPopupField(milestone: SimpleMilestone): AbstractComponent

    fun createNonBillableHoursPopupField(milestone: SimpleMilestone): AbstractComponent
}
