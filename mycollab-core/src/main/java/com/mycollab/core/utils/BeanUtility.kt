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
package com.mycollab.core.utils

import com.rits.cloning.Cloner
import org.apache.commons.lang.builder.ToStringBuilder
import org.apache.commons.lang.builder.ToStringStyle

/**
 * Utility class to print bean properties. This class is used for debug purpose
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
object BeanUtility {

    @JvmStatic fun printBeanObj(bean: Any): String {
        return ToStringBuilder.reflectionToString(bean, ToStringStyle.SHORT_PREFIX_STYLE)
    }

    @JvmStatic fun <B> deepClone(b: B): B {
        return Cloner().deepClone(b)
    }
}
