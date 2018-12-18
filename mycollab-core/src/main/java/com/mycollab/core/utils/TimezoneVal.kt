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
package com.mycollab.core.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 5.3.2
 */
class TimezoneVal(val id: String?) : Comparable<TimezoneVal> {
    private val timezone: ZoneId = if (id != null) ZoneId.of(id) else ZoneId.systemDefault()
    val area: String
    val location: String

    val displayName: String
        get() = "${getOffsetString(timezone)} $location"

    init {
        val timeZoneId = timezone.id
        val index = timeZoneId.indexOf('/')
        location = if (index > -1) timeZoneId.substring(index + 1, timeZoneId.length) else timeZoneId
        area = if (index > -1) timeZoneId.substring(0, index) else "Others"
    }

    override fun compareTo(other: TimezoneVal): Int {
        val now = LocalDateTime.now()
        val offset1 = now.atZone(timezone).offset.totalSeconds
        val offset2 = now.atZone(other.timezone).offset.totalSeconds
        return offset1 - offset2
    }

    companion object {
        private val cacheTimezones = mutableMapOf<String, MutableList<TimezoneVal>>()

        init {
            val zoneIds = TimeZone.getAvailableIDs()
            zoneIds.forEach {
                try {
                    val timezoneVal = TimezoneVal(it)
                    var timeZones = cacheTimezones[timezoneVal.area]
                    if (timeZones == null) {
                        timeZones = ArrayList()
                        cacheTimezones[timezoneVal.area] = timeZones
                    }
                    timeZones.add(timezoneVal)
                } catch (e: Exception) {
                    // ignore exception
                }
            }

            val keys = cacheTimezones.keys
            keys.map { cacheTimezones[it] }.forEach { it!!.sort() }
        }

        private fun getOffsetString(timeZone: ZoneId): String {
            val offsetInSeconds = LocalDateTime.now().atZone(timeZone).offset.totalSeconds
            var offset = String.format("%02d:%02d", Math.abs(offsetInSeconds / 3600),
                    Math.abs(offsetInSeconds / 60 % 60))
            offset = """(GMT${if (offsetInSeconds >= 0) "+" else "-"}$offset)"""
            return offset
        }

        @JvmStatic
        fun valueOf(timeZoneId: String?): ZoneId = if (StringUtils.isBlank(timeZoneId)) {
            ZoneId.systemDefault()
        } else ZoneId.of(timeZoneId)

        @JvmStatic
        fun getDisplayName(locale: Locale, timeZoneId: String?): String {
            val timeZone = valueOf(timeZoneId)
            return "${getOffsetString(timeZone)} ${timeZone.getDisplayName(TextStyle.SHORT, locale)}"
        }

        @JvmStatic
        val areas: Array<String>
            get() {
                val keys = ArrayList(cacheTimezones.keys)
                keys.sort()
                return keys.toTypedArray()
            }

        @JvmStatic
        fun getTimezoneInAreas(area: String): MutableList<TimezoneVal>? = cacheTimezones[area]
    }
}
