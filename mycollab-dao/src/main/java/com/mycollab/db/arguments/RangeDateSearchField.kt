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
 * @author MyCollab Ltd.
 * @since 1.0
 */
class RangeDateSearchField : SearchField {

    var from: LocalDate? = null
    var to: LocalDate? = null

    constructor()

    constructor(from: LocalDate, to: LocalDate) : this(SearchField.AND, from, to)

    constructor(oper: String, from: LocalDate, to: LocalDate) {
        this.operation = oper
        this.from = from
        this.to = to
    }
}
