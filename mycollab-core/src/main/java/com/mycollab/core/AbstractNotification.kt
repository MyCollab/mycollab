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
package com.mycollab.core

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
abstract class AbstractNotification(val scope: String, val kind: String) {

    val isGlobalScope: Boolean
        get() = SCOPE_GLOBAL == scope

    companion object {

        @JvmField
        val WARNING = "warning"

        @JvmField
        val NEWS = "news"

        @JvmField
        val SCOPE_GLOBAL = "global"

        @JvmField
        val SCOPE_USER = "user"
    }
}
