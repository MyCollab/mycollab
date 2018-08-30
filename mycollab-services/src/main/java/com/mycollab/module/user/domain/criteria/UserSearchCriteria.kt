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
package com.mycollab.module.user.domain.criteria

import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.db.arguments.*

import java.util.Date
import java.util.Locale

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class UserSearchCriteria : SearchCriteria() {

    var displayName: StringSearchField? = null
    var username: StringSearchField? = null
    var registerStatuses: SetSearchField<String>? = null
    var subdomain: StringSearchField? = null
    var statuses: SetSearchField<String>? = null

    // @NOTE: Only works with method find... not getTotalCount(...)
    fun setLastAccessTimeRange(from: Date, to: Date) {
        val expr = "s_user_account.lastAccessedTime >= '${DateTimeUtils.formatDate(from, "yyyy-MM-dd", Locale.US)}' AND s_user_account.lastAccessedTime <='${DateTimeUtils.formatDate(to, "yyyy-MM-dd", Locale.US)}'"
        val searchField = NoValueSearchField(SearchField.AND, expr)
        this.addExtraField(searchField)
    }
}
