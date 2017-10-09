package com.mycollab.module.project.view.settings

import com.mycollab.vaadin.mvp.PageView
import com.vaadin.ui.Component

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface UserSettingView : PageView {
    fun gotoSubView(name: String): Component
}
