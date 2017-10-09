package com.mycollab.schedule.email

import com.mycollab.common.domain.SimpleRelayEmailNotification
import com.mycollab.core.utils.TimezoneVal
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.user.domain.SimpleUser

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class MailContext<B>(val emailNotification: SimpleRelayEmailNotification,
                     val user: SimpleUser, val siteUrl: String) {
    val locale = LocalizationHelper.getLocaleInstance(user.language)
    val timeZone = TimezoneVal.valueOf(user.timezone)
    var wrappedBean: B? = null

    val saccountid
        get() = emailNotification.saccountid

    val changeByUserFullName
        get() = emailNotification.changeByUserFullName

    val typeid
        get() = emailNotification.typeid

    val type
        get() = emailNotification.type

    fun getMessage(key: Enum<*>, vararg params: String?) = LocalizationHelper.getMessage(locale, key, *params)

    fun getFieldName(fieldMapper: ItemFieldMapper, fieldName: String): String {
        val fieldFormat = fieldMapper.getField(fieldName)
        return if (fieldFormat != null) getMessage(fieldFormat.displayName) else ""
    }
}