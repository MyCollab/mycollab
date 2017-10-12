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
package com.mycollab.module.project.domain;

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.StringUtils;

import java.util.Date;

public class SimpleRisk extends Risk {
    private static final long serialVersionUID = 1L;

    private String risksource;

    private String raisedByUserAvatarId;

    private String raisedByUserFullName;

    private String assignToUserAvatarId;

    private String assignedToUserFullName;

    private String projectShortName;

    private String projectName;

    private String milestoneName;

    private Double billableHours;

    private Double nonBillableHours;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRaisedByUserFullName() {
        if (StringUtils.isBlank(raisedByUserFullName)) {
            return StringUtils.extractNameFromEmail(getCreateduser());
        }
        return raisedByUserFullName;
    }

    public void setRaisedByUserFullName(String raisedByUserFullName) {
        this.raisedByUserFullName = raisedByUserFullName;
    }

    public String getAssignedToUserFullName() {
        if (StringUtils.isBlank(assignedToUserFullName)) {
            return StringUtils.extractNameFromEmail(getAssignuser());
        }
        return assignedToUserFullName;
    }

    public void setAssignedToUserFullName(String assignedToUserFullName) {
        this.assignedToUserFullName = assignedToUserFullName;
    }

    public String getRisksource() {
        return risksource;
    }

    public void setRisksource(String risksource) {
        this.risksource = risksource;
    }

    public String getRaisedByUserAvatarId() {
        return raisedByUserAvatarId;
    }

    public void setRaisedByUserAvatarId(String raisedByUserAvatarId) {
        this.raisedByUserAvatarId = raisedByUserAvatarId;
    }

    public String getAssignToUserAvatarId() {
        return assignToUserAvatarId;
    }

    public void setAssignToUserAvatarId(String assignToUserAvatarId) {
        this.assignToUserAvatarId = assignToUserAvatarId;
    }

    public boolean isCompleted() {
        return StatusI18nEnum.Closed.name().equals(getStatus());
    }

    public boolean isOverdue() {
        Date now = DateTimeUtils.getCurrentDateWithoutMS();
        return StatusI18nEnum.Open.name().equals(getStatus()) && (getDuedate() != null) && getDuedate().before(now);
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public String getProjectShortName() {
        return projectShortName;
    }

    public void setProjectShortName(String projectShortName) {
        this.projectShortName = projectShortName;
    }

    public Double getBillableHours() {
        return billableHours;
    }

    public void setBillableHours(Double billableHours) {
        this.billableHours = billableHours;
    }

    public Double getNonBillableHours() {
        return nonBillableHours;
    }

    public void setNonBillableHours(Double nonBillableHours) {
        this.nonBillableHours = nonBillableHours;
    }

    public enum Field {
        assignedToUserFullName;

        public boolean equalTo(Object value) {
            return name().equals(value);
        }
    }
}
