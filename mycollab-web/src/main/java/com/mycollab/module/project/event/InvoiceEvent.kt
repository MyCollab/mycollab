package com.mycollab.module.project.event

import com.mycollab.vaadin.event.ApplicationEvent
import com.mycollab.module.project.domain.SimpleInvoice

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object InvoiceEvent {
    class GotoList(source: Any, val data: Any?) : ApplicationEvent(source)

    class NewInvoiceAdded(source: Any, val data: SimpleInvoice) : ApplicationEvent(source)

    class InvoiceUpdateAdded(source: Any, val data: SimpleInvoice) : ApplicationEvent(source)

    class InvoiceDelete(source: Any, val data: SimpleInvoice) : ApplicationEvent(source)

    class DisplayInvoiceView(source: Any, val data: SimpleInvoice) : ApplicationEvent(source)
}