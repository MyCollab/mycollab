/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.project.event.ProjectEvent
import com.mycollab.module.project.service.ProjectService
import com.mycollab.module.project.view.bug.BugUrlResolver
import com.mycollab.module.project.view.client.ClientUrlResolver
import com.mycollab.module.project.view.file.ProjectFileUrlResolver
import com.mycollab.module.project.view.message.MessageUrlResolver
import com.mycollab.module.project.view.milestone.MilestoneUrlResolver
import com.mycollab.module.project.view.page.PageUrlResolver
import com.mycollab.module.project.view.parameters.MilestoneScreenData
import com.mycollab.module.project.view.parameters.ProjectScreenData
import com.mycollab.module.project.view.risk.RiskUrlResolver
import com.mycollab.module.project.view.settings.*
import com.mycollab.module.project.view.task.TaskUrlResolver
import com.mycollab.module.project.view.ticket.TicketUrlResolver
import com.mycollab.module.project.view.time.InvoiceUrlResolver
import com.mycollab.shell.event.ShellEvent
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.mvp.PageActionChain
import com.mycollab.vaadin.mvp.UrlResolver
import com.mycollab.vaadin.web.ui.ModuleHelper

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
open class ProjectUrlResolver : UrlResolver() {
    fun build(): UrlResolver {
        this.addSubResolver("list", ProjectListUrlResolver())
        this.addSubResolver("dashboard", ProjectDashboardUrlResolver())
        this.addSubResolver("edit", ProjectEditUrlResolver())
        this.addSubResolver("tag", ProjectTagUrlResolver())
        this.addSubResolver("favorite", ProjectFavoriteUrlResolver())
        this.addSubResolver("gantt", GanttUrlResolver())
        this.addSubResolver("reports", ReportUrlResolver())
        this.addSubResolver("message", MessageUrlResolver())
        this.addSubResolver("milestone", MilestoneUrlResolver())
        this.addSubResolver("ticket", TicketUrlResolver())
        this.addSubResolver("task", TaskUrlResolver())
        this.addSubResolver("bug", BugUrlResolver())
        this.addSubResolver("page", PageUrlResolver())
        this.addSubResolver("risk", RiskUrlResolver())
        this.addSubResolver("user", UserUrlResolver())
        this.addSubResolver("role", RoleUrlResolver())
        this.addSubResolver("setting", SettingUrlResolver())
        this.addSubResolver("time", TimeUrlResolver())
        this.addSubResolver("invoice", InvoiceUrlResolver())
        this.addSubResolver("file", ProjectFileUrlResolver())
        this.addSubResolver("component", ComponentUrlResolver())
        this.addSubResolver("version", VersionUrlResolver())
        this.addSubResolver("roadmap", RoadmapUrlResolver())
        this.addSubResolver("calendar", CalendarUrlResolver())
        this.addSubResolver("client", ClientUrlResolver())
        return this
    }

    override fun handle(vararg params: String) {
        if (!ModuleHelper.isCurrentProjectModule()) {
            EventBusFactory.getInstance().post(ShellEvent.GotoProjectModule(this, params))
        } else {
            super.handle(*params)
        }
    }

    override fun defaultPageErrorHandler() {
        EventBusFactory.getInstance().post(ShellEvent.GotoProjectModule(this, null))
    }

    class ProjectTagUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId),
                    ProjectScreenData.GotoTagList(null))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    class ProjectFavoriteUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId),
                    ProjectScreenData.GotoFavorite())
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    class ProjectListUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(ProjectEvent.GotoList(this, null))
    }

    class ProjectDashboardUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            if (params.isEmpty()) {
                EventBusFactory.getInstance().post(ShellEvent.GotoProjectModule(this, null))
            } else {
                val projectId = UrlTokenizer(params[0]).getInt()
                val chain = PageActionChain(ProjectScreenData.Goto(projectId))
                EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
            }
        }
    }

    class ProjectEditUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            if (params.isEmpty()) {
                EventBusFactory.getInstance().post(ShellEvent.GotoProjectModule(this, null))
            } else {
                val projectId = UrlTokenizer(params[0]).getInt()
                val prjService = AppContextUtil.getSpringBean(ProjectService::class.java)
                val project = prjService.findById(projectId, AppUI.accountId)
                if (project != null) {
                    val chain = PageActionChain(ProjectScreenData.Goto(projectId), ProjectScreenData.Edit(project))
                    EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
                }
            }
        }
    }

    class RoadmapUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            if (params.isEmpty()) {
                EventBusFactory.getInstance().post(ShellEvent.GotoProjectModule(this, null))
            } else {
                val projectId = UrlTokenizer(params[0]).getInt()
                val chain = PageActionChain(ProjectScreenData.Goto(projectId), MilestoneScreenData.Roadmap())
                EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
            }
        }
    }

    private class CalendarUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            if (params.isNotEmpty()) {
                val projectId = UrlTokenizer(params[0]).getInt()
                val chain = PageActionChain(ProjectScreenData.Goto(projectId), ProjectScreenData.GotoCalendarView())
                EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
            }
        }
    }

    private class GanttUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), ProjectScreenData.GotoGanttChart())
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }
}