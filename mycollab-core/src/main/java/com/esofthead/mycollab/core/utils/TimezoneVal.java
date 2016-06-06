/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.core.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MyCollab Ltd
 * @since 5.3.2
 */
public class TimezoneVal implements Comparable<TimezoneVal> {
    private static Map<String, List<TimezoneVal>> cacheTimezones = new ConcurrentHashMap<>();

    static {
        String[] zoneIds = TimeZone.getAvailableIDs();
        for (int i = 0; i < zoneIds.length; i++) {
            TimeZone timeZone = TimeZone.getTimeZone(zoneIds[i]);
            String timeZoneId = timeZone.getID();
            try {
                DateTimeZone.forTimeZone(timeZone); //check compatible between joda timezone and java timezone
                TimezoneVal timezoneVal = new TimezoneVal(zoneIds[i]);
                List<TimezoneVal> timeZones = cacheTimezones.get(timezoneVal.getArea());
                if (timeZones == null) {
                    timeZones = new ArrayList<>();
                    cacheTimezones.put(timezoneVal.getArea(), timeZones);
                }
                timeZones.add(timezoneVal);
            } catch (Exception e) {
                // ignore exception
            }
        }

        Set<String> keys = cacheTimezones.keySet();
        for (String key : keys) {
            List<TimezoneVal> timezones = cacheTimezones.get(key);
            Collections.sort(timezones);
        }
    }

    private String id;
    private TimeZone timezone;
    private String area;
    private String location;

    public TimezoneVal(String id) {
        this.id = id;
        this.timezone = (id != null) ? TimeZone.getTimeZone(id) : TimeZone.getDefault();
        String timeZoneId = timezone.getID();
        int index = timeZoneId.indexOf('/');
        location = (index > -1) ? timeZoneId.substring(index + 1, timeZoneId.length()) : timeZoneId;
        area = (index > -1) ? timeZoneId.substring(0, index) : "Others";
    }

    public String getDisplayName() {
        return getOffsetString(timezone) + " " + location;
    }

    public DateTimeZone getTimezone() {
        return DateTimeZone.forTimeZone(timezone);
    }

    @Override
    public int compareTo(TimezoneVal val) {
        int offsetInMillis1 = this.getTimezone().getOffset(new DateTime().getMillis());
        int offsetInMillis2 = val.getTimezone().getOffset(new DateTime().getMillis());
        return offsetInMillis1 - offsetInMillis2;
    }

    public String getId() {
        return id;
    }

    public String getArea() {
        return area;
    }

    public String getLocation() {
        return location;
    }

    private static String getOffsetString(TimeZone timeZone) {
        int offsetInMillis = DateTimeZone.forTimeZone(timeZone).getOffset(new DateTime().getMillis());
        String offset = String.format("%02d:%02d", Math.abs(offsetInMillis / 3600000),
                Math.abs((offsetInMillis / 60000) % 60));
        offset = "(GMT" + (offsetInMillis >= 0 ? "+" : "-") + offset + ")";
        return offset;
    }

    public static TimeZone valueOf(String timeZoneId) {
        if (StringUtils.isBlank(timeZoneId)) {
            return TimeZone.getDefault();
        }
        return TimeZone.getTimeZone(timeZoneId);
    }

    public static String getDisplayName(String timeZoneId) {
        TimeZone timeZone = valueOf(timeZoneId);
        String str = timeZone.getID();
        int index = str.indexOf('/');
        String location = (index > -1) ? str.substring(index + 1, str.length()) : str;
        return getOffsetString(timeZone) + " " + location;
    }

    public static String[] getAreas() {
        List<String> keys = new ArrayList<>(cacheTimezones.keySet());
        Collections.sort(keys);
        return keys.toArray(new String[keys.size()]);
    }

    public static Collection<TimezoneVal> getTimezoneInAreas(String area) {
        return cacheTimezones.get(area);
    }
}
