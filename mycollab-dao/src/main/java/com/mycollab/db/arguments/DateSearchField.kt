package com.mycollab.db.arguments

import org.joda.time.LocalDate
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class DateSearchField(operation: String, var dateVal: Date, var comparison: String, var value: Date = LocalDate(dateVal).toDate()) : SearchField(operation) {
    constructor(dateVal: Date) : this(SearchField.AND, dateVal, DateSearchField.LESS_THAN)

    constructor(dateVal: Date, comparison: String) : this(SearchField.AND, dateVal, comparison)

    companion object {
        @JvmField val LESS_THAN = "<"

        @JvmField val LESS_THAN_EQUAL = "<="

        @JvmField val GREATER_THAN = ">"

        @JvmField val GREATER_THAN_EQUAL = ">="

        @JvmField val EQUAL = "="

        @JvmField val NOT_EQUAL = "<>"
    }
}