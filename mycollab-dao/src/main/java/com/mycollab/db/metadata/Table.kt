package com.mycollab.db.metadata

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class Table(val value: String)
