package com.mycollab.module.project.view.service

import com.mycollab.module.tracker.domain.SimpleBug
import com.mycollab.vaadin.mvp.CacheableComponent
import com.vaadin.ui.AbstractComponent

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
interface BugComponentFactory {
    fun createPriorityPopupField(bug: SimpleBug): AbstractComponent

    fun createAssigneePopupField(bug: SimpleBug): AbstractComponent

    fun createCommentsPopupField(bug: SimpleBug): AbstractComponent

    fun createStatusPopupField(bug: SimpleBug): AbstractComponent

    fun createMilestonePopupField(bug: SimpleBug): AbstractComponent

    fun createDeadlinePopupField(bug: SimpleBug): AbstractComponent

    fun createStartDatePopupField(bug: SimpleBug): AbstractComponent

    fun createEndDatePopupField(bug: SimpleBug): AbstractComponent

    fun createBillableHoursPopupField(bug: SimpleBug): AbstractComponent

    fun createNonbillableHoursPopupField(bug: SimpleBug): AbstractComponent

    fun createFollowersPopupField(bug: SimpleBug): AbstractComponent
}
