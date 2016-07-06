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

import com.mycollab.db.arguments.CollectionValueSearchField;

import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class StringListParam extends ColumnParam {
    public static final String IN = "in";
    public static final String NOT_IN = "not in";

    public static String[] OPTIONS = {IN, NOT_IN};

    private List<String> values;

    public StringListParam(String id, String table, String column, List<String> values) {
        super(id, table, column);
        this.values = values;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public CollectionValueSearchField buildStringParamInList(String oper, Collection<?> values) {
        String IN_EXPR = "%s.%s in ";
        return new CollectionValueSearchField(oper, String.format(IN_EXPR, this.getTable(), this.getColumn()), values);
    }

    public CollectionValueSearchField buildStringParamNotInList(String oper, Collection<?> values) {
        String NOT_IN_EXPR = "%s.%s not in ";
        return new CollectionValueSearchField(oper, String.format(NOT_IN_EXPR, this.getTable(), this.getColumn()), values);
    }
}