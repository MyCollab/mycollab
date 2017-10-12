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