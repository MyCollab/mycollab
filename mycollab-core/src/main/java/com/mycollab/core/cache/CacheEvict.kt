package com.mycollab.core.cache

import java.lang.annotation.*

/**
 * **NOTE: ** Implement of cache just be presented in premium or ondemand
 * delivery.<br></br>
 * This annotation denotes a method has data clean of cache with the key compose
 * by its arguments.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Inherited
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class CacheEvict
