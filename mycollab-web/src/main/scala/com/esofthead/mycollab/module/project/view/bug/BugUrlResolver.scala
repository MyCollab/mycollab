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
package com.esofthead.mycollab.module.project.view.bug

import com.esofthead.mycollab.common.UrlTokenizer
import com.esofthead.mycollab.core.arguments.NumberSearchField
import com.esofthead.mycollab.core.{MyCollabException, ResourceNotFoundException}
import com.esofthead.mycollab.eventmanager.EventBusFactory
import com.esofthead.mycollab.module.project.ProjectLinkParams
import com.esofthead.mycollab.module.project.events.ProjectEvent
import com.esofthead.mycollab.module.project.view.ProjectUrlResolver
import com.esofthead.mycollab.module.project.view.parameters.{BugFilterParameter, VersionScreenData, BugScreenData, ProjectScreenData}
import com.esofthead.mycollab.module.tracker.domain.SimpleBug
import com.esofthead.mycollab.module.tracker.domain.criteria.{BugSearchCriteria, VersionSearchCriteria}
import com.esofthead.mycollab.module.tracker.service.BugService
import com.esofthead.mycollab.spring.ApplicationContextUtil
import com.esofthead.mycollab.vaadin.AppContext
import com.esofthead.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
class BugUrlResolver extends ProjectUrlResolver {
    this.defaultUrlResolver = new DefaultUrlResolver
    this.addSubResolver("dashboard", new DefaultUrlResolver)
    this.addSubResolver("add", new AddUrlResolver)
    this.addSubResolver("list", new ListUrlResolver)
    this.addSubResolver("edit", new EditUrlResolver)
    this.addSubResolver("preview", new PreviewUrlResolver)
    this.addSubResolver("component", new ComponentUrlResolver)
    this.addSubResolver("version", new VersionUrlResolver)

    private class DefaultUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val projectId = new UrlTokenizer(params(0)).getInt
            val chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                new BugScreenData.GotoDashboard)
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class ListUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val projectId = new UrlTokenizer(params(0)).getInt
            val bugSearchCriteria = new BugSearchCriteria
            bugSearchCriteria.setProjectId(new NumberSearchField(projectId))
            val chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                new BugScreenData.Search(new BugFilterParameter("List", bugSearchCriteria)))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class PreviewUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            var projectId: Integer = 0
            var bugId: Integer = 0
            if (ProjectLinkParams.isValidParam(params(0))) {
                val prjShortName = ProjectLinkParams.getProjectShortName(params(0))
                val itemKey = ProjectLinkParams.getItemKey(params(0))
                val bugService = ApplicationContextUtil.getSpringBean(classOf[BugService])
                val bug = bugService.findByProjectAndBugKey(itemKey, prjShortName, AppContext.getAccountId)
                if (bug != null) {
                    projectId = bug.getProjectid
                    bugId = bug.getId
                }
                else {
                    throw new ResourceNotFoundException("Can not get bug with bugkey %d and project short name %s".format(itemKey, prjShortName))
                }
            }
            else {
                throw new MyCollabException("Invalid bug link " + params(0))
            }
            val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new BugScreenData.Read(bugId))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class EditUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            var bug: SimpleBug = null
            if (ProjectLinkParams.isValidParam(params(0))) {
                val prjShortName = ProjectLinkParams.getProjectShortName(params(0))
                val itemKey = ProjectLinkParams.getItemKey(params(0))
                val bugService = ApplicationContextUtil.getSpringBean(classOf[BugService])
                bug = bugService.findByProjectAndBugKey(itemKey, prjShortName, AppContext.getAccountId)
            }
            else {
                throw new MyCollabException("Invalid bug link: " + params(0))
            }
            if (bug == null) {
                throw new ResourceNotFoundException
            }
            val chain = new PageActionChain(new ProjectScreenData.Goto(bug.getProjectid), new BugScreenData.Edit(bug))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class AddUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val projectId = new UrlTokenizer(params(0)).getInt
            val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new BugScreenData.Add(new SimpleBug))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }
}
