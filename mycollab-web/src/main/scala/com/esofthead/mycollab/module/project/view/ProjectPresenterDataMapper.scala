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

import com.esofthead.mycollab.module.project.view.bug.TrackerPresenter
import com.esofthead.mycollab.module.project.view.file.IFilePresenter
import com.esofthead.mycollab.module.project.view.message.MessagePresenter
import com.esofthead.mycollab.module.project.view.milestone.MilestonePresenter
import com.esofthead.mycollab.module.project.view.page.PagePresenter
import com.esofthead.mycollab.module.project.view.parameters._
import com.esofthead.mycollab.module.project.view.problem.IProblemPresenter
import com.esofthead.mycollab.module.project.view.risk.IRiskPresenter
import com.esofthead.mycollab.module.project.view.settings.UserSettingPresenter
import com.esofthead.mycollab.module.project.view.standup.IStandupPresenter
import com.esofthead.mycollab.module.project.view.task.TaskPresenter
import com.esofthead.mycollab.module.project.view.time.ITimeTrackingPresenter
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
    classOf[MilestoneScreenData.Edit] -> classOf[MilestonePresenter])

  val messageMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[MessageScreenData.Read] -> classOf[MessagePresenter],
    classOf[MessageScreenData.Search] -> classOf[MessagePresenter])

  val pageMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[PageScreenData.Read] -> classOf[PagePresenter],
    classOf[PageScreenData.Add] -> classOf[PagePresenter],
    classOf[PageScreenData.Edit] -> classOf[PagePresenter],
    classOf[PageScreenData.Search] -> classOf[PagePresenter])

  val problemMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[ProblemScreenData.Add] -> classOf[IProblemPresenter],
    classOf[ProblemScreenData.Edit] -> classOf[IProblemPresenter],
    classOf[ProblemScreenData.Read] -> classOf[IProblemPresenter],
    classOf[ProblemScreenData.Search] -> classOf[IProblemPresenter])

  val riskMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[RiskScreenData.Add] -> classOf[IRiskPresenter],
    classOf[RiskScreenData.Edit] -> classOf[IRiskPresenter],
    classOf[RiskScreenData.Read] -> classOf[IRiskPresenter],
    classOf[RiskScreenData.Search] -> classOf[IRiskPresenter])

  val taskMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[TaskScreenData.Add] -> classOf[TaskPresenter],
    classOf[TaskScreenData.Edit] -> classOf[TaskPresenter],
    classOf[TaskScreenData.Read] -> classOf[TaskPresenter],
    classOf[TaskGroupScreenData.GotoDashboard] -> classOf[TaskPresenter],
    classOf[TaskGroupScreenData.Read] -> classOf[TaskPresenter],
    classOf[TaskGroupScreenData.Edit] -> classOf[TaskPresenter]
  )

  val trackerMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[BugScreenData.Read] -> classOf[TrackerPresenter],
    classOf[BugScreenData.GotoDashboard] -> classOf[TrackerPresenter],
    classOf[BugScreenData.Add] -> classOf[TrackerPresenter],
    classOf[BugScreenData.Edit] -> classOf[TrackerPresenter],
    classOf[BugScreenData.Search] -> classOf[TrackerPresenter],
    classOf[ComponentScreenData.Add] -> classOf[TrackerPresenter],
    classOf[ComponentScreenData.Edit] -> classOf[TrackerPresenter],
    classOf[ComponentScreenData.Read] -> classOf[TrackerPresenter],
    classOf[ComponentScreenData.Search] -> classOf[TrackerPresenter],
    classOf[VersionScreenData.Add] -> classOf[TrackerPresenter],
    classOf[VersionScreenData.Edit] -> classOf[TrackerPresenter],
    classOf[VersionScreenData.Read] -> classOf[TrackerPresenter],
    classOf[VersionScreenData.Search] -> classOf[TrackerPresenter])

  val standupMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[StandupScreenData.Search] -> classOf[IStandupPresenter])

  val userMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[ProjectMemberScreenData.Add] -> classOf[UserSettingPresenter],
    classOf[ProjectMemberScreenData.InviteProjectMembers] -> classOf[UserSettingPresenter],
    classOf[ProjectMemberScreenData.Read] -> classOf[UserSettingPresenter],
    classOf[ProjectMemberScreenData.Search] -> classOf[UserSettingPresenter],
    classOf[ProjectRoleScreenData.Add] -> classOf[UserSettingPresenter],
    classOf[ProjectRoleScreenData.Read] -> classOf[UserSettingPresenter],
    classOf[ProjectRoleScreenData.Search] -> classOf[UserSettingPresenter],
    classOf[ProjectSettingScreenData.ViewSettings] -> classOf[UserSettingPresenter])

  val timeMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[TimeTrackingScreenData.Search] -> classOf[ITimeTrackingPresenter])

  val fileMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[FileScreenData.GotoDashboard] -> classOf[IFilePresenter])

  val projectMapper = Map[Class[_ <: ScreenData[_]], Class[_ <: IPresenter[_]]](
    classOf[ProjectScreenData.GotoTagList] -> classOf[ProjectDashboardPresenter],
    classOf[ProjectScreenData.SearchItem] -> classOf[ProjectDashboardPresenter])

  val allMapper = milestoneMapper ++ messageMapper ++ pageMapper ++ problemMapper ++ riskMapper ++ taskMapper ++
    trackerMapper ++ standupMapper ++ userMapper ++ timeMapper ++ fileMapper ++ projectMapper

  def presenter(screenData: ScreenData[_]): Class[_ <: IPresenter[_]] = {
    val _value = allMapper get screenData.getClass
    return if (_value.isInstanceOf[Some[_]]) _value.get else null
  }
}
