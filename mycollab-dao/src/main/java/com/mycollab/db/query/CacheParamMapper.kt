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

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
object CacheParamMapper {
    private val map = mutableMapOf<String, MutableMap<String, ValueParam>>()

    @JvmStatic
    fun <P : Param> register(type: String, displayName: Enum<*>?, param: P): P {
        var valueParamMap: MutableMap<String, ValueParam>? = map[type]
        if (valueParamMap == null) {
            valueParamMap = mutableMapOf<String, ValueParam>()
            map.put(type, valueParamMap)
        }
        valueParamMap.put(param.id, ValueParam(displayName, param))
        return param
    }

    @JvmStatic
    fun getValueParam(type: String, id: String): ValueParam? {
        val valueParamMap = map[type]
        return if (valueParamMap != null) valueParamMap[id] else null
    }

    class ValueParam internal constructor(val displayName: Enum<*>?, val param: Param)
}
