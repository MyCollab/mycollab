package com.mycollab.shell.view

import com.mycollab.vaadin.mvp.PageView
import com.mycollab.web.IDesktopModule

/**
 * @author MyCollab Ltd
 * @since 5.0.8
 */
interface MainView : PageView {
    fun display()

    fun addModule(module: IDesktopModule)
}
