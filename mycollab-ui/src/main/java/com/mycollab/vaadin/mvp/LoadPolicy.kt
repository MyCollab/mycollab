package com.mycollab.vaadin.mvp

/**
 * @author MyCollab Ltd.
 * @since 5.0.5
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class LoadPolicy(val scope: ViewScope = ViewScope.SESSION)
