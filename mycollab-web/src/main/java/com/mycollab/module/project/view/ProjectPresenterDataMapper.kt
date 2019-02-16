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

import com.mycollab.module.project.view.bug.BugAddPresenter
import com.mycollab.module.project.view.bug.BugReadPresenter
import com.mycollab.module.project.view.finance.IInvoiceListPresenter
import com.mycollab.module.project.view.finance.ITimeTrackingPresenter
import com.mycollab.module.project.view.message.MessageListPresenter
import com.mycollab.module.project.view.message.MessageReadPresenter
import com.mycollab.module.project.view.milestone.MilestoneAddPresenter
import com.mycollab.module.project.view.milestone.MilestoneListPresenter
import com.mycollab.module.project.view.milestone.MilestoneReadPresenter
import com.mycollab.module.project.view.milestone.MilestoneRoadmapPresenter
import com.mycollab.module.project.view.page.PageAddPresenter
import com.mycollab.module.project.view.page.PageListPresenter
import com.mycollab.module.project.view.page.PageReadPresenter
import com.mycollab.module.project.view.parameters.*
import com.mycollab.module.project.view.risk.IRiskAddPresenter
import com.mycollab.module.project.view.risk.IRiskReadPresenter
import com.mycollab.module.project.view.settings.*
import com.mycollab.module.project.view.task.TaskAddPresenter
import com.mycollab.module.project.view.task.TaskReadPresenter
import com.mycollab.module.project.view.ticket.ITicketKanbanPresenter
import com.mycollab.module.project.view.ticket.TicketDashboardPresenter
import com.mycollab.module.project.view.user.ProjectDashboardPresenter
import com.mycollab.vaadin.mvp.IPresenter
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ProjectPresenterDataMapper {
    private val milestoneMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            MilestoneScreenData.Read::class.java to MilestoneReadPresenter::class.java,
            MilestoneScreenData.Search::class.java to MilestoneListPresenter::class.java,
            MilestoneScreenData.Add::class.java to MilestoneAddPresenter::class.java,
            MilestoneScreenData.Edit::class.java to MilestoneAddPresenter::class.java,
            MilestoneScreenData.Roadmap::class.java to MilestoneRoadmapPresenter::class.java
    )

    private val messageMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            MessageScreenData.Read::class.java to MessageReadPresenter::class.java,
            MessageScreenData.Search::class.java to MessageListPresenter::class.java)

    private val pageMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            PageScreenData.Read::class.java to PageReadPresenter::class.java,
            PageScreenData.Add::class.java to PageAddPresenter::class.java,
            PageScreenData.Edit::class.java to PageAddPresenter::class.java,
            PageScreenData.Search::class.java to PageListPresenter::class.java)

    private val riskMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            RiskScreenData.Add::class.java to IRiskAddPresenter::class.java,
            RiskScreenData.Edit::class.java to IRiskAddPresenter::class.java,
            RiskScreenData.Read::class.java to IRiskReadPresenter::class.java)

    private val taskMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            TaskScreenData.Add::class.java to TaskAddPresenter::class.java,
            TaskScreenData.Edit::class.java to TaskAddPresenter::class.java,
            TaskScreenData.Read::class.java to TaskReadPresenter::class.java,
            TicketScreenData.GotoDashboard::class.java to TicketDashboardPresenter::class.java,
            TicketScreenData.GotoKanbanView::class.java to ITicketKanbanPresenter::class.java)

    private val trackerMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            BugScreenData.Read::class.java to BugReadPresenter::class.java,
            BugScreenData.Add::class.java to BugAddPresenter::class.java,
            BugScreenData.Edit::class.java to BugAddPresenter::class.java)

    private val standupMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            StandupScreenData.Search::class.java to ProjectDashboardPresenter::class.java)

    private val userMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            ProjectMemberScreenData.Add::class.java to ProjectMemberEditPresenter::class.java,
            ProjectMemberScreenData.InviteProjectMembers::class.java to ProjectMemberInvitePresenter::class.java,
            ProjectMemberScreenData.Read::class.java to ProjectMemberReadPresenter::class.java,
            ProjectMemberScreenData.Search::class.java to ProjectMemberListPresenter::class.java,
            ProjectRoleScreenData.Add::class.java to ProjectRoleAddPresenter::class.java,
            ProjectRoleScreenData.Read::class.java to ProjectRoleReadPresenter::class.java,
            ProjectRoleScreenData.Search::class.java to ProjectRoleListPresenter::class.java,
            ProjectSettingScreenData.ViewSettings::class.java to ProjectCustomPresenter::class.java,
            ComponentScreenData.Add::class.java to ComponentAddPresenter::class.java,
            ComponentScreenData.Edit::class.java to ComponentAddPresenter::class.java,
            ComponentScreenData.Read::class.java to ComponentReadPresenter::class.java,
            ComponentScreenData.Search::class.java to ComponentListPresenter::class.java,
            VersionScreenData.Add::class.java to VersionAddPresenter::class.java,
            VersionScreenData.Edit::class.java to VersionAddPresenter::class.java,
            VersionScreenData.Read::class.java to VersionReadPresenter::class.java,
            VersionScreenData.Search::class.java to VersionListPresenter::class.java)

    private val timeMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            TimeTrackingScreenData.Search::class.java to ITimeTrackingPresenter::class.java,
            InvoiceScreenData.GotoInvoiceList::class.java to IInvoiceListPresenter::class.java)

    private val projectMapper = mapOf<Class<out ScreenData<Any>>, Class<out IPresenter<*>>>(
            ProjectScreenData.GotoTagList::class.java to ProjectDashboardPresenter::class.java,
            ProjectScreenData.GotoFavorite::class.java to ProjectDashboardPresenter::class.java,
            ProjectScreenData.GotoReportConsole::class.java to ProjectDashboardPresenter::class.java,
            ProjectScreenData.Edit::class.java to ProjectDashboardPresenter::class.java,
            ReportScreenData.GotoWeeklyTiming::class.java to ProjectDashboardPresenter::class.java)

    private val allMapper = milestoneMapper + messageMapper + pageMapper + riskMapper + taskMapper +
            trackerMapper + standupMapper + userMapper + timeMapper + projectMapper

    @JvmStatic
    fun presenter(screenData: ScreenData<Any>): Class<out IPresenter<*>>? = allMapper[screenData.javaClass]
}