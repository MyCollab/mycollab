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
package com.esofthead.mycollab.core.arguments;

import org.joda.time.LocalDate;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DateSearchField extends SearchField {
    private static final long serialVersionUID = 1L;

    public static final String LESSTHAN = "<";
    public static final String LESSTHANEQUAL = "<=";
    public static final String GREATERTHAN = ">";
    public static final String GREATERTHANEQUAL = ">=";
    public static final String EQUAL = "=";
    public static final String NOTEQUAL = "<>";

    private Date value;
    private String comparison;

    public DateSearchField(Date value) {
        this(SearchField.AND, value, LESSTHAN);
    }

    public DateSearchField(Date value, String comparison) {
        this(AND, value, comparison);
    }

    public DateSearchField(String oper, Date dateVal, String comparison) {
        this.operation = oper;
        this.comparison = comparison;
        this.value = new LocalDate(dateVal).toDate();
    }

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    public String getComparison() {
        return comparison;
    }

    public void setComparison(String comparison) {
        this.comparison = comparison;
    }
}
