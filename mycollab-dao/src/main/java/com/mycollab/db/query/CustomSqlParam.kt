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
package com.mycollab.db.query

import com.mycollab.db.arguments.NoValueSearchField

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
abstract class CustomSqlParam(id: String) : Param(id) {

    abstract fun buildPropertyParamInList(oper: String, value: Collection<String>?): NoValueSearchField?

    abstract fun buildPropertyParamNotInList(oper: String, value: Collection<String>?): NoValueSearchField?
}
