package com.mycollab.module.project.view.time

import com.mycollab.vaadin.web.ui.InitializingView
import com.vaadin.ui.Component

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface IFinanceContainer : InitializingView {

    override fun initContent()

    fun gotoSubView(name: String): Component

    fun showTimeView()

    fun showInvoiceView()
}
