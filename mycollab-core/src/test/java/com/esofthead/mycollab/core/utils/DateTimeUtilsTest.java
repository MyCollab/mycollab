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
        Date newDate = DateTimeUtils.convertTimeFromSystemTimezoneToUTC(value
                .getTime());
        Assert.assertEquals(value.getTime(), newDate.getTime());

        Date newDate2 = DateTimeUtils.trimHMSOfDate(newDate);
        Assert.assertEquals(value.getTime(), newDate2.getTime());

        Date newDate3 = DateTimeUtils.trimHMSOfDate(DateTimeUtils.convertTimeFromSystemTimezoneToUTC(value.getTime()));
        Assert.assertEquals(value.getTime(), newDate3.getTime());
    }

    @Test
    public void testConvertCurrentTimezoneToUTC() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        DateTimeZone.setDefault(DateTimeZone.UTC);

        GregorianCalendar date = new GregorianCalendar(2014, 0, 30, 23, 0, 0);
        Date dateUTC = DateTimeUtils.convertTimeFromSystemTimezoneToUTC(date.getTimeInMillis());

        Date currentTime = DateTimeUtils.convertTimeFromUTCToSystemTimezone(dateUTC.getTime());
        GregorianCalendar returnDate = new GregorianCalendar();
        returnDate.setTime(currentTime);
        Assert.assertEquals(returnDate.getTimeInMillis(), date.getTimeInMillis());
    }
}
