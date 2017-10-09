package com.mycollab.mobile.module.crm.view.activity

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.crm.event.ActivityEvent
import com.mycollab.mobile.module.crm.view.CrmUrlResolver
import com.mycollab.module.crm.domain.CallWithBLOBs

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class CallUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("add", CallAddUrlResolver())
        this.addSubResolver("edit", CallEditUrlResolver())
        this.addSubResolver("preview", CallPreviewUrlResolver())
    }

    class CallAddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(ActivityEvent.CallAdd(this, CallWithBLOBs()))
    }

    class CallEditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val meetingId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(ActivityEvent.CallEdit(this, meetingId))
        }
    }

    class CallPreviewUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val accountId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(ActivityEvent.CallRead(this, accountId))
        }
    }
}