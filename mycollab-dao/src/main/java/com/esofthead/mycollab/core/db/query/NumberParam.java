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
import com.esofthead.mycollab.core.arguments.NoValueSearchField;
import com.esofthead.mycollab.core.arguments.OneValueSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class NumberParam extends ColumnParam {
    static final String EQUAL = "=";
    static final String NOT_EQUAL = "<>";
    static final String LESS_THAN = "<";
    static final String LESS_THAN_EQUAL = "<=";
    static final String GREATER_THAN = ">";
    static final String GREATER_THAN_EQUAL = "=>";
    static final String IS_EMPTY = "is empty";
    static final String IS_NOT_EMPTY = "is not empty";

    public static final String[] OPTIONS = {EQUAL, NOT_EQUAL, LESS_THAN,
            LESS_THAN_EQUAL, GREATER_THAN, GREATER_THAN_EQUAL, IS_EMPTY, IS_NOT_EMPTY};

    private static final String EQUAL_EXPR = "%s.%s = ";
    private static final String NOT_EQUAL_EXPR = "%s.%s <> ";
    private static final String NULL_EXPR = "%s.%s is null";
    private static final String NOT_NULL_EXPR = "%s.%s is not null";
    private static final String GREATER_THAN_EXPR = "%s.%s > ";
    private static final String GREATER_THAN_EQUAL_EXPR = "%s.%s >= ";
    private static final String LESS_THAN_EXPR = "%s.%s < ";
    private static final String LESS_THAN_EQUAL_EXPR = "%s.%s <= ";

    public NumberParam(String id, Enum displayName, String table, String column) {
        super(id, displayName, table, column);
    }

    public SearchField buildSearchField(String prefixOper, String compareOper, Number value) {
        switch (compareOper) {
            case NumberParam.EQUAL:
                return this.buildParamIsEqual(prefixOper, value);
            case NumberParam.NOT_EQUAL:
                return this.buildParamIsNotEqual(prefixOper, value);
            case NumberParam.IS_EMPTY:
                return this.buildParamIsNull(prefixOper);
            case NumberParam.IS_NOT_EMPTY:
                return this.buildParamIsNotNull(prefixOper);
            case NumberParam.GREATER_THAN:
                return this.buildParamIsGreaterThan(prefixOper, value);
            case NumberParam.GREATER_THAN_EQUAL:
                return this.buildParamIsGreaterThanEqual(prefixOper, value);
            case NumberParam.LESS_THAN:
                return this.buildParamIsLessThan(prefixOper, value);
            case NumberParam.LESS_THAN_EQUAL:
                return this.buildParamIsLessThanEqual(prefixOper, value);
            default:
                throw new MyCollabException("Not support yet");
        }
    }

    public OneValueSearchField buildParamIsEqual(String oper, Object value) {
        return new OneValueSearchField(oper, String.format(EQUAL_EXPR, this.getTable(), this.getColumn()), value);
    }

    public OneValueSearchField buildParamIsNotEqual(String oper, Object value) {
        return new OneValueSearchField(oper, String.format(NOT_EQUAL_EXPR, this.getTable(), this.getColumn()), value);
    }

    public NoValueSearchField buildParamIsNull(String oper) {
        return new NoValueSearchField(oper, String.format(NULL_EXPR, this.getTable(), this.getColumn()));
    }

    public NoValueSearchField buildParamIsNotNull(String oper) {
        return new NoValueSearchField(oper, String.format(NOT_NULL_EXPR, this.getTable(), this.getColumn()));
    }

    public OneValueSearchField buildParamIsGreaterThan(String oper, Object value) {
        return new OneValueSearchField(oper, String.format(GREATER_THAN_EXPR,
                this.getTable(), this.getColumn()), value);
    }

    public OneValueSearchField buildParamIsGreaterThanEqual(String oper, Object value) {
        return new OneValueSearchField(oper, String.format(GREATER_THAN_EQUAL_EXPR, this.getTable(), this.getColumn()), value);
    }

    public OneValueSearchField buildParamIsLessThan(String oper, Object value) {
        return new OneValueSearchField(oper, String.format(LESS_THAN_EXPR, this.getTable(), this.getColumn()), value);
    }

    public OneValueSearchField buildParamIsLessThanEqual(String oper, Object value) {
        return new OneValueSearchField(oper, String.format(
                LESS_THAN_EQUAL_EXPR, this.getTable(), this.getColumn()), value);
    }
}
