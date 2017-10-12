/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
