package com.mycollab.module.project.view.settings

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.project.event.ProjectEvent
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.project.view.parameters.ProjectScreenData
import com.mycollab.module.project.view.parameters.ProjectSettingScreenData
import com.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class SettingUrlResolver : ProjectUrlResolver() {
    override fun handlePage(vararg params: String) {
        val projectId = UrlTokenizer(params[0]).getInt()
        val chain = PageActionChain(ProjectScreenData.Goto(projectId), ProjectSettingScreenData.ViewSettings())
        EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
    }
}