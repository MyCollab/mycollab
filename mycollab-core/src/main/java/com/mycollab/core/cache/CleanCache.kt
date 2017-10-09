package com.mycollab.core.cache

/**
 * @author MyCollab Ltd
 * @since 5.4.4
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class CleanCache
