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
package com.mycollab.module.project.httpmapping

import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.module.project.event.ReportEvent

/**
  * @author MyCollab Ltd
  * @since 5.2.10
  */
class ReportUrlResolver extends ProjectUrlResolver {
  this.defaultUrlResolver = new DefaultUrlResolver
  this.addSubResolver("standup", new StandupUrlResolver)
  this.addSubResolver("timesheet", new TimesheetUrlResolver)
  this.addSubResolver("weeklytiming", new WeeklyTimingUrlResolver)
  this.addSubResolver("usersworkload", new UsersWorkloadUrlResolver)

  class DefaultUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      EventBusFactory.getInstance().post(new ReportEvent.GotoConsole(this))
    }
  }

  /**
    * @param params
    */
  override protected def handlePage(params: String*): Unit = {
    EventBusFactory.getInstance().post(new ReportEvent.GotoConsole(this))
  }

  class WeeklyTimingUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*): Unit = {
      EventBusFactory.getInstance().post(new ReportEvent.GotoWeeklyTimingReport(this))
    }
  }

  class UsersWorkloadUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*): Unit = {
      EventBusFactory.getInstance().post(new ReportEvent.GotoUserWorkloadReport(this))
    }
  }

}
