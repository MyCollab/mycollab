package com.mycollab.module.project.view

import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.project.event.ReportEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class TimesheetUrlResolver : ProjectUrlResolver() {

    override fun handlePage(vararg params: String) {
        EventBusFactory.getInstance().post(ReportEvent.GotoTimesheetReport(this))
    }
}