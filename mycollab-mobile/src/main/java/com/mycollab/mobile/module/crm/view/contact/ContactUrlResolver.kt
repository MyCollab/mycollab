package com.mycollab.mobile.module.crm.view.contact

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.crm.event.ContactEvent
import com.mycollab.mobile.module.crm.event.CrmEvent.GotoActivitiesView
import com.mycollab.mobile.module.crm.view.CrmModuleScreenData
import com.mycollab.mobile.module.crm.view.CrmUrlResolver
import com.mycollab.module.crm.domain.Contact

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ContactUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("list", ContactListUrlResolver())
        this.addSubResolver("preview", ContactPreviewUrlResolver())
        this.addSubResolver("add", ContactAddUrlResolver())
        this.addSubResolver("edit", ContactEditUrlResolver())
    }

    class ContactListUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(GotoActivitiesView(this,
                        CrmModuleScreenData.GotoModule(arrayOf())))
    }

    class ContactAddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            EventBusFactory.getInstance().post(ContactEvent.GotoAdd(this, Contact()))
        }
    }

    class ContactEditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val contactId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(ContactEvent.GotoEdit(this, contactId))
        }
    }

    class ContactPreviewUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val contactId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(ContactEvent.GotoRead(this, contactId))
        }
    }
}