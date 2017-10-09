package com.mycollab.module.project.view

import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.project.event.ReportEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ReportUrlResolver : ProjectUrlResolver() {
    init {
        this.defaultUrlResolver = DefaultUrlResolver()
        this.addSubResolver("standup", StandupUrlResolver())
        this.addSubResolver("timesheet", TimesheetUrlResolver())
        this.addSubResolver("weeklytiming", WeeklyTimingUrlResolver())
        this.addSubResolver("usersworkload", UsersWorkloadUrlResolver())
    }

    class DefaultUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(ReportEvent.GotoConsole(this))
    }

    /**
     * @param params
     */
    override fun handlePage(vararg params: String) =
            EventBusFactory.getInstance().post(ReportEvent.GotoConsole(this))

    class WeeklyTimingUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(ReportEvent.GotoWeeklyTimingReport(this))
    }

    class UsersWorkloadUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(ReportEvent.GotoUserWorkloadReport(this))
    }

}