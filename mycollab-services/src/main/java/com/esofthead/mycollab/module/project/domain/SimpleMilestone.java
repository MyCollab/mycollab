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

import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 */
public class SimpleMilestone extends Milestone {
    private static final long serialVersionUID = 1L;

    private String ownerAvatarId;
    private String ownerFullName;

    private String createdUserAvatarId;
    private String createdUserFullName;

    private int numOpenTasks;
    private int numTasks;

    private int numOpenBugs;
    private int numBugs;

    private Double totalTaskBillableHours;
    private Double totalTaskNonBillableHours;
    private Double totalBugBillableHours;
    private Double totalBugNonBillableHours;

    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getOwnerFullName() {
        if (StringUtils.isBlank(ownerFullName)) {
            return StringUtils.extractNameFromEmail(getOwner());
        }
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public int getNumOpenTasks() {
        return numOpenTasks;
    }

    public void setNumOpenTasks(int numOpenTasks) {
        this.numOpenTasks = numOpenTasks;
    }

    public int getNumTasks() {
        return numTasks;
    }

    public void setNumTasks(int numTasks) {
        this.numTasks = numTasks;
    }

    public int getNumOpenBugs() {
        return numOpenBugs;
    }

    public void setNumOpenBugs(int numOpenBugs) {
        this.numOpenBugs = numOpenBugs;
    }

    public int getNumBugs() {
        return numBugs;
    }

    public void setNumBugs(int numBugs) {
        this.numBugs = numBugs;
    }

    public String getOwnerAvatarId() {
        return ownerAvatarId;
    }

    public void setOwnerAvatarId(String ownerAvatarId) {
        this.ownerAvatarId = ownerAvatarId;
    }

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

    public Double getTotalTaskBillableHours() {
        return totalTaskBillableHours;
    }

    public void setTotalTaskBillableHours(Double totalTaskBillableHours) {
        this.totalTaskBillableHours = totalTaskBillableHours;
    }

    public Double getTotalTaskNonBillableHours() {
        return totalTaskNonBillableHours;
    }

    public void setTotalTaskNonBillableHours(Double totalTaskNonBillableHours) {
        this.totalTaskNonBillableHours = totalTaskNonBillableHours;
    }

    public Double getTotalBugBillableHours() {
        return totalBugBillableHours;
    }

    public void setTotalBugBillableHours(Double totalBugBillableHours) {
        this.totalBugBillableHours = totalBugBillableHours;
    }

    public Double getTotalBugNonBillableHours() {
        return totalBugNonBillableHours;
    }

    public void setTotalBugNonBillableHours(Double totalBugNonBillableHours) {
        this.totalBugNonBillableHours = totalBugNonBillableHours;
    }

    public boolean isOverdue() {
        if (!OptionI18nEnum.MilestoneStatus.Closed.name().equals(getStatus())) {
            Date now = DateTimeUtils.getCurrentDateWithoutMS();
            return (getEnddate() != null && getEnddate().before(now));
        }

        return false;
    }
}
