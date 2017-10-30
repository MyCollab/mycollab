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

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class SimpleMonitorItem : MonitorItem() {

    var userAvatarId: String? = null
    var userFullname: String? = null
        get() {
            if (field == null || field!!.trim { it <= ' ' } == "") {
                val displayName = user
                val index = displayName?.indexOf("@") ?: 0
                if (index > 0) {
                    return displayName!!.substring(0, index)
                }
            }
            return field
        }
}
