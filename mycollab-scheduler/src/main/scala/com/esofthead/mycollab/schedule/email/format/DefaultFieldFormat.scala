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
import com.hp.gagawa.java.elements.Span
import org.apache.commons.beanutils.PropertyUtils
import org.apache.commons.lang3.StringUtils
import org.slf4j.{Logger, LoggerFactory}

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
class DefaultFieldFormat(fieldName: String, displayName: Enum[_], isColSpan: Boolean) extends FieldFormat(fieldName,
    displayName, isColSpan) {
    private val LOG: Logger = LoggerFactory.getLogger(classOf[DefaultFieldFormat])

    def this(fieldName: String, displayName: Enum[_]) = this(fieldName, displayName, false)

    override def formatField(context: MailContext[_]): String = {
        val wrappedBean = context.wrappedBean

        try {
            val value = PropertyUtils.getProperty(wrappedBean, fieldName)
            if (value == null) {
                new Span().write
            }
            else {
                new Span().appendText(value.toString).write
            }
        } catch {
            case e: Any =>
                LOG.error("Can not generate email field: " + fieldName, e)
                new Span().write
        }
    }

    override def formatField(context: MailContext[_], value: String): String = {
        if (StringUtils.isBlank(value)) {
            new Span().write
        }

        value
    }
}
