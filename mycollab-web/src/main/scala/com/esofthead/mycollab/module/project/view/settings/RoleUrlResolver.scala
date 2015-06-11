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
package com.esofthead.mycollab.module.project.view.settings

import com.esofthead.mycollab.common.UrlTokenizer
import com.esofthead.mycollab.core.arguments.NumberSearchField
import com.esofthead.mycollab.eventmanager.EventBusFactory
import com.esofthead.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria
import com.esofthead.mycollab.module.project.events.ProjectEvent
import com.esofthead.mycollab.module.project.view.ProjectUrlResolver
import com.esofthead.mycollab.module.project.view.parameters.{ProjectRoleScreenData, ProjectScreenData}
import com.esofthead.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
class RoleUrlResolver extends ProjectUrlResolver {
    this.addSubResolver("list", new ListUrlResolver())
    this.addSubResolver("preview", new PreviewUrlResolver())

    private class ListUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val projectId: Int = new UrlTokenizer(params(0)).getInt
            val roleSearchCriteria: ProjectRoleSearchCriteria = new ProjectRoleSearchCriteria
            roleSearchCriteria.setProjectId(new NumberSearchField(projectId))
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                new ProjectRoleScreenData.Search(roleSearchCriteria))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class PreviewUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val token: UrlTokenizer = new UrlTokenizer(params(0))
            val projectId: Int = token.getInt
            val roleId: Int = token.getInt
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                new ProjectRoleScreenData.Read(roleId))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

}
