package com.mycollab.module.project.view.task

import com.mycollab.common.UrlTokenizer
import com.mycollab.core.MyCollabException
import com.mycollab.core.ResourceNotFoundException
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.project.ProjectLinkParams
import com.mycollab.module.project.domain.SimpleTask
import com.mycollab.module.project.event.ProjectEvent
import com.mycollab.module.project.service.ProjectTaskService
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.project.view.parameters.ProjectScreenData
import com.mycollab.module.project.view.parameters.TaskScreenData
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.mvp.PageActionChain
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class TaskUrlResolver : ProjectUrlResolver() {
    init {
        this.addSubResolver("preview", ReadUrlResolver())
        this.addSubResolver("add", AddUrlResolver())
        this.addSubResolver("edit", EditUrlResolver())
        this.addSubResolver("kanban", KanbanUrlResolver())
    }

    private class KanbanUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId),
                    TaskScreenData.GotoKanbanView())
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class ReadUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            if (ProjectLinkParams.isValidParam(params[0])) {
                val prjShortName = ProjectLinkParams.getProjectShortName(params[0])
                val itemKey = ProjectLinkParams.getItemKey(params[0])
                val taskService = AppContextUtil.getSpringBean(ProjectTaskService::class.java)
                val task = taskService.findByProjectAndTaskKey(itemKey, prjShortName, AppUI.accountId)
                if (task != null) {
                    val chain = PageActionChain(ProjectScreenData.Goto(task.projectid), TaskScreenData.Read(task.id))
                    EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
                } else {
                    throw ResourceNotFoundException(String.format("Can not find task with itemKey %d and project %s", itemKey, prjShortName))
                }
            } else {
                throw MyCollabException("Invalid url " + params[0])
            }
        }
    }

    private class EditUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val taskService = AppContextUtil.getSpringBean(ProjectTaskService::class.java)
            if (ProjectLinkParams.isValidParam(params[0])) {
                val prjShortName = ProjectLinkParams.getProjectShortName(params[0])
                val itemKey = ProjectLinkParams.getItemKey(params[0])
                val task = taskService.findByProjectAndTaskKey(itemKey, prjShortName, AppUI.accountId) ?: throw ResourceNotFoundException("Can not find task with path ${Arrays.toString(params)}")
                val chain = PageActionChain(ProjectScreenData.Goto(task.projectid),
                        TaskScreenData.Edit(task))
                EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
            } else {
                throw MyCollabException("Can not find task link " + params[0])
            }
        }
    }

    private class AddUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId),
                    TaskScreenData.Add(SimpleTask()))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }
}