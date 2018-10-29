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

import com.mycollab.module.project.view.bug.BugPresenter
import com.mycollab.module.project.view.file.FilePresenter
import com.mycollab.module.project.view.message.MessagePresenter
import com.mycollab.module.project.view.milestone.MilestonePresenter
import com.mycollab.module.project.view.page.PagePresenter
import com.mycollab.module.project.view.parameters.*
import com.mycollab.module.project.view.risk.IRiskPresenter
import com.mycollab.module.project.view.settings.UserSettingPresenter
import com.mycollab.module.project.view.ticket.TicketPresenter
import com.mycollab.module.project.view.time.IFinancePresenter
import com.mycollab.module.project.view.user.ProjectDashboardPresenter
import com.mycollab.vaadin.mvp.IPresenter
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ProjectPresenterDataMapper {
    private val milestoneMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            MilestoneScreenData.Read::class.java to MilestonePresenter::class.java,
            MilestoneScreenData.Search::class.java to MilestonePresenter::class.java,
            MilestoneScreenData.Add::class.java to MilestonePresenter::class.java,
            MilestoneScreenData.Edit::class.java to MilestonePresenter::class.java,
            MilestoneScreenData.Roadmap::class.java to MilestonePresenter::class.java
    )

    private val messageMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            MessageScreenData.Read::class.java to MessagePresenter::class.java,
            MessageScreenData.Search::class.java to MessagePresenter::class.java)

    private val pageMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            PageScreenData.Read::class.java to PagePresenter::class.java,
            PageScreenData.Add::class.java to PagePresenter::class.java,
            PageScreenData.Edit::class.java to PagePresenter::class.java,
            PageScreenData.Search::class.java to PagePresenter::class.java)

    private val riskMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            RiskScreenData.Add::class.java to IRiskPresenter::class.java,
            RiskScreenData.Edit::class.java to IRiskPresenter::class.java,
            RiskScreenData.Read::class.java to IRiskPresenter::class.java)

    private val taskMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            TaskScreenData.Add::class.java to TicketPresenter::class.java,
            TaskScreenData.Edit::class.java to TicketPresenter::class.java,
            TaskScreenData.Read::class.java to TicketPresenter::class.java,
            TicketScreenData.GotoDashboard::class.java to TicketPresenter::class.java,
            TicketScreenData.GotoKanbanView::class.java to TicketPresenter::class.java)

    private val trackerMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            BugScreenData.Read::class.java to BugPresenter::class.java,
            BugScreenData.Add::class.java to BugPresenter::class.java,
            BugScreenData.Edit::class.java to BugPresenter::class.java)

    private val standupMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            StandupScreenData.Search::class.java to ProjectDashboardPresenter::class.java)

    private val userMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            ProjectMemberScreenData.Add::class.java to UserSettingPresenter::class.java,
            ProjectMemberScreenData.InviteProjectMembers::class.java to UserSettingPresenter::class.java,
            ProjectMemberScreenData.Read::class.java to UserSettingPresenter::class.java,
            ProjectMemberScreenData.Search::class.java to UserSettingPresenter::class.java,
            ProjectRoleScreenData.Add::class.java to UserSettingPresenter::class.java,
            ProjectRoleScreenData.Read::class.java to UserSettingPresenter::class.java,
            ProjectRoleScreenData.Search::class.java to UserSettingPresenter::class.java,
            ProjectSettingScreenData.ViewSettings::class.java to UserSettingPresenter::class.java,
            ComponentScreenData.Add::class.java to UserSettingPresenter::class.java,
            ComponentScreenData.Edit::class.java to UserSettingPresenter::class.java,
            ComponentScreenData.Read::class.java to UserSettingPresenter::class.java,
            ComponentScreenData.Search::class.java to UserSettingPresenter::class.java,
            VersionScreenData.Add::class.java to UserSettingPresenter::class.java,
            VersionScreenData.Edit::class.java to UserSettingPresenter::class.java,
            VersionScreenData.Read::class.java to UserSettingPresenter::class.java,
            VersionScreenData.Search::class.java to UserSettingPresenter::class.java)

    private val timeMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            TimeTrackingScreenData.Search::class.java to IFinancePresenter::class.java,
            InvoiceScreenData.GotoInvoiceList::class.java to IFinancePresenter::class.java)

    private val fileMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            FileScreenData.GotoDashboard::class.java to FilePresenter::class.java)

    private val projectMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            ProjectScreenData.GotoTagList::class.java to ProjectDashboardPresenter::class.java,
            ProjectScreenData.GotoFavorite::class.java to ProjectDashboardPresenter::class.java,
            ProjectScreenData.GotoGanttChart::class.java to ProjectDashboardPresenter::class.java,
            ProjectScreenData.GotoCalendarView::class.java to ProjectDashboardPresenter::class.java,
            ProjectScreenData.GotoReportConsole::class.java to ProjectDashboardPresenter::class.java,
            ProjectScreenData.SearchItem::class.java to UserProjectDashboardPresenter::class.java,
            ProjectScreenData.Edit::class.java to ProjectDashboardPresenter::class.java,
            ReportScreenData.GotoWeeklyTiming::class.java to ProjectDashboardPresenter::class.java)

    private val allMapper = milestoneMapper + messageMapper + pageMapper + riskMapper + taskMapper +
            trackerMapper + standupMapper + userMapper + timeMapper + fileMapper + projectMapper

    @JvmStatic
    fun presenter(screenData: ScreenData<Any>): Class<out IPresenter<*>>? = allMapper[screenData.javaClass]
}