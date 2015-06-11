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
package com.esofthead.mycollab.mobile.module.project.view.message

import com.esofthead.mycollab.common.UrlTokenizer
import com.esofthead.mycollab.core.arguments.SetSearchField
import com.esofthead.mycollab.eventmanager.EventBusFactory
import com.esofthead.mycollab.mobile.module.project.ProjectUrlResolver
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent
import com.esofthead.mycollab.mobile.module.project.view.parameters.{MessageScreenData, ProjectScreenData}
import com.esofthead.mycollab.module.project.domain.criteria.MessageSearchCriteria
import com.esofthead.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
class MessageUrlResolver extends ProjectUrlResolver {
    this.addSubResolver("list", new ListUrlResolver)
    this.addSubResolver("preview", new PreviewUrlResolver)
    this.addSubResolver("add", new AddUrlResolver)

    private class ListUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val projectId: Int = new UrlTokenizer(params(0)).getInt
            val messageSearchCriteria: MessageSearchCriteria = new MessageSearchCriteria
            messageSearchCriteria.setProjectids(new SetSearchField[Integer](projectId))
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId), new MessageScreenData.Search(messageSearchCriteria))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class PreviewUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val token: UrlTokenizer = new UrlTokenizer(params(0))
            val projectId: Int = token.getInt
            val messageId: Int = token.getInt
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId), new MessageScreenData.Read(messageId))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class AddUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val projectId: Int = new UrlTokenizer(params(0)).getInt
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId), new MessageScreenData.Add)
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

}
