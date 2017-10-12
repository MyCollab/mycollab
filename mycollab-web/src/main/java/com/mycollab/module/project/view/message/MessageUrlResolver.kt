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
package com.mycollab.module.project.view.message

import com.mycollab.common.UrlTokenizer
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.project.domain.criteria.MessageSearchCriteria
import com.mycollab.module.project.event.ProjectEvent
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.project.view.parameters.MessageScreenData
import com.mycollab.module.project.view.parameters.ProjectScreenData
import com.mycollab.vaadin.mvp.PageActionChain

class MessageUrlResolver : ProjectUrlResolver() {
    init {
        this.addSubResolver("list", ListUrlResolver())
        this.addSubResolver("preview", PreviewUrlResolver())
    }

    private class ListUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val searchCriteria = MessageSearchCriteria()
            searchCriteria.projectids = SetSearchField(projectId)
            val chain = PageActionChain(ProjectScreenData.Goto(projectId),
                    MessageScreenData.Search(searchCriteria))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class PreviewUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val projectId = token.getInt()
            val messageId = token.getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId),
                    MessageScreenData.Read(messageId))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }
}