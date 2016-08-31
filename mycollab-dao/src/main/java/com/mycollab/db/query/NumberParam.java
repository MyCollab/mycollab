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
package com.mycollab.db.query;

import com.mycollab.common.i18n.QueryI18nEnum.NumberI18nEnum;
import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.NoValueSearchField;
import com.mycollab.db.arguments.OneValueSearchField;
import com.mycollab.db.arguments.SearchField;

import static com.mycollab.common.i18n.QueryI18nEnum.NumberI18nEnum.*;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class NumberParam extends ColumnParam {

    public static final NumberI18nEnum[] OPTIONS = {EQUAL, NOT_EQUAL, LESS_THAN,
            LESS_THAN_EQUAL, GREATER_THAN, GREATER_THAN_EQUAL,
            IS_EMPTY, IS_NOT_EMPTY};

    private static final String EQUAL_EXPR = "%s.%s = ";
    private static final String NOT_EQUAL_EXPR = "%s.%s <> ";
    private static final String NULL_EXPR = "%s.%s is null";
    private static final String NOT_NULL_EXPR = "%s.%s is not null";
    private static final String GREATER_THAN_EXPR = "%s.%s > ";
    private static final String GREATER_THAN_EQUAL_EXPR = "%s.%s >= ";
    private static final String LESS_THAN_EXPR = "%s.%s < ";
    private static final String LESS_THAN_EQUAL_EXPR = "%s.%s <= ";

    public NumberParam(String id, String table, String column) {
        super(id, table, column);
    }

    public SearchField buildSearchField(String prefixOper, String compareOper, Number value) {
        NumberI18nEnum compareValue = valueOf(compareOper);
        switch (compareValue) {
            case EQUAL:
                return this.buildParamIsEqual(prefixOper, value);
            case NOT_EQUAL:
                return this.buildParamIsNotEqual(prefixOper, value);
            case IS_EMPTY:
                return this.buildParamIsNull(prefixOper);
            case IS_NOT_EMPTY:
                return this.buildParamIsNotNull(prefixOper);
            case GREATER_THAN:
                return this.buildParamIsGreaterThan(prefixOper, value);
            case GREATER_THAN_EQUAL:
                return this.buildParamIsGreaterThanEqual(prefixOper, value);
            case LESS_THAN:
                return this.buildParamIsLessThan(prefixOper, value);
            case LESS_THAN_EQUAL:
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
