package com.mycollab.core.cache

import java.lang.annotation.*
import kotlin.reflect.KClass

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
@Inherited
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class CacheArgs(val values: Array<KClass<*>>)
