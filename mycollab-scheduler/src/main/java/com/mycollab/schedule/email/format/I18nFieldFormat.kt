package com.mycollab.schedule.email.format

import com.hp.gagawa.java.elements.Span
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.schedule.email.MailContext
import org.apache.commons.beanutils.PropertyUtils
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class I18nFieldFormat : FieldFormat {

    companion object {
        private val LOG = LoggerFactory.getLogger(I18nFieldFormat::class.java)
    }

    private var enumKey: Class<out Enum<*>>? = null

    constructor(fieldName: String, displayName: Enum<*>, enumKey: Class<out Enum<*>>) : this(fieldName, displayName, enumKey, false)

    constructor(fieldName: String, displayName: Enum<*>, enumKey: Class<out Enum<*>>,
                isColSpan: Boolean?) : super(fieldName, displayName, isColSpan!!) {
        this.enumKey = enumKey
    }

    override fun formatField(context: MailContext<*>): String {
        val wrappedBean = context.wrappedBean
        return try {
            val value = PropertyUtils.getProperty(wrappedBean, fieldName)
            when (value) {
                null -> Span().write()
                else -> Span().appendText(LocalizationHelper.getMessage(context.locale, enumKey, value.toString())).write()
            }
        } catch (e: Exception) {
            LOG.error("Can not generate of object $wrappedBean field: $fieldName and value", e)
            Span().write()
        }
    }

    override fun formatField(context: MailContext<*>, value: String): String {
        return try {
            LocalizationHelper.getMessage(context.locale, enumKey, value)
        } catch (e: Exception) {
            LOG.error("Can not generate of object field: $fieldName and $value", e)
            Span().write()
        }
    }
}