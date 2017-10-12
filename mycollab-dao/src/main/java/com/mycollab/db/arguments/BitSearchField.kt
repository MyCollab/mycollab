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

import com.mycollab.core.utils.BeanUtility

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class BitSearchField @JvmOverloads constructor(oper: String = SearchField.AND, value: Int = 0, compareOperator: String = NumberSearchField.EQUAL) : NumberSearchField(oper, value, compareOperator) {

    override fun toString(): String {
        return BeanUtility.printBeanObj(this)
    }

    companion object {
        private val serialVersionUID = 1L

        @JvmField val TRUE = BitSearchField(SearchField.AND, 1)
        @JvmField val FALSE = BitSearchField(SearchField.AND, 0)
    }
}
