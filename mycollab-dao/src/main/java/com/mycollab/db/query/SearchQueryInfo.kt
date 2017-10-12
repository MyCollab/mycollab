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

import com.mycollab.db.arguments.SearchCriteria
import java.util.Arrays

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
class SearchQueryInfo<S: SearchCriteria>(val queryId: String, var queryName: String, var searchFieldInfos: List<SearchFieldInfo<S>>) {

    constructor(queryName: String, vararg searchFieldInfoArr: SearchFieldInfo<S>) : this(queryName, Arrays.asList(*searchFieldInfoArr))

    constructor(queryName: String, searchFieldInfos: List<SearchFieldInfo<S>>) : this("", queryName, searchFieldInfos)

    constructor(queryId: String, queryName: String, vararg searchFieldInfoArr: SearchFieldInfo<S>) : this(queryId, queryName, Arrays.asList(*searchFieldInfoArr))
}
