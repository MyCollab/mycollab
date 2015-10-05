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

import net.objectlab.kit.datecalc.common.DateCalculator;
import net.objectlab.kit.datecalc.joda.LocalDateKitCalculatorsFactory;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public class BusinessDayTimeUtils {
    private static Logger LOG = LoggerFactory.getLogger(BusinessDayTimeUtils.class);

    public static LocalDate plusDays(LocalDate refDate, int lagDate) {
        DateCalculator<LocalDate> calc1;
        boolean isFoward = false;
        if (lagDate >= 0) {
            calc1 = LocalDateKitCalculatorsFactory.forwardCalculator("MyCollab");
            isFoward = true;
        } else {
            calc1 = LocalDateKitCalculatorsFactory.backwardCalculator("MyCollab");
        }

        if (isFoward) {
            refDate = refDate.minusDays(1);
            while (calc1.isNonWorkingDay(refDate)) {
                refDate = refDate.minusDays(1);
            }
        } else {
            refDate.plusDays(1);
            while (calc1.isNonWorkingDay(refDate)) {
                refDate = refDate.plusDays(1);
            }
        }

        calc1.setStartDate(refDate);

        calc1.moveByBusinessDays(lagDate);
        return calc1.getCurrentBusinessDate();
    }

    public static int duration(LocalDate start, LocalDate end) {
        int candidateDuration = 1;
        if (start.isAfter(end)) {
            return -1;
        }
        try {
            DateCalculator<LocalDate> calc1 = LocalDateKitCalculatorsFactory.forwardCalculator("MyCollab");
            calc1.setStartDate(start);
            start = calc1.getCurrentBusinessDate();
            calc1.setStartDate(end);
            if (calc1.isNonWorkingDay(end)) {
                candidateDuration -=1;
                end = calc1.getCurrentBusinessDate();
            }
            long possibleDurations = (end.toDate().getTime() - start.toDate().getTime()) / DateTimeUtils.MILISECONDS_IN_A_DAY;
            int varDays = Math.round((possibleDurations + 1) / 2);
            calc1.setStartDate(start);
            LocalDate testDate;
            while (true) {
                LocalDate previousBizDate = calc1.getCurrentBusinessDate();
                calc1.moveByBusinessDays(varDays);
                testDate = calc1.getCurrentBusinessDate();
                if (testDate.isAfter(end)) {
                    varDays = Math.round((varDays + 1) / 2);
                    calc1.setCurrentBusinessDate(previousBizDate);
                } else if (testDate.isBefore(end)) {
                    candidateDuration += varDays;
                    varDays = Math.round(varDays / 2);
                    calc1.setStartDate(testDate);
                } else {
                    return candidateDuration + varDays;
                }

                if (varDays == 0) {
                    calc1.setStartDate(testDate);
                    calc1.moveByBusinessDays(1);
                    testDate = calc1.getCurrentBusinessDate();
                    if (!testDate.isEqual(end)) {
                        LOG.error("Error while calculate duration of " + start + "--" + end);
                    }
                    return candidateDuration + 1;
                }
            }
        } catch (Exception e) {
            LOG.error("Error while calculate duration of " + start + "--" + end);
            return candidateDuration;
        }
    }
}
