/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.schedule.email.format

import com.esofthead.mycollab.schedule.email.MailContext
import com.hp.gagawa.java.elements.{A, Span}
import org.apache.commons.beanutils.PropertyUtils
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
class EmailLinkFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {
    val LOG = LoggerFactory.getLogger(classOf[EmailLinkFieldFormat])

    override def formatField(context: MailContext[_]): String = {
        val wrappedBean = context.wrappedBean
        try {
            val value = PropertyUtils.getProperty(wrappedBean, fieldName)
            formatEmail(value.asInstanceOf[String])
        } catch {
            case e: Any =>
                LOG.error("Error", e)
                new Span().write
        }
    }

    override def formatField(context: MailContext[_], value: String): String = formatEmail(value)

    private def formatEmail(value: String): String = {
        if (value == null) {
            new Span().write
        } else {
            val link: A = new A
            link.setStyle("text-decoration: none; color: rgb(36, 127, 211);")
            link.setHref("mailto:" + value.toString)
            link.appendText(value.toString)
            new Span().appendChild(link).write
        }
    }
}
