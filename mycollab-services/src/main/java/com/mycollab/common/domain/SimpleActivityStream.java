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

import com.mycollab.core.utils.StringUtils;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleActivityStream extends ActivityStreamWithBLOBs {
    private static final long serialVersionUID = 1L;

    private String createdUserAvatarId;
    private String createdUserFullName;
    private SimpleAuditLog assoAuditLog;

    public String getCreatedUserAvatarId() {
        return createdUserAvatarId;
    }

    public void setCreatedUserAvatarId(String createdUserAvatarId) {
        this.createdUserAvatarId = createdUserAvatarId;
    }

    public String getCreatedUserFullName() {
        if (StringUtils.isBlank(createdUserFullName)) {
            return StringUtils.extractNameFromEmail(getCreateduser());
        }
        return createdUserFullName;
    }

    public void setCreatedUserFullName(String createdUserFullName) {
        this.createdUserFullName = createdUserFullName;
    }

    public SimpleAuditLog getAssoAuditLog() {
        return assoAuditLog;
    }

    public void setAssoAuditLog(SimpleAuditLog assoAuditLog) {
        this.assoAuditLog = assoAuditLog;
    }
}
