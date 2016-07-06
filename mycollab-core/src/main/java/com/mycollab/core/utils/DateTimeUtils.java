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
package com.mycollab.core.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.ocpsoft.prettytime.PrettyTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utility class to process date instance
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DateTimeUtils {
    private static final Logger LOG = LoggerFactory.getLogger(DateTimeUtils.class);

    private static DateTimeZone utcZone = DateTimeZone.UTC;

    public static final long MILLISECONDS_IN_A_DAY = 1000 * 60 * 60 * 24;

    /**
     * Trim hour-minute-second of date instance value to zero.
     *
     * @param value
     * @return
     */
    public static Date trimHMSOfDate(Date value) {
        return new LocalDate(value).toDate();
    }

    public static Date getCurrentDateWithoutMS() {
        return new LocalDate().toDate();
    }

    public static Date convertDateByString(String strDate, String format) {
        if (!StringUtils.isBlank(strDate)) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(format);
                return formatter.parse(strDate);
            } catch (ParseException e) {
                LOG.error("Error while parse date", e);
            }
        }
        return new Date();
    }

    public static String convertToStringWithUserTimeZone(String dateVal, String dateFormat, TimeZone userTimeZone) {
        Date date = parseDateByW3C(dateVal);
        return convertToStringWithUserTimeZone(date, dateFormat, userTimeZone);
    }

    /**
     * @param strDate
     * @return
     */
    public static Date parseDateByW3C(String strDate) {
        String formatW3C = "yyyy-MM-dd'T'HH:mm:ss";
        if (strDate != null && !strDate.equals("")) {
            SimpleDateFormat formatter = new SimpleDateFormat(formatW3C);
            try {
                return formatter.parse(strDate);
            } catch (ParseException e) {
                LOG.error("Error while parse date", e);
            }
        }
        return null;
    }

    public static String formatDateToW3C(Date date) {
        if (date != null) {
            String formatW3C = "yyyy-MM-dd'T'HH:mm:ss";
            SimpleDateFormat formatter = new SimpleDateFormat(formatW3C);
            return formatter.format(date);
        }
        return "";
    }

    public static String convertToStringWithUserTimeZone(Date date, String dateFormat, TimeZone userTimeZone) {
        if (date == null)
            return "";
        return formatDate(date, dateFormat, userTimeZone);
    }

    public static String getPrettyDateValue(Date dateTime, Locale locale) {
        if (dateTime == null) {
            return "";
        }
        PrettyTime p = new PrettyTime(locale);
        return p.format(dateTime);
    }

    public static String getPrettyDurationValue(Date date, Locale locale) {
        Period period = new Period(new LocalDate(date), LocalDate.now());
        PeriodFormatter formatter = PeriodFormat.wordBased(locale);
        return formatter.print(period);
    }

    /**
     * @param date
     * @param duration Example: Date date = subtractOrAddDayDuration(new Date(), -2);
     *                 // Result: the last 2 days
     *
     *                 Date date = subtractOrAddDayDuration(new Date(), 2); //
     *                 Result: the next 2 days
     * @return
     */
    public static Date subtractOrAddDayDuration(Date date, int duration) {
        DateTime localDate = new DateTime(date);
        return localDate.plusDays(duration).toDate();
    }

    public static String formatDate(Date date, String dateFormat) {
        return formatDate(date, dateFormat, null);
    }

    public static String formatDate(Date date, String dateFormat, TimeZone timezone) {
        if (date == null) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);
        if (timezone != null) {
            formatter = formatter.withZone(DateTimeZone.forTimeZone(timezone));
        }

        return formatter.print(new DateTime(date));
    }

    /**
     * @param date
     * @return
     */
    public static Date convertDateTimeToUTC(Date date) {
        return convertDateTimeByTimezone(date, DateTimeZone.UTC.toTimeZone());
    }

    public static Date convertDateTimeByTimezone(Date date, TimeZone timeZone) {
        DateTime dateTime = new DateTime(date);
        return dateTime.toDateTime(DateTimeZone.forTimeZone(timeZone)).toLocalDateTime().toDate();
    }

    /**
     * Convert from UTC time to default time zone of system
     *
     * @param timeInMillis
     * @return
     */
    public static Date convertTimeFromUTCToSystemTimezone(long timeInMillis) {
        DateTime dt = new DateTime();
        dt = dt.withMillis(DateTimeZone.getDefault().getOffset(timeInMillis) + timeInMillis);
        dt = dt.withZone(utcZone);
        return dt.toDate();
    }

    /**
     * @param date
     * @return array of two date elements, first is the first day of week, and
     * the second is the end week date
     */
    public static Date[] getBounceDatesOfWeek(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date begin = calendar.getTime();

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date end = calendar.getTime();
        return new Date[]{begin, end};
    }

    public static LocalDate min(LocalDate... values) {
        LocalDate minVal = values[0];
        for (int i = 1; i < values.length; i++) {
            if (minVal.isAfter(values[i])) {
                minVal = values[i];
            }
        }
        return minVal;
    }

    public static LocalDate max(LocalDate... values) {
        LocalDate max = values[0];
        for (int i = 1; i < values.length; i++) {
            if (max.isBefore(values[i])) {
                max = values[i];
            }
        }
        return max;
    }
}
