package com.mycollab.module.project.view.settings

import com.mycollab.common.UrlTokenizer
import com.mycollab.core.ResourceNotFoundException
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.project.event.ProjectEvent
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.project.view.parameters.ComponentScreenData
import com.mycollab.module.project.view.parameters.ProjectScreenData
import com.mycollab.module.tracker.domain.Component
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria
import com.mycollab.module.tracker.service.ComponentService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ComponentUrlResolver : ProjectUrlResolver() {
    init {
        this.addSubResolver("list", ListUrlResolver())
        this.addSubResolver("add", AddUrlResolver())
        this.addSubResolver("edit", EditUrlResolver())
        this.addSubResolver("preview", PreviewUrlResolver())
    }

    private class ListUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val searchCriteria = ComponentSearchCriteria()
            searchCriteria.projectId = NumberSearchField(projectId)
            val chain = PageActionChain(ProjectScreenData.Goto(projectId),
                    ComponentScreenData.Search(searchCriteria))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class AddUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId),
                    ComponentScreenData.Add(Component()))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class PreviewUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val projectId = token.getInt()
            val componentId = token.getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId),
                    ComponentScreenData.Read(componentId))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class EditUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val projectId = token.getInt()
            val componentId = token.getInt()
            val componentService = AppContextUtil.getSpringBean(ComponentService::class.java)
            val component = componentService.findById(componentId, AppUI.accountId)
            if (component != null) {
                val chain = PageActionChain(ProjectScreenData.Goto(projectId), ComponentScreenData.Edit(component))
                EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
            } else {
                throw ResourceNotFoundException("Can not find component $componentId")
            }
        }
    }
}