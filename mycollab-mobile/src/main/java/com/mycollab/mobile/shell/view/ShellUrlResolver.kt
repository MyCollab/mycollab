package com.mycollab.mobile.shell.view

import com.mycollab.mobile.module.crm.view.CrmUrlResolver
import com.mycollab.mobile.module.project.view.ProjectUrlResolver
import com.mycollab.vaadin.mvp.UrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ShellUrlResolver : UrlResolver() {
    init {
        this.addSubResolver("crm", CrmUrlResolver().build())
        this.addSubResolver("project", ProjectUrlResolver().build())
    }

    fun navigateByFragement(fragement: String?) {
        if (fragement != null && fragement.isNotBlank()) {
            val tokens = fragement.split("/").toTypedArray()
            this.handle(*tokens)
        } else {
            defaultPageErrorHandler()
        }
    }

    override fun defaultPageErrorHandler() {}
}