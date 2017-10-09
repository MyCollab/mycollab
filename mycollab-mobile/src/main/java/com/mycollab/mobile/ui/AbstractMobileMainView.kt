package com.mycollab.mobile.ui

import com.mycollab.vaadin.event.ViewEvent
import com.mycollab.vaadin.mvp.PageView
import com.vaadin.ui.VerticalLayout

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
abstract class AbstractMobileMainView : VerticalLayout(), PageView {
    override fun <E> addViewListener(listener: PageView.ViewListener<E>) {
        addListener(ViewEvent.VIEW_IDENTIFIER, ViewEvent::class.java, listener, PageView.ViewListener.viewInitMethod)
    }
}