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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.ocpsoft.prettytime.PrettyTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.MyCollabException;

/**
 * Utility class to process date instance
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DateTimeUtils {
	private static final Logger LOG = LoggerFactory.getLogger(DateTimeUtils.class);

	private static DateTimeZone utcZone = DateTimeZone.UTC;

	private static Map<String, SimpleDateFormat> dateFormats = new HashMap<String, SimpleDateFormat>();

	/**
	 * Trim hour-minute-second of date instance value to zero.
	 * 
	 * @param value
	 * @return
	 */
	public static Date trimHMSOfDate(Date value) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return df.parse(df.format(value.getTime()));
		} catch (ParseException e) {
			throw new MyCollabException(e);
		}
	}

	public static Date getCurrentDateWithoutMS() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		return calendar.getTime();
	}

	public static Date convertDateByString(String strDate, String format) {
		if (!StringUtils.isEmpty(strDate)) {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat(format);
				return formatter.parse(strDate);
			} catch (ParseException e) {
				LOG.error("Error while parse date", e);
			}
		}
		return new Date();
	}

	public static String converToStringWithUserTimeZone(String dateVal,
			String dateFormat, TimeZone userTimeZone) {
		Date date = convertDateByFormatW3C(dateVal);
		return converToStringWithUserTimeZone(date, dateFormat, userTimeZone);
	}

	/**
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date convertDateByFormatW3C(String strDate) {
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

	public static String converToStringWithUserTimeZone(Date date,
			String dateFormat, TimeZone userTimeZone) {
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

	/**
	 * 
	 * @param date
	 * @param duration
	 *            Example: Date date = subtractOrAddDayDuration(new Date(), -2);
	 *            // Result: the last 2 days
	 * 
	 *            Date date = subtractOrAddDayDuration(new Date(), 2); //
	 *            Result: the next 2 days
	 * @return
	 */
	public static Date subtractOrAddDayDuration(Date date, int duration) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, duration);
		Date dateExpect = cal.getTime();
		return dateExpect;
	}

	public static String formatDate(Date date, String dateFormat) {
		return formatDate(date, dateFormat, null);
	}

	public static String formatDate(Date date, String dateFormat,
			TimeZone timezone) {
		if (date == null) {
			return "";
		}

		SimpleDateFormat simpleDateFormat = getDateFormat(dateFormat);
		if (timezone != null) {
			simpleDateFormat.setTimeZone(timezone);
		}

		return simpleDateFormat.format(date);
	}

	private static SimpleDateFormat getDateFormat(String format) {
		SimpleDateFormat dateFormat = dateFormats.get(format);
		if (dateFormat != null) {
			return dateFormat;
		} else {
			dateFormat = new SimpleDateFormat(format);
			dateFormats.put(format, dateFormat);
			return dateFormat;
		}
	}

	public static Date convertTimeFromSystemTimezoneToUTC(long timeInMillis) {
		DateTime dt = new DateTime();
		dt = dt.withMillis(-DateTimeZone.getDefault().getOffset(timeInMillis)
				+ timeInMillis);
		dt = dt.withZone(utcZone);
		Date date = dt.toDate();
		return date;
	}

	/**
	 * Convert from UTC time to default time zone of system
	 * 
	 * @param timeInMillis
	 * @return
	 */
	public static Date convertTimeFromUTCToSystemTimezone(long timeInMillis) {
		DateTime dt = new DateTime();
		dt = dt.withMillis(DateTimeZone.getDefault().getOffset(timeInMillis)
				+ timeInMillis);
		dt = dt.withZone(utcZone);
		Date date = dt.toDate();
		return date;
	}

	/**
	 * 
	 * @param date
	 * @return array of two date elements, first is the first day of week, and
	 *         the second is the end week date
	 */
	public static Date[] getBounceDateofWeek(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date begin = calendar.getTime();

		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		Date end = calendar.getTime();
		return new Date[] { begin, end };
	}

	public static int compareByDate(Date date1, Date date2) {
		Date newDate1 = trimHMSOfDate(date1);
		Date newDate2 = trimHMSOfDate(date2);
		return newDate1.compareTo(newDate2);
	}

}
