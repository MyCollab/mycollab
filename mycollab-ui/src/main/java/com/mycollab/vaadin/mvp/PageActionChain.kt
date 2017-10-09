package com.mycollab.vaadin.mvp

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class PageActionChain(vararg pageActionArr: ScreenData<Any>) {
    private val chains = mutableListOf<ScreenData<Any>>()

    init {
        chains.addAll(pageActionArr)
    }

    fun add(pageAction: ScreenData<Any>): PageActionChain {
        chains.add(pageAction)
        return this
    }

    fun pop(): ScreenData<Any>? {
        return if (chains.size > 0) {
            val pageAction = chains[0]
            chains.removeAt(0)
            pageAction
        } else null
    }

    fun peek(): ScreenData<Any> = chains[0]

    fun hasNext(): Boolean = chains.size > 0
}