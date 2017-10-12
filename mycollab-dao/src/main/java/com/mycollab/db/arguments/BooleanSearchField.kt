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

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class BooleanSearchField(oper: String, var comparision: String?, var isValue: Boolean) : SearchField() {

    @JvmOverloads constructor(value: Boolean = false) : this(SearchField.AND, value)

    constructor(oper: String, value: Boolean) : this(oper, BooleanSearchField.IS, value)

    init {
        this.operation = oper
    }

    companion object {
        private val serialVersionUID = 1L

        val IS = "is"
    }
}
