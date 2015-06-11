/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.bug

import com.esofthead.mycollab.common.UrlTokenizer
import com.esofthead.mycollab.core.{MyCollabException, ResourceNotFoundException}
import com.esofthead.mycollab.core.arguments.{SetSearchField, SearchField, NumberSearchField}
import com.esofthead.mycollab.eventmanager.EventBusFactory
import com.esofthead.mycollab.mobile.module.project.ProjectUrlResolver
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent
import com.esofthead.mycollab.mobile.module.project.view.parameters.{BugScreenData, ProjectScreenData, BugFilterParameter}
import com.esofthead.mycollab.module.project.ProjectLinkParams
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus
import com.esofthead.mycollab.module.tracker.domain.SimpleBug
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria
import com.esofthead.mycollab.module.tracker.service.BugService
import com.esofthead.mycollab.spring.ApplicationContextUtil
import com.esofthead.mycollab.vaadin.AppContext
import com.esofthead.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
class BugUrlResolver extends ProjectUrlResolver {
    this.addSubResolver("list", new DashboardUrlResolver)
    this.addSubResolver("add", new AddUrlResolver)
    this.addSubResolver("edit", new EditUrlResolver)
    this.addSubResolver("preview", new PreviewUrlResolver)

    private class DashboardUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val projectId: Int = new UrlTokenizer(params(0)).getInt
            val criteria: BugSearchCriteria = new BugSearchCriteria
            criteria.setProjectId(new NumberSearchField(SearchField.AND, projectId))
            criteria.setStatuses(new SetSearchField[String](BugStatus.InProgress.name, BugStatus.Open.name, BugStatus.ReOpened.name))
            val parameter: BugFilterParameter = new BugFilterParameter("Open Bugs", criteria)
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                new BugScreenData.Search(parameter))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class PreviewUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            var projectId: Int = 0
            var bugId: Int = 0
            if (ProjectLinkParams.isValidParam(params(0))) {
                val prjShortName: String = ProjectLinkParams.getProjectShortName(params(0))
                val itemKey: Int = ProjectLinkParams.getItemKey(params(0))
                val bugService: BugService = ApplicationContextUtil.getSpringBean(classOf[BugService])
                val bug: SimpleBug = bugService.findByProjectAndBugKey(itemKey, prjShortName, AppContext.getAccountId)
                if (bug != null) {
                    projectId = bug.getProjectid
                    bugId = bug.getId
                }
                else {
                    throw new ResourceNotFoundException("Can not get bug with bugkey %d and project short name %s".format(itemKey, prjShortName))
                }
            }
            else {
                throw new MyCollabException("Invalid bug link %s".format(params(0)))
            }
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId), new BugScreenData.Read(bugId))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class EditUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            var bug: SimpleBug = null
            if (ProjectLinkParams.isValidParam(params(0))) {
                val prjShortName: String = ProjectLinkParams.getProjectShortName(params(0))
                val itemKey: Int = ProjectLinkParams.getItemKey(params(0))
                val bugService: BugService = ApplicationContextUtil.getSpringBean(classOf[BugService])
                bug = bugService.findByProjectAndBugKey(itemKey, prjShortName, AppContext.getAccountId)
            }
            else {
                throw new MyCollabException("Invalid bug link: %s".format(params(0)))
            }
            if (bug == null) {
                throw new ResourceNotFoundException
            }
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(bug.getProjectid),
                new BugScreenData.Edit(bug))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class AddUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val projectId: Int = new UrlTokenizer(params(0)).getInt
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                new BugScreenData.Add(new SimpleBug))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

}
