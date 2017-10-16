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

import java.util.ArrayList

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
class CompositionSearchField(oper: String) : SearchField() {

    private var fields: MutableList<SearchField>? = null

    init {
        this.operation = oper
    }

    fun getFields(): List<SearchField>? {
        return fields
    }

    fun setFields(fields: MutableList<SearchField>) {
        this.fields = fields
    }

    fun addField(field: SearchField) {
        if (fields == null) {
            fields = ArrayList()
        }

        fields!!.add(field)
    }
}
