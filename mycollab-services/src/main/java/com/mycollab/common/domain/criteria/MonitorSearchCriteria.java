/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.common.domain.criteria;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MonitorSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    private StringSearchField user;

    private NumberSearchField typeId;

    private StringSearchField type;

    private SetSearchField<String> types;

    private SetSearchField<Integer> extraTypeIds;

    public StringSearchField getUser() {
        return user;
    }

    public void setUser(StringSearchField user) {
        this.user = user;
    }

    public NumberSearchField getTypeId() {
        return typeId;
    }

    public void setTypeId(NumberSearchField typeId) {
        this.typeId = typeId;
    }

    public StringSearchField getType() {
        return type;
    }

    public void setType(StringSearchField type) {
        this.type = type;
    }

    public SetSearchField<String> getTypes() {
        return types;
    }

    public void setTypes(SetSearchField<String> types) {
        this.types = types;
    }

    public SetSearchField<Integer> getExtraTypeIds() {
        return extraTypeIds;
    }

    public void setExtraTypeIds(SetSearchField<Integer> extraTypeIds) {
        this.extraTypeIds = extraTypeIds;
    }
}
