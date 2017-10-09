package com.mycollab.module.crm.view.file

import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.crm.event.DocumentEvent
import com.mycollab.module.crm.view.CrmUrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class FileUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("dashboard", FileDashboardUrlResolver())
    }

    class FileDashboardUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(DocumentEvent.GotoDashboard(this, null))
    }
}