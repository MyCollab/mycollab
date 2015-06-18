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
package com.esofthead.mycollab.module.project.view.standup

import java.text.ParseException
import java.util.{GregorianCalendar, Date}

import com.esofthead.mycollab.common.UrlTokenizer
import com.esofthead.mycollab.core.arguments.{SearchField, DateSearchField, NumberSearchField}
import com.esofthead.mycollab.eventmanager.EventBusFactory
import com.esofthead.mycollab.module.project.domain.SimpleStandupReport
import com.esofthead.mycollab.module.project.domain.criteria.StandupReportSearchCriteria
import com.esofthead.mycollab.module.project.events.ProjectEvent
import com.esofthead.mycollab.module.project.service.StandupReportService
import com.esofthead.mycollab.module.project.view.ProjectUrlResolver
import com.esofthead.mycollab.module.project.view.parameters.{StandupScreenData, ProjectScreenData}
import com.esofthead.mycollab.spring.ApplicationContextUtil
import com.esofthead.mycollab.vaadin.AppContext
import com.esofthead.mycollab.vaadin.mvp.PageActionChain
import org.apache.commons.lang3.time.FastDateFormat

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
class StandupUrlResolver extends ProjectUrlResolver {
    this.addSubResolver("list", new ListUrlResolver)
    this.addSubResolver("add", new PreviewUrlResolver)

    private val simpleDateFormat: FastDateFormat = FastDateFormat.getInstance("MM/dd/yyyy")

    /**
     *
     * @param dateVal
     * @return
     */
    private def parseDate(dateVal: String): Date = {
        try {
            return simpleDateFormat.parse(dateVal)
        }
        catch {
            case e: ParseException => new GregorianCalendar().getTime
        }
    }

    private class ListUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val token: UrlTokenizer = new UrlTokenizer(params(0))
            val projectId: Integer = token.getInt
            val standupSearchCriteria: StandupReportSearchCriteria = new StandupReportSearchCriteria
            standupSearchCriteria.setProjectId(new NumberSearchField(projectId))
            if (token.hasMoreTokens) {
                val date: Date = parseDate(token.getString)
                standupSearchCriteria.setOnDate(new DateSearchField(date))
            }
            else {
                standupSearchCriteria.setOnDate(new DateSearchField(new GregorianCalendar().getTime))
            }
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                new StandupScreenData.Search(standupSearchCriteria))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class PreviewUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val token: UrlTokenizer = new UrlTokenizer(params(0))
            val projectId: Integer = token.getInt
            val onDate: Date = parseDate(token.getString)
            val reportService: StandupReportService = ApplicationContextUtil.getSpringBean(classOf[StandupReportService])
            var report: SimpleStandupReport = reportService.findStandupReportByDateUser(projectId, AppContext.getUsername,
                onDate, AppContext.getAccountId)
            if (report == null) {
                report = new SimpleStandupReport
            }
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                new StandupScreenData.Add(report))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

}
