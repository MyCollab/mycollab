package com.mycollab.core.cache

import java.lang.annotation.Inherited


/**
 * **NOTE: ** Implement of cache just be presented in premium or ondemand
 * delivery.<br></br>
 * This annotation denotes a method is cached its value base on the key compose
 * by its arguments. Mycollab cache user data base on user account value base on
 * user account
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Inherited
annotation class Cacheable
