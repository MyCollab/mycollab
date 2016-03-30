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
package com.esofthead.mycollab.module.project.view

import com.esofthead.mycollab.module.project.view.bug.BugPresenter
import com.esofthead.mycollab.module.project.view.file.FilePresenter
import com.esofthead.mycollab.module.project.view.message.MessagePresenter
import com.esofthead.mycollab.module.project.view.milestone.MilestonePresenter
import com.esofthead.mycollab.module.project.view.page.PagePresenter
import com.esofthead.mycollab.module.project.view.parameters._
import com.esofthead.mycollab.module.project.view.risk.IRiskPresenter
import com.esofthead.mycollab.module.project.view.settings.UserSettingPresenter
import com.esofthead.mycollab.module.project.view.task.TaskPresenter
import com.esofthead.mycollab.module.project.view.time.IFinancePresenter
import com.esofthead.mycollab.module.project.view.user.ProjectDashboardPresenter
import com.esofthead.mycollab.vaadin.mvp.{IPresenter, ScreenData}

/**
  * @author MyCollab Ltd.
  * @since 5.0.3
  */
object ProjectPresenterDataMapper {
  val milestoneMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[MilestoneScreenData.Read] -> classOf[MilestonePresenter],
    classOf[MilestoneScreenData.Search] -> classOf[MilestonePresenter],
    classOf[MilestoneScreenData.Add] -> classOf[MilestonePresenter],
    classOf[MilestoneScreenData.Edit] -> classOf[MilestonePresenter],
    classOf[MilestoneScreenData.Roadmap] -> classOf[MilestonePresenter])

  val messageMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[MessageScreenData.Read] -> classOf[MessagePresenter],
    classOf[MessageScreenData.Search] -> classOf[MessagePresenter])

  val pageMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[PageScreenData.Read] -> classOf[PagePresenter],
    classOf[PageScreenData.Add] -> classOf[PagePresenter],
    classOf[PageScreenData.Edit] -> classOf[PagePresenter],
    classOf[PageScreenData.Search] -> classOf[PagePresenter])

  val riskMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[RiskScreenData.Add] -> classOf[IRiskPresenter],
    classOf[RiskScreenData.Edit] -> classOf[IRiskPresenter],
    classOf[RiskScreenData.Read] -> classOf[IRiskPresenter],
    classOf[RiskScreenData.Search] -> classOf[IRiskPresenter])

  val taskMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[TaskScreenData.Add] -> classOf[TaskPresenter],
    classOf[TaskScreenData.Edit] -> classOf[TaskPresenter],
    classOf[TaskScreenData.Read] -> classOf[TaskPresenter],
    classOf[TaskScreenData.GotoDashboard] -> classOf[TaskPresenter],
    classOf[TaskScreenData.GotoKanbanView] -> classOf[TaskPresenter])

  val trackerMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[BugScreenData.Read] -> classOf[BugPresenter],
    classOf[BugScreenData.Add] -> classOf[BugPresenter],
    classOf[BugScreenData.Edit] -> classOf[BugPresenter],
    classOf[BugScreenData.Search] -> classOf[BugPresenter],
    classOf[BugScreenData.GotoKanbanView] -> classOf[BugPresenter])

  val standupMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[StandupScreenData.Search] -> classOf[ProjectDashboardPresenter],
    classOf[StandupScreenData.Add] -> classOf[ProjectDashboardPresenter])

  val userMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[ProjectMemberScreenData.Add] -> classOf[UserSettingPresenter],
    classOf[ProjectMemberScreenData.InviteProjectMembers] -> classOf[UserSettingPresenter],
    classOf[ProjectMemberScreenData.Read] -> classOf[UserSettingPresenter],
    classOf[ProjectMemberScreenData.Search] -> classOf[UserSettingPresenter],
    classOf[ProjectRoleScreenData.Add] -> classOf[UserSettingPresenter],
    classOf[ProjectRoleScreenData.Read] -> classOf[UserSettingPresenter],
    classOf[ProjectRoleScreenData.Search] -> classOf[UserSettingPresenter],
    classOf[ProjectSettingScreenData.ViewSettings] -> classOf[UserSettingPresenter],
    classOf[ComponentScreenData.Add] -> classOf[UserSettingPresenter],
    classOf[ComponentScreenData.Edit] -> classOf[UserSettingPresenter],
    classOf[ComponentScreenData.Read] -> classOf[UserSettingPresenter],
    classOf[ComponentScreenData.Search] -> classOf[UserSettingPresenter],
    classOf[VersionScreenData.Add] -> classOf[UserSettingPresenter],
    classOf[VersionScreenData.Edit] -> classOf[UserSettingPresenter],
    classOf[VersionScreenData.Read] -> classOf[UserSettingPresenter],
    classOf[VersionScreenData.Search] -> classOf[UserSettingPresenter])

  val timeMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[TimeTrackingScreenData.Search] -> classOf[IFinancePresenter],
    classOf[InvoiceScreenData.GotoInvoiceList] -> classOf[IFinancePresenter])

  val fileMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[FileScreenData.GotoDashboard] -> classOf[FilePresenter])

  val projectMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[ProjectScreenData.GotoTagList] -> classOf[ProjectDashboardPresenter],
    classOf[ProjectScreenData.GotoFavorite] -> classOf[ProjectDashboardPresenter],
    classOf[ProjectScreenData.GotoGanttChart] -> classOf[ProjectDashboardPresenter],
    classOf[ProjectScreenData.GotoCalendarView] -> classOf[ProjectDashboardPresenter],
    classOf[ProjectScreenData.GotoReportConsole] -> classOf[ProjectDashboardPresenter],
    classOf[ProjectScreenData.SearchItem] -> classOf[UserProjectDashboardPresenter],
    classOf[ProjectScreenData.Edit] -> classOf[UserProjectDashboardPresenter])

  val allMapper = milestoneMapper ++ messageMapper ++ pageMapper  ++ riskMapper ++ taskMapper ++
    trackerMapper ++ standupMapper ++ userMapper ++ timeMapper ++ fileMapper ++ projectMapper

  def presenter(screenData: ScreenData[_]): Class[_ <: IPresenter[_]] = {
    val _value = allMapper get screenData.getClass
    return if (_value.isInstanceOf[Some[_]]) _value.get else null
  }
}
