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
import com.esofthead.mycollab.core.arguments.OneValueSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class ConcatStringParam extends Param {

    public static final String CONTAINS = "contains";
    public static final String NOT_CONTAINS = "doesn't contains";

    public static String[] OPTIONS = {CONTAINS, NOT_CONTAINS};

    private String table;
    private String[] columns;

    public ConcatStringParam(String id, String table, String[] columns) {
        super(id);
        this.table = table;
        this.columns = columns;
    }

    public SearchField buildSearchField(String prefixOper, String compareOper, String value) {
        switch (compareOper) {
            case StringParam.CONTAINS:
                return this.buildStringParamIsLike(prefixOper, value);
            case StringParam.NOT_CONTAINS:
                return this.buildStringParamIsNotLike(prefixOper, value);
            default:
                throw new MyCollabException("Not support yet");
        }
    }

    public OneValueSearchField buildStringParamIsLike(String oper, Object value) {
        return new OneValueSearchField(oper, buildConcatField() + " like ", "%" + value + "%");
    }

    public OneValueSearchField buildStringParamIsNotLike(String oper, Object value) {
        return new OneValueSearchField(oper, buildConcatField() + " not like ", "%" + value + "%");
    }

    private String buildConcatField() {
        StringBuffer concatField = new StringBuffer("concat(");
        for (int i = 0; i < columns.length; i++) {
            if (i == 0) {
                concatField.append("IFNULL(").append(table).append(".").append(columns[0]).append(",''), ' ',");
            } else if (i < columns.length - 1) {
                concatField.append("IFNULL(").append(table).append(".").append(columns[i]).append(",''), ' '");
            } else {
                concatField.append("IFNULL(").append(table).append(".").append(columns[i]).append(",'')");
            }
        }
        concatField.append(")");
        return concatField.toString();
    }
}
