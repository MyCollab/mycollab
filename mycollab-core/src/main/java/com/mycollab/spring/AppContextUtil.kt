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
package com.mycollab.spring

import com.mycollab.core.MyCollabException
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

import javax.validation.Validator

/**
 * Static spring application context to retrieve spring bean without in servlet
 * context
 *
 * @author MyCollab Ltd
 * @since 1.0.0
 */
@Component("appContextUtil")
@Profile("production", "test")
class AppContextUtil : ApplicationContextAware {

    @Throws(BeansException::class)
    override fun setApplicationContext(appContext: ApplicationContext) {
        ctx = appContext
    }

    companion object {
        private var ctx: ApplicationContext? = null

        @JvmStatic fun <T> getSpringBean(name: String, classType: Class<T>): T {
            try {
                return ctx!!.getBean(name, classType)
            } catch (e: Exception) {
                throw MyCollabException("Can not find service " + name)
            }

        }

        @JvmStatic val validator: Validator
            get() = getSpringBean("validator", LocalValidatorFactoryBean::class.java)

        @JvmStatic fun <T> getSpringBean(classType: Class<T>): T {
            try {
                return ctx!!.getBean(classType)
            } catch (e: Exception) {
                throw MyCollabException("Can not find service " + classType, e)
            }
        }
    }
}
