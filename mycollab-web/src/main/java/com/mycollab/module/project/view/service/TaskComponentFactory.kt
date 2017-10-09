package com.mycollab.module.project.view.service

import com.mycollab.module.project.domain.SimpleTask
import com.mycollab.vaadin.mvp.CacheableComponent
import com.vaadin.ui.AbstractComponent

/**
 * @author MyCollab Ltd
 * @since 5.1.2
 */
interface TaskComponentFactory {

    fun createAssigneePopupField(task: SimpleTask): AbstractComponent

    fun createPriorityPopupField(task: SimpleTask): AbstractComponent

    fun createCommentsPopupField(task: SimpleTask): AbstractComponent

    fun createStatusPopupField(task: SimpleTask): AbstractComponent

    fun createMilestonePopupField(task: SimpleTask): AbstractComponent

    fun createPercentagePopupField(task: SimpleTask): AbstractComponent

    fun createDeadlinePopupField(task: SimpleTask): AbstractComponent

    fun createStartDatePopupField(task: SimpleTask): AbstractComponent

    fun createEndDatePopupField(task: SimpleTask): AbstractComponent

    fun createBillableHoursPopupField(task: SimpleTask): AbstractComponent

    fun createNonBillableHoursPopupField(task: SimpleTask): AbstractComponent

    fun createFollowersPopupField(task: SimpleTask): AbstractComponent
}
