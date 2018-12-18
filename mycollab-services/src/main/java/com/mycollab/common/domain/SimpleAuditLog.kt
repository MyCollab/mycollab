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

import com.fasterxml.jackson.core.type.TypeReference
import com.mycollab.core.utils.JsonDeSerializer
import com.mycollab.core.utils.StringUtils
import java.time.LocalDateTime
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class SimpleAuditLog : AuditLog() {

    var changeItems: List<AuditChangeItem>? = null
        get() = if (field == null) {
            val result = parseChangeItems()
            result ?: ArrayList()
        } else field

    var postedUserFullName: String? = null
        get() = if (StringUtils.isBlank(field)) {
            StringUtils.extractNameFromEmail(posteduser)
        } else field

    var postedUserAvatarId: String? = null

    val createdtime: LocalDateTime
        get() = posteddate

    private fun parseChangeItems(): List<AuditChangeItem>? = JsonDeSerializer.fromJson<List<AuditChangeItem>>(
            this.changeset, object : TypeReference<List<AuditChangeItem>>() {

    })
}
