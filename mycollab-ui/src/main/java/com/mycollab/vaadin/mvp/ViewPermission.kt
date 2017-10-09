package com.mycollab.vaadin.mvp

/**
 * @author MyCollab Ltd
 * @since 2.0
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class ViewPermission(val permissionId: String, val impliedPermissionVal: Int)
