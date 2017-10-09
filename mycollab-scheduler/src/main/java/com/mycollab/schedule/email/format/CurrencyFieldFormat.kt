package com.mycollab.schedule.email.format

import com.hp.gagawa.java.elements.Span
import com.mycollab.core.utils.CurrencyUtils
import com.mycollab.core.utils.StringUtils
import com.mycollab.schedule.email.MailContext
import org.apache.commons.beanutils.PropertyUtils
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class CurrencyFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {
    companion object {
        private val LOG = LoggerFactory.getLogger(CurrencyFieldFormat::class.java)
    }

    override fun formatField(context: MailContext<*>): String {
        val wrappedBean = context.wrappedBean
        return try {
            val value = PropertyUtils.getProperty(wrappedBean, fieldName)
            when (value) {
                null -> Span().write()
                else -> Span().appendText(value as String).write()
            }
        } catch (e: Exception) {
            LOG.error("Can not generate email field: " + fieldName, e)
            Span().write()
        }
    }

    override fun formatField(context: MailContext<*>, value: String): String =
            if (StringUtils.isBlank(value)) Span().write() else CurrencyUtils.getInstance(value).getDisplayName()

}