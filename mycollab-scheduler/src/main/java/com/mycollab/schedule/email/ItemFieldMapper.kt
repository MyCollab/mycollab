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
package com.mycollab.schedule.email

import com.mycollab.schedule.email.format.DefaultFieldFormat
import com.mycollab.schedule.email.format.FieldFormat

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
open class ItemFieldMapper {
    private val fieldNameMap: MutableMap<String, FieldFormat> = mutableMapOf()

    fun put(fieldName: Enum<*>, displayName: Enum<*>) {
        fieldNameMap[fieldName.name] = DefaultFieldFormat(fieldName.name, displayName)
    }

    fun put(fieldName: Enum<*>, displayName: Enum<*>, isColSpan: Boolean) {
        fieldNameMap[fieldName.name] = DefaultFieldFormat(fieldName.name, displayName, isColSpan)
    }

    fun put(fieldName: Enum<*>, format: FieldFormat) {
        fieldNameMap[fieldName.name] = format
    }

    fun keySet(): Set<String> = fieldNameMap.keys

    fun hasField(fieldName: String): Boolean = fieldNameMap.contains(fieldName)

    fun getField(fieldName: String): FieldFormat? = fieldNameMap[fieldName]
}