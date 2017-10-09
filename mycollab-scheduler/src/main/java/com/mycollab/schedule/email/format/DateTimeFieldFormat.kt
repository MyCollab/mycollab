package com.mycollab.schedule.email.format

import com.hp.gagawa.java.elements.Span
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.core.utils.StringUtils
import com.mycollab.core.utils.TimezoneVal
import com.mycollab.schedule.email.MailContext
import org.apache.commons.beanutils.PropertyUtils
import org.slf4j.LoggerFactory
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class DateTimeFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

    companion object {
        private val LOG = LoggerFactory.getLogger(DateTimeFieldFormat::class.java)
    }

    override fun formatField(context: MailContext<*>): String {
        val wrappedBean = context.wrappedBean
        return try {
            val value = PropertyUtils.getProperty(wrappedBean, fieldName)
            when (value) {
                null -> Span().write()
                else -> Span().appendText(DateTimeUtils.formatDate(value as Date, context.user.dateFormat,
                        context.locale, TimezoneVal.valueOf(context.user.timezone))).write()
            }
        } catch (e: Exception) {
            LOG.error("Can not generate email field: " + fieldName, e)
            Span().write()
        }
    }

    override fun formatField(context: MailContext<*>, value: String): String =
            if (StringUtils.isBlank(value)) Span().write() else
                DateTimeUtils.convertToStringWithUserTimeZone(value, context.user.dateFormat, context.locale, context.timeZone)
}