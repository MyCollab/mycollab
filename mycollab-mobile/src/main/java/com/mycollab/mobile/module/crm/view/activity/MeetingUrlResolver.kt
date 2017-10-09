package com.mycollab.mobile.module.crm.view.activity

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.crm.event.ActivityEvent
import com.mycollab.mobile.module.crm.view.CrmUrlResolver
import com.mycollab.module.crm.domain.MeetingWithBLOBs

class MeetingUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("add", MeetingAddUrlResolver())
        this.addSubResolver("edit", MeetingEditUrlResolver())
        this.addSubResolver("preview", MeetingPreviewUrlResolver())
    }

    class MeetingAddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(ActivityEvent.MeetingAdd(this, MeetingWithBLOBs()))
    }

    class MeetingEditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val meetingId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(ActivityEvent.MeetingEdit(this, meetingId))
        }
    }

    class MeetingPreviewUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val accountId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(ActivityEvent.MeetingRead(this, accountId))
        }
    }
}