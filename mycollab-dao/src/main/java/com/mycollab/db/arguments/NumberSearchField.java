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
package com.mycollab.db.arguments;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class NumberSearchField extends SearchField {
    private static final long serialVersionUID = 1L;

    public static final String EQUAL = "=";
    public static final String NOTEQUAL = "<>";
    public static final String LESSTHAN = "<";
    public static final String GREATER = ">";

    private Number value;
    private String compareOperator;

    public NumberSearchField() {
        this(0);
    }

    public NumberSearchField(Number value) {
        this(SearchField.AND, value, EQUAL);
    }

    public NumberSearchField(String oper, Number value) {
        this(oper, value, EQUAL);
    }

    public NumberSearchField(Number value, String compareOperator) {
        this(SearchField.AND, value, compareOperator);
    }

    public NumberSearchField(String oper, Number value, String compareOperator) {
        this.operation = oper;
        this.value = value;
        this.compareOperator = compareOperator;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public String getCompareOperator() {
        return compareOperator;
    }

    public void setCompareOperator(String compareOperator) {
        this.compareOperator = compareOperator;
    }

    public static NumberSearchField and(Number value) {
        return new NumberSearchField(AND, value, EQUAL);
    }

}
