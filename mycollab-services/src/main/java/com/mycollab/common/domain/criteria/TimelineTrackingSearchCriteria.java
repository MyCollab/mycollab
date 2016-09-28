/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.common.domain.criteria;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
public class TimelineTrackingSearchCriteria extends SearchCriteria {

    private StringSearchField type;
    private StringSearchField fieldgroup;
    private SetSearchField<Integer> extraTypeIds;

    public StringSearchField getType() {
        return type;
    }

    public void setType(StringSearchField type) {
        this.type = type;
    }

    public SetSearchField<Integer> getExtraTypeIds() {
        return extraTypeIds;
    }

    public void setExtraTypeIds(SetSearchField<Integer> extraTypeIds) {
        this.extraTypeIds = extraTypeIds;
    }

    public StringSearchField getFieldgroup() {
        return fieldgroup;
    }

    public void setFieldgroup(StringSearchField fieldgroup) {
        this.fieldgroup = fieldgroup;
    }
}
