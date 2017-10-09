package com.mycollab.vaadin.mvp

import com.mycollab.vaadin.event.ViewEvent
import com.vaadin.ui.CssLayout

import java.io.Serializable

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
abstract class AbstractCssPageView : CssLayout(), PageView, Serializable {
    init {
        this.setSizeFull()
    }

    override fun <E> addViewListener(listener: PageView.ViewListener<E>) {
        addListener(ViewEvent.VIEW_IDENTIFIER, ViewEvent::class.java, listener, PageView.ViewListener.viewInitMethod)
    }
}
