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
package com.mycollab.module.crm

import com.mycollab.core.MyCollabException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.lang.reflect.Method

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object CrmResources {
    private val LOG = LoggerFactory.getLogger(CrmResources::class.java)
    private var getResMethod: Method? = null

    init {
        try {
            val resourceCls = Class.forName("com.mycollab.module.crm.ui.CrmAssetsManager")
            getResMethod = resourceCls.getMethod("toHexString", String::class.java)
        } catch (e: Exception) {
            throw MyCollabException("Can not reload resource", e)
        }

    }

    fun getFontIconHtml(type: String): String {
        try {
            val codePoint = getResMethod!!.invoke(null, type) as String
            return String.format("<span class=\"v-icon\" style=\"font-family: FontAwesome;\">%s;</span>", codePoint)
        } catch (e: Exception) {
            LOG.error("Can not get resource type {}", type)
            return ""
        }

    }
}
