package com.mycollab.module.crm.view.contact

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.crm.domain.Contact
import com.mycollab.module.crm.event.ContactEvent
import com.mycollab.module.crm.view.CrmUrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ContactUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("list", ListUrlResolver())
        this.addSubResolver("preview", PreviewUrlResolver())
        this.addSubResolver("add", AddUrlResolver())
        this.addSubResolver("edit", EditUrlResolver())
    }

    class ListUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(ContactEvent.GotoList(this, null))
    }

    class AddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(ContactEvent.GotoAdd(this, Contact()))
    }

    class EditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val contactId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(ContactEvent.GotoEdit(this, contactId))
        }
    }

    class PreviewUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val contactId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(ContactEvent.GotoRead(this, contactId))
        }
    }
}