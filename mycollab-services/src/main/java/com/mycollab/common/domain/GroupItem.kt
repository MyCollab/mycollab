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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.common.domain

import com.mycollab.core.arguments.ValuedBean

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class GroupItem : ValuedBean() {

    var groupid: String? = null
    var groupname: String? = null
    var value: Double? = null
    var extraValue: String? = null
    private var countNum: Int? = null

    fun getCountNum(): Int {
        return countNum!!
    }

    fun setCountNum(countNum: Int?) {
        this.countNum = countNum
    }
}
