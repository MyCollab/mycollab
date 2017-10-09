package com.mycollab.module.project.view

import com.mycollab.vaadin.mvp.PageView

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface UserDashboardView : PageView {

    val involvedProjectKeys: List<Int>

    fun showDashboard()

    fun showProjectList()
}
