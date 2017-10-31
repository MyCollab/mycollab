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

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author MyCollab Ltd
 * @since 5.3.2
 */
class TimezoneVal(val id: String?) : Comparable<TimezoneVal> {
    private val timezone: TimeZone = if (id != null) TimeZone.getTimeZone(id) else TimeZone.getDefault()
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

    fun getTimezone(): DateTimeZone = DateTimeZone.forTimeZone(timezone)

    override fun compareTo(other: TimezoneVal): Int {
        val offsetInMillis1 = this.getTimezone().getOffset(DateTime().millis)
        val offsetInMillis2 = other.getTimezone().getOffset(DateTime().millis)
        return offsetInMillis1 - offsetInMillis2
    }

    companion object {
        private val cacheTimezones = mutableMapOf<String, MutableList<TimezoneVal>>()

        init {
            val zoneIds = TimeZone.getAvailableIDs()
            zoneIds.forEach {
                val timeZone = TimeZone.getTimeZone(it)
                try {
                    DateTimeZone.forTimeZone(timeZone) //check compatible between joda timezone and java timezone
                    val timezoneVal = TimezoneVal(it)
                    var timeZones = cacheTimezones[timezoneVal.area]
                    if (timeZones == null) {
                        timeZones = ArrayList()
                        cacheTimezones.put(timezoneVal.area, timeZones)
                    }
                    timeZones.add(timezoneVal)
                } catch (e: Exception) {
                    // ignore exception
                }
            }

            val keys = cacheTimezones.keys
            keys.map { cacheTimezones[it] }.forEach { Collections.sort(it) }
        }

        private fun getOffsetString(timeZone: TimeZone): String {
            val offsetInMillis = DateTimeZone.forTimeZone(timeZone).getOffset(DateTime().millis)
            var offset = String.format("%02d:%02d", Math.abs(offsetInMillis / 3600000),
                    Math.abs(offsetInMillis / 60000 % 60))
            offset = """(GMT${if (offsetInMillis >= 0) "+" else "-"}$offset)"""
            return offset
        }

        @JvmStatic
        fun valueOf(timeZoneId: String): TimeZone = if (StringUtils.isBlank(timeZoneId)) {
            TimeZone.getDefault()
        } else TimeZone.getTimeZone(timeZoneId)

        @JvmStatic
        fun getDisplayName(locale: Locale, timeZoneId: String): String {
            val timeZone = valueOf(timeZoneId)
            return "${getOffsetString(timeZone)} ${timeZone.getDisplayName(locale)}"
        }

        @JvmStatic
        val areas: Array<String>
            get() {
                val keys = ArrayList(cacheTimezones.keys)
                Collections.sort(keys)
                return keys.toTypedArray()
            }

        @JvmStatic
        fun getTimezoneInAreas(area: String): MutableList<TimezoneVal>? = cacheTimezones[area]
    }
}
