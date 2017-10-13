/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui.formatter

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
open class FieldGroupFormatter {

    private var fieldsFormat = mutableMapOf<String, DefaultFieldDisplayHandler>()

    fun generateFieldDisplayHandler(fieldName: String, displayName: Enum<*>) {
        fieldsFormat.put(fieldName, DefaultFieldDisplayHandler(displayName))
    }

    fun generateFieldDisplayHandler(fieldName: String, displayName: Enum<*>, format: HistoryFieldFormat) {
        fieldsFormat.put(fieldName, DefaultFieldDisplayHandler(displayName, format))
    }

    fun generateFieldDisplayHandler(fieldName: String, displayName: Enum<*>, formatName: String) {
        fieldsFormat.put(fieldName, DefaultFieldDisplayHandler(displayName, defaultFieldHandlers[formatName]!!))
    }

    fun getFieldDisplayHandler(fieldName: String): DefaultFieldDisplayHandler? = fieldsFormat[fieldName]

    companion object {

        @JvmField
        val DEFAULT_FIELD = "default"

        @JvmField
        val DATE_FIELD = "date"

        @JvmField
        val DATETIME_FIELD = "datetime"

        @JvmField
        val PRETTY_DATE_FIELD = "prettydate"

        @JvmField
        val PRETTY_DATE_TIME_FIELD = "prettydatetime"

        @JvmField
        val CURRENCY_FIELD = "currency"

        @JvmField
        val TRIM_HTMLS = "trim_htmls"

        private var defaultFieldHandlers: MutableMap<String, HistoryFieldFormat> = mutableMapOf(
                DEFAULT_FIELD to DefaultHistoryFieldFormat(),
                DATE_FIELD to DateHistoryFieldFormat(),
                DATETIME_FIELD to DateTimeHistoryFieldFormat(),
                PRETTY_DATE_FIELD to PrettyDateHistoryFieldFormat(),
                PRETTY_DATE_TIME_FIELD to PrettyDateTimeHistoryFieldFormat(),
                CURRENCY_FIELD to CurrencyHistoryFieldFormat(),
                TRIM_HTMLS to TrimHtmlHistoryFieldFormat())
    }
}