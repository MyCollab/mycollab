package com.mycollab.module.crm.view.activity

import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.crm.event.ActivityEvent
import com.mycollab.module.crm.view.CrmUrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ActivityUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("calendar", ActivityCalendartUrlResolver())
        this.addSubResolver("todo", ActivityTodoAddUrlResolver())
        this.addSubResolver("task", ActivityTaskUrlResolver())
        this.addSubResolver("meeting", MeetingUrlResolver())
        this.addSubResolver("call", CallUrlResolver())
    }

    class ActivityCalendartUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            EventBusFactory.getInstance().post(ActivityEvent.GotoCalendar(this, null))
        }
    }

    class ActivityTodoAddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            EventBusFactory.getInstance().post(ActivityEvent.GotoTodoList(this, null))
        }
    }
}