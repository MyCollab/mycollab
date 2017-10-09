package com.mycollab.module.file.view

import com.mycollab.vaadin.EventBusFactory
import com.mycollab.shell.event.ShellEvent
import com.mycollab.vaadin.mvp.UrlResolver
import com.mycollab.vaadin.web.ui.ModuleHelper

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
open class FileUrlResolver : UrlResolver() {
    fun build(): UrlResolver {
        this.addSubResolver("list", FileListUrlResolver())
        return this
    }

    override fun handle(vararg params: String) {
        if (!ModuleHelper.isCurrentFileModule()) {
            EventBusFactory.getInstance().post(ShellEvent.GotoFileModule(this, params))
        } else {
            super.handle(*params)
        }
    }

    override fun defaultPageErrorHandler() {}

    class FileListUrlResolver : FileUrlResolver()
}