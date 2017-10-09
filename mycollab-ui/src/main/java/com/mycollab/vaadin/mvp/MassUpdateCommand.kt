package com.mycollab.vaadin.mvp

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
interface MassUpdateCommand<in V> {
    fun massUpdate(value: V)
}