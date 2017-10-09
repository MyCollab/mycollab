package com.mycollab.vaadin.mvp

import com.mycollab.vaadin.event.ViewEvent
import org.vaadin.viritin.layouts.MVerticalLayout

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
abstract class AbstractVerticalPageView : MVerticalLayout(), PageView {

    init {
        this.withSpacing(false).withMargin(false)
    }

    override fun <E> addViewListener(listener: PageView.ViewListener<E>) {
        addListener(ViewEvent.VIEW_IDENTIFIER, ViewEvent::class.java, listener, PageView.ViewListener.viewInitMethod)
    }
}
