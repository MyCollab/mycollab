package com.mycollab.vaadin.web.ui

import com.mycollab.vaadin.mvp.PageView

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
interface InitializingView : PageView {
    fun initContent()
}
