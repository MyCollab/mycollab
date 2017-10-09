package com.mycollab.module.project.view.risk

import com.mycollab.common.UrlTokenizer
import com.mycollab.core.ResourceNotFoundException
import com.mycollab.module.project.domain.SimpleRisk
import com.mycollab.module.project.event.ProjectEvent
import com.mycollab.module.project.service.RiskService
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.project.view.parameters.ProjectScreenData
import com.mycollab.module.project.view.parameters.RiskScreenData
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
        this.addSubResolver("preview", PreviewUrlResolver())
        this.addSubResolver("add", AddUrlResolver())
        this.addSubResolver("edit", EditUrlResolver())
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

    private class AddUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), RiskScreenData.Add(SimpleRisk()))
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
                throw ResourceNotFoundException("Can not find risk $params")
            }
        }
    }

}