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

import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.OneValueSearchField;
import com.mycollab.db.arguments.SearchField;

import static com.mycollab.common.i18n.QueryI18nEnum.StringI18nEnum;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class PropertyParam extends ColumnParam {

    public static StringI18nEnum[] OPTIONS = {StringI18nEnum.IS, StringI18nEnum.IS_NOT};

    private static final String IS_EXPR = "%s.%s = ";
    private static final String IS_NOT_EXPR = "%s.%s <> ";

    public PropertyParam(String id, String table, String column) {
        super(id, table, column);
    }

    public SearchField buildSearchField(String prefixOper, String compareOper, Object value) {
        StringI18nEnum compareValue = StringI18nEnum.valueOf(compareOper);
        switch (compareValue) {
            case IS:
                return buildPropertyIs(prefixOper, value);
            case IS_NOT:
                return buildPropertyIsNot(prefixOper, value);
            default:
                throw new MyCollabException("Not support");
        }
    }

    public OneValueSearchField buildPropertyIs(String oper, Object value) {
        return new OneValueSearchField(oper, String.format(IS_EXPR, table, column), value);
    }

    public OneValueSearchField buildPropertyIsNot(String oper, Object value) {
        return new OneValueSearchField(oper, String.format(IS_NOT_EXPR, table, column), value);
    }
}
