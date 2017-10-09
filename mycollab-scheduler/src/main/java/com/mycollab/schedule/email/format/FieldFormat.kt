package com.mycollab.schedule.email.format

import com.mycollab.schedule.email.MailContext

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
abstract class FieldFormat(var fieldName: String, var displayName: Enum<*>,
                           var isColSpan: Boolean) {
    constructor(fieldName: String, displayName: Enum<*>): this(fieldName, displayName, false)

    /**
     *
     * @param context
     * @return
     */
    abstract fun formatField(context: MailContext<*>): String

    /**
     *
     * @param context
     * @param value
     * @return
     */
    abstract fun formatField(context: MailContext<*>, value: String): String

    enum class  Type {
        DEFAULT, DATE, DATE_TIME, CURRENCY
    }
}