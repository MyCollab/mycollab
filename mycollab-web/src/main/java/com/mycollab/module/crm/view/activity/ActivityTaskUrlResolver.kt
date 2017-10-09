package com.mycollab.module.crm.view.activity

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.crm.domain.CrmTask
import com.mycollab.module.crm.event.ActivityEvent
import com.mycollab.module.crm.view.CrmUrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ActivityTaskUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("add", TaskAddUrlResolver())
        this.addSubResolver("edit", TaskEditUrlResolver())
        this.addSubResolver("preview", TaskPreviewUrlResolver())
    }

    class TaskAddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(ActivityEvent.TaskAdd(this, CrmTask()))
    }

    class TaskEditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val meetingId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(ActivityEvent.TaskEdit(this, meetingId))
        }
    }

    class TaskPreviewUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val accountId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(ActivityEvent.TaskRead(this, accountId))
        }
    }
}