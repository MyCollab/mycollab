package com.mycollab.core.utils;

import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateTimeUtilsTest {

    @Test
    public void testDateWithZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        DateTimeZone.setDefault(DateTimeZone.UTC);

        Date value = new GregorianCalendar(2012, 11, 20).getTime();
        Date newDate = DateTimeUtils.convertDateTimeToUTC(value);
        Assert.assertEquals(value.getTime(), newDate.getTime());

        Date newDate2 = DateTimeUtils.trimHMSOfDate(newDate);
        Assert.assertEquals(value.getTime(), newDate2.getTime());

        Date newDate3 = DateTimeUtils.trimHMSOfDate(DateTimeUtils.convertDateTimeToUTC(value));
        Assert.assertEquals(value.getTime(), newDate3.getTime());
    }

    @Test
    public void testConvertCurrentTimezoneToUTC() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        DateTimeZone.setDefault(DateTimeZone.UTC);

        GregorianCalendar date = new GregorianCalendar(2014, 0, 30, 23, 0, 0);
        Date dateUTC = DateTimeUtils.convertDateTimeToUTC(date.getTime());

        Date currentTime = DateTimeUtils.convertTimeFromUTCToSystemTimezone(dateUTC.getTime());
        GregorianCalendar returnDate = new GregorianCalendar();
        returnDate.setTime(currentTime);
        Assert.assertEquals(returnDate.getTimeInMillis(), date.getTimeInMillis());
    }
}
