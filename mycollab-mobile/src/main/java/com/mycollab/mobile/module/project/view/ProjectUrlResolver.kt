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
package com.mycollab.mobile.module.project.view

import com.mycollab.common.ModuleNameConstants
import com.mycollab.common.UrlTokenizer
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.project.event.ProjectEvent
import com.mycollab.mobile.module.project.view.bug.BugUrlResolver
import com.mycollab.mobile.module.project.view.message.MessageUrlResolver
import com.mycollab.mobile.module.project.view.milestone.MilestoneUrlResolver
import com.mycollab.mobile.module.project.view.parameters.ProjectScreenData
import com.mycollab.mobile.module.project.view.risk.RiskUrlResolver
import com.mycollab.mobile.module.project.view.settings.UserUrlResolver
import com.mycollab.mobile.module.project.view.task.TaskUrlResolver
import com.mycollab.mobile.module.project.view.ticket.TicketUrlResolver
import com.mycollab.mobile.shell.ModuleHelper
import com.mycollab.mobile.shell.event.ShellEvent
import com.mycollab.module.project.service.ProjectService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.UserUIContext
import com.mycollab.vaadin.mvp.PageActionChain
import com.mycollab.vaadin.mvp.UrlResolver

class ProjectUrlResolver : UrlResolver() {
    fun build(): UrlResolver {
        this.addSubResolver("dashboard", DashboardUrlResolver())
        this.addSubResolver("activities", ActivityUrlResolver())
        this.addSubResolver("message", MessageUrlResolver())
        this.addSubResolver("milestone", MilestoneUrlResolver())
        this.addSubResolver("ticket", TicketUrlResolver())
        this.addSubResolver("task", TaskUrlResolver())
        this.addSubResolver("bug", BugUrlResolver())
        this.addSubResolver("risk", RiskUrlResolver())
        this.addSubResolver("user", UserUrlResolver())
        return this
    }

    override fun handle(vararg params: String) {
        if (!ModuleHelper.isCurrentProjectModule) {
            EventBusFactory.getInstance().post(ShellEvent.GotoProjectModule(this, params))
        } else {
            super.handle(*params)
        }
    }

    override fun defaultPageErrorHandler() {
        EventBusFactory.getInstance().post(ShellEvent.GotoProjectModule(this, null))
    }

    override fun handlePage(vararg params: String) {
        super.handlePage(*params)
        EventBusFactory.getInstance().post(ProjectEvent.GotoProjectList(this, null))
    }

    class DashboardUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            if (params.isEmpty()) {
                EventBusFactory.getInstance().post(ShellEvent.GotoProjectModule(this, null))
            } else {
                val projectId = UrlTokenizer(params[0]).getInt()
                val chain = PageActionChain(ProjectScreenData.Goto(projectId), ProjectScreenData.GotoDashboard())
                EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
            }
        }
    }

    class ActivityUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            if (params.isEmpty()) {
                val prjService = AppContextUtil.getSpringBean(ProjectService::class.java)
                val prjKeys = prjService.getProjectKeysUserInvolved(UserUIContext.getUsername(), AppUI.accountId)
                val searchCriteria = ActivityStreamSearchCriteria()
                searchCriteria.moduleSet = SetSearchField(ModuleNameConstants.PRJ)
                searchCriteria.saccountid = NumberSearchField(AppUI.accountId)
                searchCriteria.extraTypeIds = SetSearchField(prjKeys)

                val data = ProjectScreenData.AllActivities(searchCriteria)
                EventBusFactory.getInstance().post(ProjectEvent.GotoAllActivitiesView(this, data))
            } else {
                val projectId = UrlTokenizer(params[0]).getInt()
                val searchCriteria = ActivityStreamSearchCriteria()
                searchCriteria.moduleSet = SetSearchField(ModuleNameConstants.PRJ)
                searchCriteria.saccountid = NumberSearchField(AppUI.accountId)
                searchCriteria.extraTypeIds = SetSearchField(projectId)
                val chain = PageActionChain(ProjectScreenData.Goto(projectId), ProjectScreenData.ProjectActivities(searchCriteria))
                EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
            }
        }
    }
}