package com.mycollab.module.project.view.settings

import com.mycollab.common.UrlTokenizer
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.project.event.ProjectEvent
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.project.view.parameters.ProjectScreenData
import com.mycollab.module.project.view.parameters.VersionScreenData
import com.mycollab.module.tracker.domain.Version
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria
import com.mycollab.module.tracker.service.VersionService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class VersionUrlResolver : ProjectUrlResolver() {
    init {
        this.addSubResolver("list", ListUrlResolver())
        this.addSubResolver("add", AddUrlResolver())
        this.addSubResolver("edit", EditUrlResolver())
        this.addSubResolver("preview", PreviewUrlResolver())
    }

    private class ListUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val versionSearchCriteria = VersionSearchCriteria()
            versionSearchCriteria.projectId = NumberSearchField(projectId)
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), VersionScreenData.Search(versionSearchCriteria))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class PreviewUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val projectId = token.getInt()
            val versionId = token.getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), VersionScreenData.Read(versionId))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class EditUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val projectId = token.getInt()
            val versionId = token.getInt()
            val versionService = AppContextUtil.getSpringBean(VersionService::class.java)
            val version = versionService.findById(versionId, AppUI.accountId)
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), VersionScreenData.Edit(version))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class AddUrlResolver : ProjectUrlResolver() {
        protected override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), VersionScreenData.Add(Version()))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }
}