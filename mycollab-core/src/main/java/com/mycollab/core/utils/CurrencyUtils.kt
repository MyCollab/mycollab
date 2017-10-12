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

import java.util.Currency
import java.util.Locale

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
object CurrencyUtils {
    @JvmStatic fun getInstance(currencyCode: String?): Currency {
        return try {
            Currency.getInstance(currencyCode)
        } catch (e: Exception) {
            Currency.getInstance(Locale.US)
        }
    }
}
