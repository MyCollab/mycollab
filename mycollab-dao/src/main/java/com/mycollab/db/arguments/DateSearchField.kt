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

import java.time.LocalDate

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class DateSearchField(operation: String, var value: LocalDate, var comparison: String = DateSearchField.LESS_THAN) : SearchField(operation) {

    constructor(dateVal: LocalDate, comparison: String) : this(SearchField.AND, dateVal, comparison)

    companion object {
        const val LESS_THAN = "<"

        const val LESS_THAN_EQUAL = "<="

        const val GREATER_THAN = ">"

        const val  GREATER_THAN_EQUAL = ">="

        const val  EQUAL = "="

        const val  NOT_EQUAL = "<>"
    }
}