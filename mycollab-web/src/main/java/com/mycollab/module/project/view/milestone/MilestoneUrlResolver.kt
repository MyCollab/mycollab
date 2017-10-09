package com.mycollab.module.project.view.milestone

import com.mycollab.common.UrlTokenizer
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.project.domain.SimpleMilestone
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria
import com.mycollab.module.project.event.ProjectEvent
import com.mycollab.module.project.service.MilestoneService
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.project.view.parameters.MilestoneScreenData
import com.mycollab.module.project.view.parameters.ProjectScreenData
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class MilestoneUrlResolver : ProjectUrlResolver() {
    init {
        this.addSubResolver("list", ListUrlResolver())
        this.addSubResolver("add", AddUrlResolver())
        this.addSubResolver("edit", EditUrlResolver())
        this.addSubResolver("preview", PreviewUrlResolver())
        this.addSubResolver("kanban", KanbanUrlResolver())
    }

    private class ListUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val milestoneSearchCriteria = MilestoneSearchCriteria()
            milestoneSearchCriteria.projectIds = SetSearchField(projectId)
            val chain = PageActionChain(ProjectScreenData.Goto(projectId),
                    MilestoneScreenData.Search(milestoneSearchCriteria))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class KanbanUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), MilestoneScreenData.Kanban())
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class AddUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId),
                    MilestoneScreenData.Add(SimpleMilestone()))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class EditUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val projectId = token.getInt()
            val milestoneId = token.getInt()
            val milestoneService = AppContextUtil.getSpringBean(MilestoneService::class.java)
            val milestone = milestoneService.findById(milestoneId, AppUI.accountId)
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), MilestoneScreenData.Edit(milestone))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class PreviewUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val projectId = token.getInt()
            val milestoneId = token.getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), MilestoneScreenData.Read(milestoneId))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }
}