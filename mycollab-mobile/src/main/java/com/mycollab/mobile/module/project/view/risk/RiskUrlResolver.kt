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
package com.mycollab.mobile.module.project.view.risk

import com.mycollab.common.UrlTokenizer
import com.mycollab.core.ResourceNotFoundException
import com.mycollab.mobile.module.project.event.ProjectEvent
import com.mycollab.mobile.module.project.view.ProjectUrlResolver
import com.mycollab.mobile.module.project.view.parameters.ProjectScreenData
import com.mycollab.mobile.module.project.view.parameters.RiskScreenData
import com.mycollab.module.project.domain.SimpleRisk
import com.mycollab.module.project.service.RiskService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class RiskUrlResolver : ProjectUrlResolver() {
    init {
        this.addSubResolver("add", AddUrlResolver())
        this.addSubResolver("edit", EditUrlResolver())
        this.addSubResolver("preview", PreviewUrlResolver())
    }

    private class AddUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val risk = SimpleRisk()
            risk.projectid = projectId
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), RiskScreenData.Add(risk))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class EditUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val projectId = token.getInt()
            val riskId = token.getInt()
            val riskService = AppContextUtil.getSpringBean(RiskService::class.java)
            val risk = riskService.findById(riskId, AppUI.accountId)
            if (risk != null) {
                val chain = PageActionChain(ProjectScreenData.Goto(projectId), RiskScreenData.Edit(risk))
                EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
            } else {
                throw ResourceNotFoundException("Can noy find risk $params")
            }
        }
    }

    private class PreviewUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val projectId = token.getInt()
            val riskId = token.getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), RiskScreenData.Read(riskId))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }
}