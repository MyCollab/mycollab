package com.mycollab.module.crm.view.activity

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.crm.domain.CallWithBLOBs
import com.mycollab.module.crm.event.ActivityEvent
import com.mycollab.module.crm.view.CrmUrlResolver

class CallUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("add", AddUrlResolver())
        this.addSubResolver("edit", EditUrlResolver())
        this.addSubResolver("preview", PreviewUrlResolver())
    }

    class AddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(ActivityEvent.CallAdd(this, CallWithBLOBs()))
    }

    class EditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val callId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(ActivityEvent.CallEdit(this, callId))
        }
    }

    class PreviewUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val callId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(ActivityEvent.CallRead(this, callId))
        }
    }
}