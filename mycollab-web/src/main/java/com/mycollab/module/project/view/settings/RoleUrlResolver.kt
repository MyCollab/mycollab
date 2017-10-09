package com.mycollab.module.project.view.settings

import com.mycollab.common.UrlTokenizer
import com.mycollab.core.ResourceNotFoundException
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.module.project.domain.ProjectRole
import com.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria
import com.mycollab.module.project.event.ProjectEvent
import com.mycollab.module.project.service.ProjectRoleService
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.project.view.parameters.ProjectRoleScreenData
import com.mycollab.module.project.view.parameters.ProjectScreenData
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class RoleUrlResolver : ProjectUrlResolver() {
    init {
        this.addSubResolver("list", ListUrlResolver())
        this.addSubResolver("preview", PreviewUrlResolver())
        this.addSubResolver("edit", EditUrlResolver())
        this.addSubResolver("add", AddUrlResolver())
    }

    private class ListUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val roleSearchCriteria = ProjectRoleSearchCriteria()
            roleSearchCriteria.projectId = NumberSearchField(projectId)
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), ProjectRoleScreenData.Search(roleSearchCriteria))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class PreviewUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val projectId = token.getInt()
            val roleId = token.getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), ProjectRoleScreenData.Read(roleId))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class EditUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val projectId = token.getInt()
            val roleId = token.getInt()
            val roleService = AppContextUtil.getSpringBean(ProjectRoleService::class.java)
            val role = roleService.findById(roleId, AppUI.accountId)
            if (role != null) {
                val chain = PageActionChain(ProjectScreenData.Goto(projectId), ProjectRoleScreenData.Add(role))
                EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
            } else {
                throw ResourceNotFoundException("Can not find resource $params")
            }
        }
    }

    private class AddUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val projectId = token.getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), ProjectRoleScreenData.Add(ProjectRole()))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }
}