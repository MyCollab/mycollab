package com.mycollab.mobile.module.project.view.message

import com.mycollab.common.UrlTokenizer
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.project.event.ProjectEvent
import com.mycollab.mobile.module.project.view.ProjectUrlResolver
import com.mycollab.mobile.module.project.view.parameters.MessageScreenData
import com.mycollab.mobile.module.project.view.parameters.ProjectScreenData
import com.mycollab.module.project.domain.criteria.MessageSearchCriteria
import com.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class MessageUrlResolver : ProjectUrlResolver() {
    init {
        this.addSubResolver("list", ListUrlResolver())
        this.addSubResolver("preview", PreviewUrlResolver())
        this.addSubResolver("add", AddUrlResolver())
    }

    private class ListUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val searchCriteria = MessageSearchCriteria()
            searchCriteria.projectids = SetSearchField<Int>(projectId)
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), MessageScreenData.Search(searchCriteria))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class PreviewUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val projectId = token.getInt()
            val messageId = token.getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), MessageScreenData.Read(messageId))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class AddUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), MessageScreenData.Add())
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }
}