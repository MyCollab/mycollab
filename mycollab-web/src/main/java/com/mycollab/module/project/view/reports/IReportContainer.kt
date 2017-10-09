package com.mycollab.module.project.view.reports

import com.mycollab.vaadin.mvp.PageView

/**
 * @author MyCollab Ltd
 * @since 5.2.10
 */
interface IReportContainer : PageView {
    fun addView(view: PageView)

    fun showDashboard()
}
