/**
 * This file is part of mycollab-dao.
 *
 * mycollab-dao is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-dao is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-dao.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.core.db.query;

import org.joda.time.LocalDate;

import java.util.Date;

/**
 * @author MyCollab Ltd
 * @since 5.2.1
 */
public interface VariableInjector<T> {
    VariableInjector LAST_WEEK = new VariableInjector() {
        @Override
        public Object eval() {
            LocalDate date = new LocalDate(new Date());
            date = date.minusWeeks(-1);
            LocalDate minDate = date.dayOfWeek().withMinimumValue();
            LocalDate maxDate = date.dayOfWeek().withMaximumValue();
            return new Date[]{minDate.toDate(), maxDate.toDate()};
        }
    };

    VariableInjector THIS_WEEK = new VariableInjector() {
        @Override
        public Object eval() {
            LocalDate date = new LocalDate(new Date());
            LocalDate minDate = date.dayOfWeek().withMinimumValue();
            LocalDate maxDate = date.dayOfWeek().withMaximumValue();
            return new Date[]{minDate.toDate(), maxDate.toDate()};
        }
    };

    VariableInjector THIS_MONTH = new VariableInjector() {
        @Override
        public Object eval() {
            LocalDate date = new LocalDate(new Date());
            LocalDate minDate = date.dayOfMonth().withMinimumValue();
            LocalDate maxDate = date.dayOfMonth().withMaximumValue();
            return new Date[]{minDate.toDate(), maxDate.toDate()};
        }
    };


    T eval();
}
