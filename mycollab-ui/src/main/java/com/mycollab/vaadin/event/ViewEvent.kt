package com.mycollab.vaadin.event

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ViewEvent<B>(source: Any, val data: Any) : ApplicationEvent(source) {
    companion object {
        @JvmField val VIEW_IDENTIFIER = "viewEvent"
    }
}