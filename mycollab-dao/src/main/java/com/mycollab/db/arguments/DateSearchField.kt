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