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
package com.mycollab.mobile.module.project.view.settings

import com.mycollab.common.UrlTokenizer
import com.mycollab.core.ResourceNotFoundException
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.project.event.ProjectEvent
import com.mycollab.mobile.module.project.view.ProjectUrlResolver
import com.mycollab.mobile.module.project.view.parameters.ProjectMemberScreenData
import com.mycollab.mobile.module.project.view.parameters.ProjectScreenData
import com.mycollab.module.project.ProjectMemberStatusConstants
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria
import com.mycollab.module.project.service.ProjectMemberService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class UserUrlResolver : ProjectUrlResolver() {
    init {
        this.addSubResolver("list", ListUrlResolver())
        this.addSubResolver("preview", PreviewUrlResolver())
        this.addSubResolver("edit", EditUrlResolver())
        this.addSubResolver("invite", InviteUrlResolver())
    }

    private class ListUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val memberSearchCriteria = ProjectMemberSearchCriteria()
            memberSearchCriteria.projectId = NumberSearchField(projectId)
            memberSearchCriteria.statuses = SetSearchField<String>(ProjectMemberStatusConstants.ACTIVE, ProjectMemberStatusConstants.NOT_ACCESS_YET)
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), ProjectMemberScreenData.Search(memberSearchCriteria))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class PreviewUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val projectId = token.getInt()
            val memberName = token.getString()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), ProjectMemberScreenData.Read(memberName))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class EditUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val projectId = token.getInt()
            val memberId = token.getInt()
            val memberService = AppContextUtil.getSpringBean(ProjectMemberService::class.java)
            val member = memberService.findById(memberId, AppUI.accountId)
            if (member != null) {
                val chain = PageActionChain(ProjectScreenData.Goto(projectId), ProjectMemberScreenData.Edit(member))
                EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
            } else {
                throw ResourceNotFoundException("Can not find member ${memberId}")
            }
        }
    }

    private class InviteUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val projectId = token.getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), ProjectMemberScreenData.InviteProjectMembers())
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }
}