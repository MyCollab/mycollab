package com.mycollab.mobile.module.project.view.ticket

import com.mycollab.common.UrlTokenizer
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.project.event.ProjectEvent
import com.mycollab.mobile.module.project.view.ProjectUrlResolver
import com.mycollab.mobile.module.project.view.parameters.ProjectScreenData
import com.mycollab.mobile.module.project.view.parameters.TicketScreenData
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria
import com.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class TicketUrlResolver : ProjectUrlResolver() {
    init {
        this.addSubResolver("dashboard", ListUrlResolver())
    }

    private class ListUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val searchCriteria = ProjectTicketSearchCriteria()
            searchCriteria.projectIds = SetSearchField<Int>(projectId)
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), TicketScreenData.GotoDashboard(searchCriteria))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }
}