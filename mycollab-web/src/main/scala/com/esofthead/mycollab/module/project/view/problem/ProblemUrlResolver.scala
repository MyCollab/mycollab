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
package com.esofthead.mycollab.module.project.view.problem

import com.esofthead.mycollab.common.UrlTokenizer
import com.esofthead.mycollab.core.arguments.NumberSearchField
import com.esofthead.mycollab.eventmanager.EventBusFactory
import com.esofthead.mycollab.module.project.domain.{SimpleProblem, Problem}
import com.esofthead.mycollab.module.project.domain.criteria.ProblemSearchCriteria
import com.esofthead.mycollab.module.project.events.ProjectEvent
import com.esofthead.mycollab.module.project.service.ProblemService
import com.esofthead.mycollab.module.project.view.ProjectUrlResolver
import com.esofthead.mycollab.module.project.view.parameters.{ProblemScreenData, ProjectScreenData}
import com.esofthead.mycollab.spring.ApplicationContextUtil
import com.esofthead.mycollab.vaadin.AppContext
import com.esofthead.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
class ProblemUrlResolver extends ProjectUrlResolver {
    this.addSubResolver("list", new ListUrlResolver)
    this.addSubResolver("preview", new PreviewUrlResolver)
    this.addSubResolver("add", new AddUrlResolver)
    this.addSubResolver("edit", new EditUrlResolver)

    private class ListUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val projectId: Int = new UrlTokenizer(params(0)).getInt
            val problemSearchCriteria: ProblemSearchCriteria = new ProblemSearchCriteria
            problemSearchCriteria.setProjectId(new NumberSearchField(projectId))
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                new ProblemScreenData.Search(problemSearchCriteria))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class PreviewUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val token: UrlTokenizer = new UrlTokenizer(params(0))
            val projectId: Int = token.getInt
            val problemId: Int = token.getInt
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                new ProblemScreenData.Read(problemId))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class AddUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val projectId: Int = new UrlTokenizer(params(0)).getInt
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                new ProblemScreenData.Add(new Problem))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class EditUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val token: UrlTokenizer = new UrlTokenizer(params(0))
            val projectId: Int = token.getInt
            val problemId: Int = token.getInt
            val problemService: ProblemService = ApplicationContextUtil.getSpringBean(classOf[ProblemService])
            val problem: SimpleProblem = problemService.findById(problemId, AppContext.getAccountId)
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                new ProblemScreenData.Edit(problem))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

}
