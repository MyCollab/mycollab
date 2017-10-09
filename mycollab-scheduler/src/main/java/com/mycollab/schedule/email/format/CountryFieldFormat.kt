package com.mycollab.schedule.email.format

import com.hp.gagawa.java.elements.Span
import com.mycollab.core.utils.StringUtils
import com.mycollab.schedule.email.MailContext
import org.apache.commons.beanutils.PropertyUtils
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class CountryFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {
    override fun formatField(context: MailContext<*>): String {
        val wrappedBean = context.wrappedBean
        return try {
            val countryCode = PropertyUtils.getProperty(wrappedBean, fieldName) as String
            val locale = Locale("", countryCode)
            Span().appendText(locale.getDisplayCountry(locale)).write()
        } catch (e: Exception) {
            Span().write()
        }
    }

    override fun formatField(context: MailContext<*>, value: String): String {
        return when {
            StringUtils.isBlank(value) -> Span().write()
            else -> {
                val locale = Locale("", value)
                Span().appendText(locale.getDisplayCountry(context.locale)).write()
            }
        }

    }
}