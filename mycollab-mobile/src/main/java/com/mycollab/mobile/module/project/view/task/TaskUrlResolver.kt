package com.mycollab.mobile.module.project.view.task

import com.mycollab.common.UrlTokenizer
import com.mycollab.core.MyCollabException
import com.mycollab.core.ResourceNotFoundException
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.project.event.ProjectEvent
import com.mycollab.mobile.module.project.view.ProjectUrlResolver
import com.mycollab.mobile.module.project.view.parameters.ProjectScreenData
import com.mycollab.mobile.module.project.view.parameters.TaskScreenData
import com.mycollab.module.project.ProjectLinkParams
import com.mycollab.module.project.domain.SimpleTask
import com.mycollab.module.project.service.ProjectTaskService
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
        this.addSubResolver("edit", EditUrlResolver())
        this.addSubResolver("add", AddUrlResolver())
    }

    private class ReadUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            if (ProjectLinkParams.isValidParam(params[0])) {
                val prjShortName = ProjectLinkParams.getProjectShortName(params[0])
                val itemKey = ProjectLinkParams.getItemKey(params[0])
                val taskService = AppContextUtil.getSpringBean(ProjectTaskService::class.java)
                val task = taskService.findByProjectAndTaskKey(itemKey, prjShortName, AppUI.accountId)
                when {
                    task != null -> {
                        val chain = PageActionChain(ProjectScreenData.Goto(task.projectid), TaskScreenData.Read(task.id))
                        EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
                    }
                    else -> throw ResourceNotFoundException("Can not find task with itemKey $itemKey and project $prjShortName")
                }
            } else throw ResourceNotFoundException("Invalid param ${Arrays.toString(params)}")
        }
    }

    private class EditUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val taskService = AppContextUtil.getSpringBean(ProjectTaskService::class.java)
            if (ProjectLinkParams.isValidParam(params[0])) {
                val prjShortName = ProjectLinkParams.getProjectShortName(params[0])
                val itemKey = ProjectLinkParams.getItemKey(params[0])
                val task = taskService.findByProjectAndTaskKey(itemKey, prjShortName, AppUI.accountId)
                val chain = PageActionChain(ProjectScreenData.Goto(task.projectid), TaskScreenData.Edit(task))
                EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
            } else throw MyCollabException("Can not find task link ${Arrays.toString(params)}")
        }
    }

    private class AddUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val token = UrlTokenizer(params[0])
            val projectId = token.getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), TaskScreenData.Add(SimpleTask()))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }
}