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

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.BetweenValuesSearchField;
import com.esofthead.mycollab.core.arguments.OneValueSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;

import java.lang.reflect.Array;
import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class DateParam extends ColumnParam {
    public static final String IS = "is";
    public static final String IS_NOT = "isn't";
    public static final String BEFORE = "is before";
    public static final String AFTER = "is after";
    public static final String BETWEEN = "between";
    public static final String NOT_BETWEEN = "not between";

    public static String[] OPTIONS = {IS, IS_NOT, BEFORE, AFTER, BETWEEN, NOT_BETWEEN};

    public DateParam(String id, Enum displayName, String table, String column) {
        super(id, displayName, table, column);
    }

    public static SearchField inRangeDate(DateParam dateParam, VariableInjecter variableInjecter) {
        Object value = variableInjecter.eval();
        if (value != null) {
            if (value.getClass().isArray()) {
                return dateParam.buildSearchField(SearchField.AND, BETWEEN, (Date) Array.get(value, 0), (Date) Array.get(value, 1));
            } else {
                return dateParam.buildSearchField(SearchField.AND, BETWEEN, (Date) value);
            }
        } else {
            return null;
        }
    }

    public SearchField buildSearchField(String prefixOper, String compareOper, Date dateValue1, Date dateValue2) {
        switch (compareOper) {
            case DateParam.BETWEEN:
                return buildDateValBetween(prefixOper, dateValue1, dateValue2);
            case DateParam.NOT_BETWEEN:
                return buildDateValNotBetween(prefixOper, dateValue1, dateValue2);
            default:
                throw new MyCollabException("Not support yet");
        }
    }

    public SearchField buildSearchField(String prefixOper, String compareOper, Date dateValue) {
        switch (compareOper) {
            case DateParam.IS:
                return buildDateIsEqual(prefixOper, dateValue);
            case DateParam.IS_NOT:
                return buildDateIsNotEqual(prefixOper, dateValue);
            case DateParam.BEFORE:
                return buildDateIsLessThan(prefixOper, dateValue);
            case DateParam.AFTER:
                return buildDateIsGreaterThan(prefixOper, dateValue);
            default:
                throw new MyCollabException("Not support yet");
        }
    }

    private BetweenValuesSearchField buildDateValBetween(String oper, Date value1, Date value2) {
        return new BetweenValuesSearchField(oper, String.format("DATE(%s.%s) BETWEEN", table, column), value1, value2);
    }

    private BetweenValuesSearchField buildDateValNotBetween(String oper, Date value1, Date value2) {
        return new BetweenValuesSearchField(oper, String.format("DATE(%s.%s) NOT BETWEEN", table, column), value1, value2);
    }

    private OneValueSearchField buildDateIsEqual(String oper, Date value) {
        return new OneValueSearchField(oper, String.format("DATE(%s.%s) = ", table, column), value);
    }

    private OneValueSearchField buildDateIsNotEqual(String oper, Date value) {
        return new OneValueSearchField(oper, String.format("DATE(%s.%s) <> ", table, column), value);
    }

    private OneValueSearchField buildDateIsGreaterThan(String oper, Date value) {
        return new OneValueSearchField(oper, String.format("DATE(%s.%s) >= ", table, column), value);
    }

    private OneValueSearchField buildDateIsLessThan(String oper, Date value) {
        return new OneValueSearchField(oper, String.format("DATE(%s.%s) <= ", table, column), value);
    }
}
