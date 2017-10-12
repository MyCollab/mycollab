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
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class UrlTokenizer(url: String) {

    private var internalVal: String = "";
    var remainValue: String = ""
    var query: String = ""

    @Throws(InvalidTokenException::class)
    fun getInt(): Int {
        if (hasMoreTokens())
            return getNextToken().toInt()
        else throw InvalidTokenException("Invalid token $internalVal")
    }

    @Throws(InvalidTokenException::class)
    fun getString(): String = if (hasMoreTokens()) getNextToken() else throw InvalidTokenException("Invalid token " + internalVal)

    private fun hasMoreTokens(): Boolean = remainValue != ""

    private fun getNextToken(): String {
        val index = remainValue.indexOf("/")
        return if (index < 0) {
            val result = remainValue + ""
            remainValue = ""
            result
        } else {
            val result = remainValue.substring(0, index)
            remainValue = remainValue.substring(index + 1)
            result
        }
    }

    init {
        internalVal = if (url.startsWith("/")) url.substring(1) else url
        val queryIndex: Int = internalVal.indexOf("?")
        if (queryIndex != -1) {
            query = internalVal.substring(queryIndex + 1)
            internalVal = internalVal.substring(0, queryIndex)
        }
        internalVal = UrlEncodeDecoder.decode(internalVal)
        remainValue = internalVal
    }
}