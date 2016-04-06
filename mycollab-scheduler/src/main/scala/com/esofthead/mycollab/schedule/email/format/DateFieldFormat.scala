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

import java.util.Date

import com.esofthead.mycollab.configuration.LocaleHelper
import com.esofthead.mycollab.core.utils.{StringUtils, DateTimeUtils, TimezoneMapper}
import com.esofthead.mycollab.schedule.email.MailContext
import com.hp.gagawa.java.elements.Span
import org.apache.commons.beanutils.PropertyUtils
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
class DateFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {
    private val LOG = LoggerFactory.getLogger(classOf[DateFieldFormat])

    override def formatField(context: MailContext[_]): String = {
        val wrappedBean = context.wrappedBean
        var value: AnyRef = null
        try {
            value = PropertyUtils.getProperty(wrappedBean, fieldName)
            if (value == null) {
                new Span().write
            } else {
                new Span().appendText(DateTimeUtils.formatDate(value.asInstanceOf[Date], LocaleHelper.getDateFormatInstance
                    (context.getLocale).getDateFormat,
                    TimezoneMapper.getTimezone(context.getUser.getTimezone))).write
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

        DateTimeUtils.convertToStringWithUserTimeZone(value, LocaleHelper.getDateFormatInstance(context.getLocale).getDateFormat,
            context.getTimeZone)
    }
}
