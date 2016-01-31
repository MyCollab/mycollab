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
package com.esofthead.mycollab.module.user.domain.criteria;

import com.esofthead.mycollab.core.arguments.*;
import com.esofthead.mycollab.core.utils.DateTimeUtils;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class UserSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    private StringSearchField displayName;
    private StringSearchField username;
    private SetSearchField<String> registerStatuses;
    private StringSearchField subdomain;
    private SetSearchField<String> statuses;
    private BooleanSearchField isAccountOwner;

    public StringSearchField getDisplayName() {
        return displayName;
    }

    public void setDisplayName(StringSearchField displayName) {
        this.displayName = displayName;
    }

    public StringSearchField getUsername() {
        return username;
    }

    public void setUsername(StringSearchField username) {
        this.username = username;
    }

    public SetSearchField<String> getRegisterStatuses() {
        return registerStatuses;
    }

    public void setRegisterStatuses(SetSearchField<String> registerStatuses) {
        this.registerStatuses = registerStatuses;
    }

    public StringSearchField getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(StringSearchField subdomain) {
        this.subdomain = subdomain;
    }

    public SetSearchField<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(SetSearchField<String> statuses) {
        this.statuses = statuses;
    }

    // @NOTE: Only works with method find... not getTotalCount(...)
    public void setLastAccessTimeRange(Date from, Date to) {
        String expr = String.format("s_user_account.lastAccessedTime >= '%s' AND s_user_account.lastAccessedTime <='%s'",
                        DateTimeUtils.formatDate(from, "yyyy-MM-dd"),
                        DateTimeUtils.formatDate(to, "yyyy-MM-dd"));
        NoValueSearchField searchField = new NoValueSearchField(SearchField.AND, expr);
        this.addExtraField(searchField);
    }
}
