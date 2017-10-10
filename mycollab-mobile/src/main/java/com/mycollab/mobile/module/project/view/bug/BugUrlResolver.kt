/**
 * mycollab-mobile - Parent pom providing dependency and plugin management for applications
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
package com.mycollab.mobile.module.project.view.bug

import com.mycollab.common.UrlTokenizer
import com.mycollab.core.MyCollabException
import com.mycollab.core.ResourceNotFoundException
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.project.event.ProjectEvent
import com.mycollab.mobile.module.project.view.ProjectUrlResolver
import com.mycollab.mobile.module.project.view.parameters.BugScreenData
import com.mycollab.mobile.module.project.view.parameters.ProjectScreenData
import com.mycollab.module.project.ProjectLinkParams
import com.mycollab.module.tracker.domain.SimpleBug
import com.mycollab.module.tracker.service.BugService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class BugUrlResolver : ProjectUrlResolver() {
    init {
        this.addSubResolver("add", AddUrlResolver())
        this.addSubResolver("edit", EditUrlResolver())
        this.addSubResolver("preview", PreviewUrlResolver())
    }

    private class PreviewUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            if (ProjectLinkParams.isValidParam(params[0])) {
                val prjShortName = ProjectLinkParams.getProjectShortName(params[0])
                val itemKey = ProjectLinkParams.getItemKey(params[0])
                val bugService = AppContextUtil.getSpringBean(BugService::class.java)
                val bug = bugService.findByProjectAndBugKey(itemKey, prjShortName, AppUI.accountId)
                if (bug != null) {
                    val projectId = bug.projectid
                    val bugId = bug.id
                    val chain = PageActionChain(ProjectScreenData.Goto(projectId), BugScreenData.Read(bugId))
                    EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
                } else {
                    throw ResourceNotFoundException("Can not get bug with bugkey $itemKey and project short name $prjShortName")
                }
            } else {
                throw MyCollabException("Invalid bug link $params[0]")
            }
        }
    }

    private class EditUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            if (ProjectLinkParams.isValidParam(params[0])) {
                val prjShortName = ProjectLinkParams.getProjectShortName(params[0])
                val itemKey = ProjectLinkParams.getItemKey(params[0])
                val bugService = AppContextUtil.getSpringBean(BugService::class.java)
                val bug = bugService.findByProjectAndBugKey(itemKey, prjShortName, AppUI.accountId)
                when(bug) {
                    null -> throw ResourceNotFoundException("Can not find bug with key $itemKey and project $prjShortName")
                    else -> {
                        val chain = PageActionChain(ProjectScreenData.Goto(bug.projectid), BugScreenData.Edit(bug))
                        EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
                    }
                }
            } else {
                throw MyCollabException("Invalid bug link: %s".format(params[0]))
            }
        }
    }

    private class AddUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), BugScreenData.Add(SimpleBug()))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }
}