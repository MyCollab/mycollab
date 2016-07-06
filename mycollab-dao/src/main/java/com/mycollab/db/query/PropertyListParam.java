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

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class PropertyListParam<P> extends ColumnParam {
    public static final String BELONG_TO = "belong to";
    public static final String NOT_BELONG_TO = "not belong to";

    public static final String[] OPTIONS = {BELONG_TO, NOT_BELONG_TO};

    public PropertyListParam(String id, String table, String column) {
        super(id, table, column);
    }

    public CollectionValueSearchField buildPropertyParamInList(String oper, Collection<P> value) {
        String IN_EXPR = "%s.%s in ";
        return new CollectionValueSearchField(oper, String.format(IN_EXPR, this.getTable(), this.getColumn()), value);
    }

    public CollectionValueSearchField buildPropertyParamNotInList(String oper, Collection<P> value) {
        String NOT_IN_EXPR = "%s.%s not in ";
        return new CollectionValueSearchField(oper, String.format(NOT_IN_EXPR, this.getTable(), this.getColumn()), value);
    }
}
