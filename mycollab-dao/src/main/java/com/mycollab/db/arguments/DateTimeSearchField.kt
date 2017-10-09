package com.mycollab.db.arguments

import com.mycollab.core.utils.DateTimeUtils
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class DateTimeSearchField(operation: String, var comparison: String, var dateVal: Date, var value: Date = DateTimeUtils.convertDateTimeToUTC(dateVal)) : SearchField(operation) {
    companion object {
        @JvmField val LESS_THAN = "<"

        @JvmField val LESS_THAN_EQUAL = "<="

        @JvmField val GREATER_THAN = ">"

        @JvmField val GREATER_THAN_EQUAL = ">="

        @JvmField val EQUAL = "="

        @JvmField val NOT_EQUAL = "<>"
    }
}