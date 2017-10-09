package com.mycollab.mobile.module.crm.event

import com.mycollab.vaadin.event.ApplicationEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object OpportunityEvent {

    class GotoList(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoAdd(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoEdit(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoRead(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoRelatedItems(source: Any, val data: Any?) : ApplicationEvent(source)
}