package com.mycollab.db.metadata

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Column(val value: String)
