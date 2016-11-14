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
package com.mycollab.common.domain;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.user.domain.SimpleUser;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleRelayEmailNotification extends RelayEmailNotificationWithBLOBs {
    private static final long serialVersionUID = 1L;

    private String accountLogo;
    private String changeByUserFullName;
    private List<SimpleUser> notifyUsers;

    public String getChangeByUserFullName() {
        if (StringUtils.isBlank(changeByUserFullName)) {
            return StringUtils.extractNameFromEmail(getChangeby());
        }
        return changeByUserFullName;
    }

    public void setChangeByUserFullName(String changeByUserFullName) {
        this.changeByUserFullName = changeByUserFullName;
    }

    public List<SimpleUser> getNotifyUsers() {
        return notifyUsers;
    }

    public void setNotifyUsers(List<SimpleUser> notifyUsers) {
        this.notifyUsers = notifyUsers;
    }

    public String getAccountLogo() {
        return accountLogo;
    }

    public void setAccountLogo(String accountLogo) {
        this.accountLogo = accountLogo;
    }
}
