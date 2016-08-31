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

import com.mycollab.common.i18n.QueryI18nEnum.StringI18nEnum;
import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.NoValueSearchField;
import com.mycollab.db.arguments.OneValueSearchField;
import com.mycollab.db.arguments.SearchField;

import static com.mycollab.common.i18n.QueryI18nEnum.StringI18nEnum.*;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class StringParam extends ColumnParam {

    public static StringI18nEnum[] OPTIONS = {IS, IS_NOT, CONTAINS, NOT_CONTAINS, IS_EMPTY, IS_NOT_EMPTY};

    public StringParam(String id, String table, String column) {
        super(id, table, column);
    }

    public SearchField buildSearchField(String prefixOper, String compareOper, String value) {
        StringI18nEnum compareVal = valueOf(compareOper);
        switch (compareVal) {
            case IS_EMPTY:
                return this.buildStringParamIsNull(prefixOper);
            case IS_NOT_EMPTY:
                return this.buildStringParamIsNotNull(prefixOper);
            case IS:
                return this.buildStringParamIsEqual(prefixOper, value);
            case IS_NOT:
                return this.buildStringParamIsNotEqual(prefixOper, value);
            case CONTAINS:
                return this.buildStringParamIsLike(prefixOper, value);
            case NOT_CONTAINS:
                return this.buildStringParamIsNotLike(prefixOper, value);
            default:
                throw new MyCollabException("Not support yet");
        }
    }

    public NoValueSearchField buildStringParamIsNull(String oper) {
        String NULL_EXPR = "%s.%s is null";
        return new NoValueSearchField(oper, String.format(NULL_EXPR,
                this.getTable(), this.getColumn()));
    }

    public NoValueSearchField andStringParamIsNull() {
        return buildStringParamIsNull(SearchField.AND);
    }

    public NoValueSearchField orStringParamIsNull() {
        return buildStringParamIsNull(SearchField.OR);
    }

    public NoValueSearchField buildStringParamIsNotNull(String oper) {
        String NOT_NULL_EXPR = "%s.%s is not null";
        return new NoValueSearchField(oper, String.format(NOT_NULL_EXPR, this.getTable(), this.getColumn()));
    }

    public NoValueSearchField andStringParamIsNotNull() {
        return buildStringParamIsNotNull(SearchField.AND);
    }

    public NoValueSearchField orStringParamIsNotNull() {
        return buildStringParamIsNull(SearchField.OR);
    }

    public OneValueSearchField buildStringParamIsEqual(String oper, Object value) {
        String EQUAL_EXPR = "%s.%s = ";
        return new OneValueSearchField(oper, String.format(EQUAL_EXPR, this.getTable(), this.getColumn()), value);
    }

    public OneValueSearchField andStringParamIsEqual(Object value) {
        return buildStringParamIsEqual(SearchField.AND, value);
    }

    public OneValueSearchField orStringParamIsEqual(Object value) {
        return buildStringParamIsEqual(SearchField.OR, value);
    }

    public OneValueSearchField buildStringParamIsNotEqual(String oper, Object value) {
        String NOT_EQUAL_EXPR = "%s.%s <> ";
        return new OneValueSearchField(oper, String.format(NOT_EQUAL_EXPR, this.getTable(), this.getColumn()), value);
    }

    public OneValueSearchField andStringParamIsNotEqual(Object value) {
        return buildStringParamIsNotEqual(SearchField.AND, value);
    }

    public OneValueSearchField orStringParamIsNotEqual(StringParam param,
                                                       Object value) {
        return buildStringParamIsNotEqual(SearchField.OR, value);
    }

    public OneValueSearchField buildStringParamIsLike(String oper, Object value) {
        String LIKE_EXPR = "%s.%s like ";
        return new OneValueSearchField(oper, String.format(LIKE_EXPR, this.getTable(), this.getColumn()), "%" + value + "%");
    }

    public OneValueSearchField andStringParamIsLike(Object value) {
        return buildStringParamIsLike(SearchField.AND, value);
    }

    public OneValueSearchField orStringParamIsLike(Object value) {
        return buildStringParamIsLike(SearchField.OR, value);
    }

    public OneValueSearchField buildStringParamIsNotLike(String oper, Object value) {
        String NOT_LIKE_EXPR = "%s.%s not like ";
        return new OneValueSearchField(oper, String.format(NOT_LIKE_EXPR,
                this.getTable(), this.getColumn()), "%" + value + "%");
    }

    public OneValueSearchField andStringParamIsNotLike(Object value) {
        return buildStringParamIsNotLike(SearchField.AND, value);
    }

    public OneValueSearchField orStringParamIsNotLike(Object value) {
        return buildStringParamIsNotLike(SearchField.OR, value);
    }
}
