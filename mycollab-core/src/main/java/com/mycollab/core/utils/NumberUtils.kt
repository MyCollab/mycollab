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
package com.mycollab.core.utils

import java.math.BigDecimal

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
object NumberUtils {
    @JvmStatic
    fun roundDouble(scale: Int, value: Double?): Double =
            when (value) {
                null -> 0.0
                else -> BigDecimal(value.toString()).setScale(scale, BigDecimal.ROUND_HALF_UP).toDouble()
            }

    @JvmStatic
    fun zeroIfNull(value: Number?): Number = value ?: 0
}
