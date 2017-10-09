package com.mycollab.module.crm.view.activity

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.crm.domain.MeetingWithBLOBs
import com.mycollab.module.crm.event.ActivityEvent
import com.mycollab.module.crm.view.CrmUrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
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
            val meetingId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(ActivityEvent.MeetingRead(this, meetingId))
        }
    }
}