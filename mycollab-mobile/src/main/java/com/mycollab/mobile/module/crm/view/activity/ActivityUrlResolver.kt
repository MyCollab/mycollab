package com.mycollab.mobile.module.crm.view.activity

import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.crm.event.CrmEvent
import com.mycollab.mobile.module.crm.view.CrmModuleScreenData
import com.mycollab.mobile.module.crm.view.CrmUrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ActivityUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("list", ActivityListUrlResolver())
        this.addSubResolver("task", ActivityTaskUrlResolver())
        this.addSubResolver("meeting", MeetingUrlResolver())
        this.addSubResolver("call", CallUrlResolver())
    }

    class ActivityListUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(CrmEvent.GotoActivitiesView(this,
                        CrmModuleScreenData.GotoModule(arrayOf())))
    }
}