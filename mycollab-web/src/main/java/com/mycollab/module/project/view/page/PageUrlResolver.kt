package com.mycollab.module.project.view.page

import com.mycollab.common.UrlTokenizer
import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.StringUtils
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.page.domain.Page
import com.mycollab.module.page.service.PageService
import com.mycollab.module.project.event.ProjectEvent
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.project.view.parameters.PageScreenData
import com.mycollab.module.project.view.parameters.ProjectScreenData
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.UserUIContext
import com.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class PageUrlResolver : ProjectUrlResolver() {
    init {
        this.addSubResolver("list", ListUrlResolver())
        this.addSubResolver("add", AddUrlResolver())
        this.addSubResolver("edit", EditUrlResolver())
        this.addSubResolver("preview", PreviewUrlResolver())
    }

    private class ListUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            try {
                val tokenizer = UrlTokenizer(params[0])
                val projectId = tokenizer.getInt()
                val pagePath = tokenizer.remainValue
                val chain = PageActionChain(ProjectScreenData.Goto(projectId),
                        PageScreenData.Search(pagePath))
                EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
            } catch (e: Exception) {
                throw MyCollabException(e)
            }
        }
    }

    private class PreviewUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            try {
                val tokenizer = UrlTokenizer(params[0])
                val projectId = tokenizer.getInt()
                val pagePath = tokenizer.remainValue
                val pageService = AppContextUtil.getSpringBean(PageService::class.java)
                val page = pageService.getPage(pagePath, UserUIContext.getUsername())
                if (page != null) {
                    val chain = PageActionChain(ProjectScreenData.Goto(projectId), PageScreenData.Read(page))
                    EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
                } else {
                    val chain = PageActionChain(ProjectScreenData.Goto(projectId))
                    EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
                }
            } catch (e: Exception) {
                throw MyCollabException(e)
            }
        }
    }

    private class EditUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            try {
                val tokenizer = UrlTokenizer(params[0])
                val projectId = tokenizer.getInt()
                val pagePath = tokenizer.remainValue
                val pageService = AppContextUtil.getSpringBean(PageService::class.java)
                val page = pageService.getPage(pagePath, UserUIContext.getUsername())
                if (page != null) {
                    val chain = PageActionChain(ProjectScreenData.Goto(projectId), PageScreenData.Edit(page))
                    EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
                } else {
                    val chain = PageActionChain(ProjectScreenData.Goto(projectId))
                    EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
                }
            } catch (e: Exception) {
                throw MyCollabException(e)
            }
        }
    }

    private class AddUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            try {
                val tokenizer = UrlTokenizer(params[0])
                val projectId = tokenizer.getInt()
                val pagePath = tokenizer.remainValue
                val page = Page()
                page.path = pagePath + "/" + StringUtils.generateSoftUniqueId()
                val chain = PageActionChain(ProjectScreenData.Goto(projectId), PageScreenData.Add(page))
                EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
            } catch (e: Exception) {
                throw MyCollabException(e)
            }
        }
    }
}