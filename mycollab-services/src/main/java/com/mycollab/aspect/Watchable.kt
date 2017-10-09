package com.mycollab.aspect


/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class Watchable(val userFieldName: String = "", val extraTypeId: String = "")