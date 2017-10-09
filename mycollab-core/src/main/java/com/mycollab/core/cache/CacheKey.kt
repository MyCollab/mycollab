package com.mycollab.core.cache

/**
 * MyCollab cache is a distributed map with key and value. This annotation
 * denote a pameter of method is played as cache key.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class CacheKey
