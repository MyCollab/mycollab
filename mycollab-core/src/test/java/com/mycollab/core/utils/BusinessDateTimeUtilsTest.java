/**
 * Copyright © MyCollab
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

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public class BusinessDateTimeUtilsTest {
    @Test
    public void testPlusDaysWithPositiveValues() {
        // Friday
        LocalDate date = LocalDate.of(2015, 8, 28);

        // Monday
        LocalDate result = BusinessDayTimeUtils.plusDays(date, 1);
        assertEquals(0, result.compareTo(LocalDate.of(2015, 8, 28)));

        //Next Friday
        result = BusinessDayTimeUtils.plusDays(date, 5);
        assertEquals(0, result.compareTo(LocalDate.of(2015, 9, 3)));

        // Tuesday
        date = LocalDate.of(2015, 8, 25);
        result = BusinessDayTimeUtils.plusDays(date, 8);
        assertEquals(0, result.compareTo(LocalDate.of(2015, 9, 3)));

        //the same day
        result = BusinessDayTimeUtils.plusDays(date, 0);
        assertEquals(0, result.compareTo(LocalDate.of(2015, 8, 24)));

        date = LocalDate.of(2015, 8, 28);
        result = BusinessDayTimeUtils.plusDays(date, 23);
        assertEquals(0, result.compareTo(LocalDate.of(2015, 9, 29)));
    }

    @Test
    public void testPlusDaysWithNegativeValues() {
        // Friday
        LocalDate date = LocalDate.of(2015, 8, 28);

        LocalDate result = BusinessDayTimeUtils.plusDays(date, -1);
        assertEquals(0, result.compareTo(LocalDate.of(2015, 8, 27)));

        result = BusinessDayTimeUtils.plusDays(date, -5);
        assertEquals(0, result.compareTo(LocalDate.of(2015, 8, 21)));

        //Monday
        date = LocalDate.of(2015, 8, 24);
        result = BusinessDayTimeUtils.plusDays(date, -1);
        assertEquals(0, result.compareTo(LocalDate.of(2015, 8, 21)));
    }

    //    @Test
    private void testDuration() {
        // Friday
        LocalDate startDate = LocalDate.of(2015, 8, 28);
        LocalDate endDate = LocalDate.of(2015, 8, 28);
        int duration = BusinessDayTimeUtils.duration(startDate, endDate);
        assertEquals(1, duration);
        assertTrue(endDate.isEqual(BusinessDayTimeUtils.plusDays(startDate, duration)));

        startDate = LocalDate.of(2015, 8, 28);
        endDate = LocalDate.of(2015, 8, 31);
        duration = BusinessDayTimeUtils.duration(startDate, endDate);
        endDate = BusinessDayTimeUtils.plusDays(startDate, 1);
        System.out.println("Duration " + duration + " " + endDate);
        assertTrue(endDate.isEqual(BusinessDayTimeUtils.plusDays(startDate, duration)));
        assertEquals(1, duration);

        startDate = LocalDate.of(2015, 8, 28);
        endDate = LocalDate.of(2015, 9, 30);
        duration = BusinessDayTimeUtils.duration(startDate, endDate);
        assertEquals(24, BusinessDayTimeUtils.duration(startDate, endDate));
        assertTrue(endDate.isEqual(BusinessDayTimeUtils.plusDays(startDate, duration)));

        startDate = LocalDate.of(2015, 8, 24);
        endDate = LocalDate.of(2015, 8, 31);
        duration = BusinessDayTimeUtils.duration(startDate, endDate);
        assertEquals(6, duration);
        assertTrue(endDate.isEqual(BusinessDayTimeUtils.plusDays(startDate, duration)));

        startDate = LocalDate.of(2015, 6, 8);
        endDate = LocalDate.of(2015, 6, 26);
        duration = BusinessDayTimeUtils.duration(startDate, endDate);
        assertEquals(15, BusinessDayTimeUtils.duration(startDate, endDate));
        assertTrue(endDate.isEqual(BusinessDayTimeUtils.plusDays(startDate, duration)));

        startDate = LocalDate.of(2015, 8, 17);
        endDate = LocalDate.of(2016, 12, 26);
        duration = BusinessDayTimeUtils.duration(startDate, endDate);
        assertEquals(356, duration);
        assertTrue(endDate.isEqual(BusinessDayTimeUtils.plusDays(startDate, duration)));

        startDate = LocalDate.of(2015, 9, 9);
        endDate = LocalDate.of(2015, 9, 16);
        duration = BusinessDayTimeUtils.duration(startDate, endDate);
        assertEquals(6, duration);
        assertTrue(endDate.isEqual(BusinessDayTimeUtils.plusDays(startDate, duration)));

        startDate = LocalDate.of(2015, 9, 5);
        endDate = LocalDate.of(2015, 9, 7);
        assertEquals(1, BusinessDayTimeUtils.duration(startDate, endDate));

        startDate = LocalDate.of(2015, 8, 27);
        endDate = LocalDate.of(2015, 8, 30);
        assertEquals(2, BusinessDayTimeUtils.duration(startDate, endDate));

        startDate = LocalDate.of(2013, 5, 3);
        endDate = LocalDate.of(2015, 9, 4);
        assertEquals(611, BusinessDayTimeUtils.duration(startDate, endDate));

        startDate = LocalDate.of(2015, 12, 21);
        endDate = LocalDate.of(2016, 2, 17);
        assertEquals(42, BusinessDayTimeUtils.duration(startDate, endDate));
    }
}
