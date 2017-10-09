package com.mycollab.vaadin.mvp

import com.vaadin.ui.HasComponents

import java.io.Serializable

/**
 * @param <V>
 * @author MyCollab Ltd
 * @since 2.0
</V> */
interface IPresenter<out V : PageView> : Serializable {

    /**
     * @param container
     * @param pageActionChain
     */
    fun handleChain(container: HasComponents, pageActionChain: PageActionChain)

    /**
     * @param container
     * @param data
     */
    fun go(container: HasComponents, data: ScreenData<Any>?): Boolean

    /**
     * @return
     */
    fun getView(): V?
}
