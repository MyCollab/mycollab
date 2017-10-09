package com.mycollab.module.project.view.ticket

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.project.event.ProjectEvent
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.project.view.parameters.ProjectScreenData
import com.mycollab.module.project.view.parameters.TicketScreenData
import com.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class TicketUrlResolver : ProjectUrlResolver() {
    init {
        this.addSubResolver("dashboard", DashboardUrlResolver())
        this.defaultUrlResolver = DashboardUrlResolver()
    }

    private class DashboardUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val tokenizer = UrlTokenizer(params[0])
            val projectId = tokenizer.getInt()
            val query = tokenizer.query
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), TicketScreenData.GotoDashboard(query))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }
}