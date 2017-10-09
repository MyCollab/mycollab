package com.mycollab.module.project.event

import com.mycollab.vaadin.event.ApplicationEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ReportEvent {
    class GotoConsole(source: Any) : ApplicationEvent(source)

    class GotoTimesheetReport(source: Any) : ApplicationEvent(source)

    class GotoWeeklyTimingReport(source: Any) : ApplicationEvent(source)

    class GotoUserWorkloadReport(source: Any) : ApplicationEvent(source)
}