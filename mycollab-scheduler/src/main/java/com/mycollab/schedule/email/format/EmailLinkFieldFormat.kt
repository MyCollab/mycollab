/**
 * mycollab-scheduler - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.schedule.email.format

import com.hp.gagawa.java.elements.A
import com.hp.gagawa.java.elements.Span
import com.mycollab.schedule.email.MailContext
import org.apache.commons.beanutils.PropertyUtils
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class EmailLinkFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {
    companion object {
        private val LOG = LoggerFactory.getLogger(EmailLinkFieldFormat::class.java)
    }

    override fun formatField(context: MailContext<*>): String {
        val wrappedBean = context.wrappedBean
        return try {
            val value = PropertyUtils.getProperty(wrappedBean, fieldName)
            formatEmail(value as String)
        } catch (e: Exception) {
            LOG.error("Error", e)
            Span().write()
        }
    }

    override fun formatField(context: MailContext<*>, value: String): String = formatEmail(value)

    private fun formatEmail(value: String?): String {
        return if (value == null) {
            Span().write()
        } else {
            val link = A("mailto:" + value.toString()).setStyle("text-decoration: none; color: rgb(36, 127, 211);").appendText(value.toString())
            Span().appendChild(link).write()
        }
    }
}