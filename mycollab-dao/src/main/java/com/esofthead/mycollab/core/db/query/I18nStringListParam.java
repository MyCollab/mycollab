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

import com.esofthead.mycollab.core.arguments.CollectionValueSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;

import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.3.0
 */
public class I18nStringListParam extends ColumnParam {
    static final String IN = "in";
    static final String NOT_IN = "not in";

    public static String[] OPTIONS = {IN, NOT_IN};

    private List<? extends Enum<?>> lstValues;

    public I18nStringListParam(String id, Enum<?> displayName, String table, String column, List<? extends Enum<?>> values) {
        super(id, displayName, table, column);
        this.lstValues = values;
    }

    public List<? extends Enum<?>> getLstValues() {
        return lstValues;
    }

    public void setLstValues(List<? extends Enum<?>> lstValues) {
        this.lstValues = lstValues;
    }

    public CollectionValueSearchField buildStringParamInList(String oper, Collection<?> value) {
        String IN_EXPR = "%s.%s in ";
        return new CollectionValueSearchField(oper, String.format(IN_EXPR, this.getTable(), this.getColumn()), value);
    }

    public CollectionValueSearchField andStringParamInList(Collection<?> value) {
        return buildStringParamInList(SearchField.AND, value);
    }

    public CollectionValueSearchField orStringParamInList(Collection<?> value) {
        return buildStringParamInList(SearchField.OR, value);
    }

    public CollectionValueSearchField buildStringParamNotInList(String oper, Collection<?> value) {
        String NOT_IN_EXPR = "%s.%s not in ";
        return new CollectionValueSearchField(oper, String.format(NOT_IN_EXPR,
                this.getTable(), this.getColumn()), value);
    }

    public CollectionValueSearchField andStringParamNotInList(List<?> value) {
        return buildStringParamNotInList(SearchField.AND, value);
    }

    public CollectionValueSearchField orStringParamNotInList(List<?> value) {
        return buildStringParamNotInList(SearchField.OR, value);
    }
}
