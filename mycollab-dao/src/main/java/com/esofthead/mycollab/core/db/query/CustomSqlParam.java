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

import com.esofthead.mycollab.core.arguments.NoValueSearchField;

import java.util.Collection;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public abstract class CustomSqlParam extends Param {
    public CustomSqlParam(String id) {
        super(id);
    }

    public abstract NoValueSearchField buildPropertyParamInList(String oper, Collection<?> value);

    public abstract NoValueSearchField buildPropertyParamNotInList(String oper, Collection<?> value);
}
