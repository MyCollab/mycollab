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
package com.esofthead.mycollab.module.project.domain;

import com.esofthead.mycollab.common.i18n.OptionI18nEnum;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class SimpleProblem extends Problem {
    private static final long serialVersionUID = 1L;

    private String raisedByUserAvatarId;

    private String raisedByUserFullName;

    private String assignUserAvatarId;

    private String assignedUserFullName;

    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRaisedByUserAvatarId() {
        return raisedByUserAvatarId;
    }

    public void setRaisedByUserAvatarId(String raisedByUserAvatarId) {
        this.raisedByUserAvatarId = raisedByUserAvatarId;
    }

    public String getAssignUserAvatarId() {
        return assignUserAvatarId;
    }

    public void setAssignUserAvatarId(String assignUserAvatarId) {
        this.assignUserAvatarId = assignUserAvatarId;
    }

    public String getRaisedByUserFullName() {
        if (StringUtils.isBlank(raisedByUserFullName)) {
            return com.esofthead.mycollab.core.utils.StringUtils.extractNameFromEmail(getRaisedbyuser());
        }
        return raisedByUserFullName;
    }

    public void setRaisedByUserFullName(String raisedByUserFullName) {
        this.raisedByUserFullName = raisedByUserFullName;
    }

    public String getAssignedUserFullName() {
        if (StringUtils.isBlank(assignedUserFullName)) {
            return com.esofthead.mycollab.core.utils.StringUtils.extractNameFromEmail(getAssigntouser());
        }
        return assignedUserFullName;
    }

    public void setAssignedUserFullName(String assignedUserFullName) {
        this.assignedUserFullName = assignedUserFullName;
    }

    public boolean isOverdue() {
        Date now = DateTimeUtils.getCurrentDateWithoutMS();
        return (OptionI18nEnum.StatusI18nEnum.Open.name().equals(getStatus())
                && (getDatedue() != null) && getDatedue().before(
                now));
    }

    public enum Field {
        assignedUserFullName;
        public boolean equalTo(Object value) {
            return name().equals(value);
        }
    }
}
