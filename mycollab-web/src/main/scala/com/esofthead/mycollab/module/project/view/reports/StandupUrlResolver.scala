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
package com.esofthead.mycollab.module.project.view.reports

import java.text.ParseException
import java.util.{Date, GregorianCalendar}

import com.esofthead.mycollab.core.arguments.DateSearchField
import com.esofthead.mycollab.eventmanager.EventBusFactory
import com.esofthead.mycollab.module.project.domain.criteria.StandupReportSearchCriteria
import com.esofthead.mycollab.module.project.events.StandUpEvent
import com.esofthead.mycollab.module.project.view.ProjectUrlResolver
import org.joda.time.format.DateTimeFormat

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class StandupUrlResolver extends ProjectUrlResolver {
  this.addSubResolver("list", new ListUrlResolver)

  private val simpleDateFormat = DateTimeFormat.forPattern("MM-dd-yyyy");

  /**
    * @param dateVal
    * @return
    */
  private def parseDate(dateVal: String): Date = {
    try {
      return simpleDateFormat.parseDateTime(dateVal).toDate
    } catch {
      case e: ParseException => new GregorianCalendar().getTime
    }
  }

  private class ListUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val searchCriteria = new StandupReportSearchCriteria
      if (params.length == 0) {
        searchCriteria.setOnDate(new DateSearchField(new GregorianCalendar().getTime))
      } else {
        val date = parseDate(params(0))
        searchCriteria.setOnDate(new DateSearchField(date))
      }
      EventBusFactory.getInstance().post(new StandUpEvent.GotoList(this, searchCriteria))
    }
  }

}
