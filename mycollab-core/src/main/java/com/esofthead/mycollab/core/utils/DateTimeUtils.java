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
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
	private static Logger log = LoggerFactory.getLogger(DateTimeUtils.class);

	private static DateTimeZone utcZone = DateTimeZone.UTC;

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"MM/dd/yyyy");

	private static SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(
			"MM/dd/yyyy hh:mm aa");

	/**
	 * Trim hour-minute-second of date instance value to zero.
	 * 
	 * @param value
	 * @return
	 */
	public static Date trimHMSOfDate(Date value) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = df.parse(df.format(value.getTime()));
		} catch (ParseException e) {
			throw new MyCollabException(e);
		}
		return date;
	}

	public static Date convertDateByString(String strDate, String format) {
		if (strDate != null && !strDate.equals("")) {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat(format);
				return formatter.parse(strDate);
			} catch (ParseException e) {
				log.error("Error while parse date", e);
			}
		}
		return new Date();
	}

	public static Date convertDateByString(String strDate,
			SimpleDateFormat formatter) {
		if (strDate != null && !strDate.equals("")) {
			try {
				return formatter.parse(strDate);
			} catch (ParseException e) {
				log.error("Error while parse date", e);
			}
		}
		return new Date();
	}

	public static String converToStringWithUserTimeZone(String dateVal,
			String userTimeZone) {
		Date date = convertDateByFormatW3C(dateVal);
		return converToStringWithUserTimeZone(date, userTimeZone);
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
				log.error("Error while parse date", e);
			}
		}
		return null;
	}

	public static String converToStringWithUserTimeZone(Date date,
			String userTimeZone) {
		if (date == null)
			return "";
		return DateTimeUtils.formatDate(date,
				TimezoneMapper.getTimezone(userTimeZone).getTimezone());
	}

	public static Date converToDateWithUserTimeZone(Date date,
			String userTimeZone) {
		if (date == null || userTimeZone == null) {
			return null;
		}

		simpleDateFormat.setTimeZone(TimezoneMapper.getTimezone(userTimeZone)
				.getTimezone());
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		try {
			return df.parse(simpleDateFormat.format(date));
		} catch (ParseException e) {
			log.error("ConverToDateWithUserTimeZone Error while parse date", e);
			return null;
		}
	}

	public static String getStringDateFromNow(Date dateTime) {
		if (dateTime == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Date current = Calendar.getInstance().getTime();
		long diffInSeconds = (current.getTime() - dateTime.getTime()) / 1000;

		long sec = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
		long min = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60
				: diffInSeconds;
		long hrs = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24
				: diffInSeconds;
		long days = (diffInSeconds = (diffInSeconds / 24)) >= 30 ? diffInSeconds % 30
				: diffInSeconds;
		long months = (diffInSeconds = (diffInSeconds / 30)) >= 12 ? diffInSeconds % 12
				: diffInSeconds;
		long years = (diffInSeconds = (diffInSeconds / 12));

		if (years > 0) {
			sb.append((years == 1) ? "a year" : (years + " years"));
			if (years <= 6 && months > 0) {
				sb.append(" and ");
				sb.append((months == 1) ? "a month" : (months + " months"));
			}
		} else if (months > 0) {
			sb.append((months == 1) ? "a month" : (months + " months"));
			if (months <= 6 && days > 0) {
				sb.append(" and ");
				sb.append((days == 1) ? "a day" : (days + " days"));
			}
		} else if (days > 0) {
			sb.append((days == 1) ? "a day" : (days + " days"));
			if (days <= 3 && hrs > 0) {
				sb.append(" and ");
				sb.append((hrs == 1) ? "an hour" : (hrs + " hours"));
			}
		} else if (hrs > 0) {
			sb.append((hrs == 1) ? "an hour" : (hrs + " hours"));
			if (min > 1) {
				sb.append(" and " + min + " minutes");
			}
		} else if (min > 0) {
			sb.append((min == 1) ? "a minute" : (min + " minutes"));
			if (sec > 1) {
				sb.append(" and " + sec + " seconds");
			}
		} else {
			sb.append((sec == 1) ? "about a second"
					: ("about " + sec + " seconds"));
		}

		sb.append(" ago");

		return sb.toString();
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

	public static String formatDate(Date date) {
		return formatDate(date, null);
	}

	public static String formatDate(Date date, TimeZone timezone) {
		if (date == null) {
			return "";
		}

		if (timezone != null) {
			simpleDateFormat.setTimeZone(timezone);
		}

		return simpleDateFormat.format(date);
	}

	public static String formatDateTime(Date date, TimeZone timezone) {
		if (date == null) {
			return "";
		}

		if (timezone != null) {
			simpleDateTimeFormat.setTimeZone(timezone);
		}
		return simpleDateTimeFormat.format(date);
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
		Date begin = null, end = null;
		Calendar calendar = new GregorianCalendar();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		begin = calendar.getTime();

		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		end = calendar.getTime();
		return new Date[] { begin, end };
	}

}
