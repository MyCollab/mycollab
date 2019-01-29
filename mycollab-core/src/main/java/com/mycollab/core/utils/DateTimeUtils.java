/**
 * Copyright Â© MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.core.utils;

import org.ocpsoft.prettytime.PrettyTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * Utility class to process date instance
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DateTimeUtils {
    private static final Logger LOG = LoggerFactory.getLogger(DateTimeUtils.class);

    public static Integer MILLISECONDS_IN_A_DAY = 1000*60*60*24;

    private static ZoneId utcZone = ZoneId.of("UTC");

    public static LocalDateTime getCurrentDateWithoutMS() {
        return LocalDate.now().atStartOfDay();
    }

    public static LocalDate convertDateByString(String strDate, String format) {
        if (!StringUtils.isBlank(strDate)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(strDate, formatter);
        } else {
            throw new IllegalArgumentException("Date string must be not null");
        }
    }

    public static String convertToStringWithUserTimeZone(String dateVal, String dateFormat, Locale locale, ZoneId userTimeZone) {
        LocalDateTime date = parseDateTimeWithMilisByW3C(dateVal);
        return convertToStringWithUserTimeZone(date, dateFormat, locale, userTimeZone);
    }

    /**
     * @param strDate
     * @return
     */
    public static LocalDateTime parseDateTimeWithMilisByW3C(String strDate) {
        if (StringUtils.isNotBlank(strDate)) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                return LocalDateTime.parse(strDate, formatter);
            } catch (DateTimeParseException e) {
                LOG.error("Error while parse date", e);
            }
        }
        return null;
    }

    public static LocalDate parseDate(String strDate) {
        if (StringUtils.isNotBlank(strDate)) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return LocalDate.parse(strDate, formatter);
            } catch (DateTimeParseException e) {
                LOG.error("Error while parse date", e);
            }
        }
        return null;
    }

    public static String formatDateToW3C(LocalDate date) {
        if (date != null) {
            String formatW3C = "yyyy-MM-dd";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatW3C);
            return formatter.format(date);
        }
        return "";
    }

    public static String convertToStringWithUserTimeZone(LocalDateTime date, String dateFormat, Locale locale, ZoneId userTimeZone) {
        if (date == null)
            return "";
        return formatDate(date, dateFormat, locale, userTimeZone);
    }

    public static String getPrettyDateValue(LocalDateTime dateTime, ZoneId zoneId, Locale locale) {

        if (dateTime == null) {
            return "";
        }
        PrettyTime p = new PrettyTime(locale);
        return p.format(convertLocalDateTimeToDate(dateTime, zoneId));
    }

    public static Date convertLocalDateTimeToDate(LocalDateTime localDT, ZoneId zoneId) {
        return Date.from(localDT.atZone(zoneId).toInstant());
    }


    public static String formatDate(TemporalAccessor date, String dateFormat, Locale locale) {
        return formatDate(date, dateFormat, locale, null);
    }

    public static String formatDate(TemporalAccessor date, String dateFormat, Locale locale, ZoneId timezone) {
        if (date == null) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat).withLocale(locale);
        if (timezone != null) {
            formatter = formatter.withZone(timezone);
        }

        return formatter.format(date);
    }

    /**
     * @param date
     * @return
     */
    public static LocalDateTime convertDateTimeToUTC(LocalDateTime date) {
        return convertDateTimeByTimezone(date, utcZone);
    }

    public static LocalDateTime convertDateTimeByTimezone(LocalDateTime date, ZoneId timeZone) {
        return date.atZone(timeZone).toLocalDateTime();
    }

    /**
     * Convert from UTC time to default time zone of system
     *
     * @param timeInMillis
     * @return
     */
    public static LocalDateTime convertTimeFromUTCToSystemTimezone(long timeInMillis) {
        return Instant.ofEpochMilli(timeInMillis).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * @param date
     * @return array of two date elements, first is the first day of week, and
     * the second is the end week date
     */
    public static LocalDate[] getBounceDatesOfWeek(LocalDate date) {
        return new LocalDate[]{getFirstDayOfWeek(date), getLastDayOfWeek(date)};
    }

    public static LocalDate getFirstDayOfWeek(LocalDate date) {
        return date.with(WeekFields.ISO.dayOfWeek(), 1);
    }

    public static LocalDate getLastDayOfWeek(LocalDate date) {
        return date.with(WeekFields.ISO.dayOfWeek(), 7);
    }

    public static LocalDate min(LocalDate... values) {
        return Arrays.stream(values).min(LocalDate::compareTo).get();
    }

    public static LocalDate max(LocalDate... values) {
        return Arrays.stream(values).max(LocalDate::compareTo).get();
    }

    public static String getCurrentYear() {
        return String.valueOf(LocalDate.now().getYear());
    }

    /**
     * Return miliseconds from UTC timezone, 1970
     *
     * @return
     */
    public static long toMilliseconds(LocalDate date) {
        return date.atStartOfDay(ZoneId.of("UTC")).toEpochSecond() * 1000;
    }

    public static LocalDateTime toLocalDateTime(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        TimeZone tz = calendar.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        return LocalDateTime.ofInstant(calendar.toInstant(), zid);
    }
}