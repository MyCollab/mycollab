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
package com.mycollab.common.domain;

import com.mycollab.core.utils.JsonDeSerializer;
import com.mycollab.core.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleAuditLog extends AuditLog {
    private static final long serialVersionUID = 1L;

    private List<AuditChangeItem> changeItems;

    private String postedUserFullName;

    private String postedUserAvatarId;

    public List<AuditChangeItem> getChangeItems() {
        if (changeItems == null) {
            changeItems = parseChangeItems();
        }
        if (changeItems == null) {
            changeItems = new ArrayList<>();
        }
        return changeItems;
    }

    public Date getCreatedtime() {
        return getPosteddate();
    }

    public String getPostedUserFullName() {
        if (StringUtils.isBlank(postedUserFullName)) {
            return StringUtils.extractNameFromEmail(getPosteduser());
        }
        return postedUserFullName;
    }

    public void setPostedUserFullName(String postedUserFullName) {
        this.postedUserFullName = postedUserFullName;
    }

    public String getPostedUserAvatarId() {
        return postedUserAvatarId;
    }

    public void setPostedUserAvatarId(String postedUserAvatarId) {
        this.postedUserAvatarId = postedUserAvatarId;
    }

    private List<AuditChangeItem> parseChangeItems() {
        return JsonDeSerializer.fromJson(
                this.getChangeset(), new TypeReference<List<AuditChangeItem>>() {
                });
    }
}
