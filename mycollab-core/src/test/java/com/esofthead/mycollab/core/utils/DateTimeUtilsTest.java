package com.esofthead.mycollab.core.utils;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

public class DateTimeUtilsTest {

	@Test
	public void testDateWithZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		Date value = new GregorianCalendar(2012, 11, 20).getTime();
		Date newDate = DateTimeUtils.convertTimeFromSystemTimezoneToUTC(value
				.getTime());
		Assert.assertEquals(value.getTime(), newDate.getTime());

		Date newDate2 = DateTimeUtils.trimHMSOfDate(newDate);
		Assert.assertEquals(value.getTime(), newDate2.getTime());

		Date newDate3 = DateTimeUtils.trimHMSOfDate(DateTimeUtils
				.convertTimeFromSystemTimezoneToUTC(value.getTime()));
		Assert.assertEquals(value.getTime(), newDate3.getTime());
	}
}
