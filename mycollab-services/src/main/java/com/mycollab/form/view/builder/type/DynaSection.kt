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
package com.mycollab.form.view.builder.type

import com.mycollab.form.view.LayoutType
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class DynaSection : Comparable<DynaSection> {
    var header: Enum<*>? = null
    var contextHelp: Enum<*>? = null
    var orderIndex: Int = 0
    var isDeletedSection = false
    lateinit var layoutType: LayoutType
    private val fields = ArrayList<AbstractDynaField>()
    var parentForm: DynaForm? = null

    val fieldCount: Int
        get() = fields.size

    fun fields(vararg dynaFields: AbstractDynaField): DynaSection {
        dynaFields.forEach {
            fields.add(it)
            it.ownSection = this
        }

        return this
    }

    fun getField(index: Int): AbstractDynaField = fields[index]

    override fun compareTo(other: DynaSection): Int = orderIndex - other.orderIndex
}
