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
package com.mycollab.common

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object GenericLinkUtils {
    @JvmField val URL_PREFIX_PARAM = "#"

    /**
     * @param params
     * @return
     */
    @JvmStatic fun encodeParam(vararg params: Any?): String {
        val paramStr = StringBuilder("")
        params.indices.forEach { i ->
            if (params[i] != null) {
                paramStr.append(params[i].toString())
                if (i != params.size - 1) {
                    paramStr.append("/")
                }
            }
        }
        return UrlEncodeDecoder.encode(paramStr.toString())
    }
}
