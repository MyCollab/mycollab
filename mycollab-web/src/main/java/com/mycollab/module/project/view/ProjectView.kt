package com.mycollab.module.project.view

import com.mycollab.module.project.domain.SimpleProject
import com.mycollab.vaadin.mvp.PageView
import com.vaadin.ui.Component

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface ProjectView : PageView {

    fun initView(project: SimpleProject)

    fun updateProjectFeatures()

    fun gotoSubView(name: String): Component

    fun setNavigatorVisibility(visibility: Boolean)

    fun displaySearchResult(value: String)
}
