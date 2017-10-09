package com.mycollab.shell.view

import com.mycollab.core.utils.StringUtils
import com.mycollab.module.crm.view.CrmUrlResolver
import com.mycollab.module.file.view.FileUrlResolver
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.user.accountsettings.view.AccountSettingUrlResolver
import com.mycollab.vaadin.mvp.UrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ShellUrlResolver : UrlResolver() {
    companion object {
        @JvmField
        val ROOT = ShellUrlResolver()
    }

    init {
        this.addSubResolver("crm", CrmUrlResolver().build())
        this.addSubResolver("project", ProjectUrlResolver().build())
        this.addSubResolver("account", AccountSettingUrlResolver().build())
        this.addSubResolver("document", FileUrlResolver().build())
    }

    fun resolveFragment(newFragmentUrl: String) {
        if (!StringUtils.isBlank(newFragmentUrl)) {
            val tokens = newFragmentUrl.split("/").toTypedArray()
            this.handle(*tokens)
        }
    }

    override fun defaultPageErrorHandler() {}
}