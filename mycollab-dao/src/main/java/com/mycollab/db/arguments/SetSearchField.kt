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
package com.mycollab.db.arguments

import com.mycollab.core.IgnoreException
import com.mycollab.core.utils.ArrayUtils
import org.apache.commons.collections.CollectionUtils

/**
 * @param <T>
 * @author MyCollab Ltd.
 * @since 1.0
</T> */
class SetSearchField<T> : SearchField {
    private var values: MutableSet<T> = mutableSetOf()

    constructor()

    constructor(vararg vals: T) {
        if (ArrayUtils.isNotEmpty(vals)) {
            CollectionUtils.addAll(values, vals)
        }

        this.operation = SearchField.AND
    }

    constructor(oper: String, vals: Collection<T>) : super(oper) {
        values.addAll(vals)
    }

    constructor(items: Collection<T>) {
        if (CollectionUtils.isNotEmpty(items)) {
            values.addAll(items)
        }
        this.operation = SearchField.AND
    }

    fun getValues(): Set<T> {
        if (values.isEmpty()) {
            throw IgnoreException("You must select one option")
        }
        return values
    }

    fun setValues(values: MutableSet<T>) {
        this.values = values
    }

    fun addValue(value: T) {
        values.add(value)
    }

    fun removeValue(value: T) {
        values.remove(value)
    }
}
