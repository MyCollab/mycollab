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

import com.mycollab.db.arguments.CompositionSearchField;
import com.mycollab.db.arguments.SearchField;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class CompositionStringParam extends Param {
    private StringParam[] params;

    public CompositionStringParam(String id, StringParam... params) {
        this.id = id;
        this.params = params;
    }

    public StringParam[] getParams() {
        return params;
    }

    public void setParams(StringParam[] params) {
        this.params = params;
    }

    public SearchField buildSearchField(String prefixOper, String compareOper, String value) {
        CompositionSearchField searchField = new CompositionSearchField(prefixOper);
        for (StringParam param : params) {
            SearchField field = param.buildSearchField("", compareOper, value);
            searchField.addField(field);
        }
        return searchField;
    }
}
