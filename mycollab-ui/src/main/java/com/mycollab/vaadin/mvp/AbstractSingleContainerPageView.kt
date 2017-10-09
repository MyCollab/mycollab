package com.mycollab.vaadin.mvp

import com.mycollab.vaadin.event.ViewEvent
import com.vaadin.ui.*
import com.vaadin.ui.HasComponents.ComponentAttachListener
import com.vaadin.ui.HasComponents.ComponentDetachListener

/**
 * @author MyCollab Ltd
 * @since 5.4.5
 */
open class AbstractSingleContainerPageView : CustomComponent(), PageView, SingleComponentContainer {

    private val contentLayout: CssLayout = CssLayout()

    init {
        contentLayout.setSizeFull()
        compositionRoot = contentLayout
        setSizeFull()
    }

    override fun getContent(): Component {
        return contentLayout.getComponent(0)
    }

    override fun setContent(component: Component) {
        contentLayout.removeAllComponents()
        contentLayout.addComponent(component)
    }

    override fun addComponentAttachListener(componentAttachListener: ComponentAttachListener) {
        contentLayout.addComponentAttachListener(componentAttachListener)
    }

    override fun removeComponentAttachListener(componentAttachListener: ComponentAttachListener) {

    }

    override fun addComponentDetachListener(componentDetachListener: ComponentDetachListener) {

    }

    override fun removeComponentDetachListener(componentDetachListener: ComponentDetachListener) {

    }

    override fun <E> addViewListener(listener: PageView.ViewListener<E>) {
        addListener(ViewEvent.VIEW_IDENTIFIER, ViewEvent::class.java, listener, PageView.ViewListener.viewInitMethod)
    }
}
