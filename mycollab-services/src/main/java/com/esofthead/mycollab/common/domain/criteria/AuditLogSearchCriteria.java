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

package com.esofthead.mycollab.common.domain.criteria;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.StringSearchField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AuditLogSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    private StringSearchField module;

    private StringSearchField type;

    private StringSearchField typeId;

    public StringSearchField getType() {
        return type;
    }

    public void setType(StringSearchField type) {
        this.type = type;
    }

    public StringSearchField getTypeId() {
        return typeId;
    }

    public void setTypeId(StringSearchField typeId) {
        this.typeId = typeId;
    }

    public StringSearchField getModule() {
        return module;
    }

    public void setModule(StringSearchField module) {
        this.module = module;
    }
}
