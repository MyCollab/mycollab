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

package com.mycollab.module.project.domain;

import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleMilestone extends Milestone {
    private static final long serialVersionUID = 1L;

    private String ownerAvatarId;
    private String ownerFullName;

    private String createdUserAvatarId;
    private String createdUserFullName;

    private Integer numOpenTasks;
    private Integer numTasks;

    private Integer numOpenBugs;
    private Integer numBugs;

    private Integer numOpenRisks;
    private Integer numRisks;

    private Double totalTaskBillableHours;
    private Double totalTaskNonBillableHours;
    private Double totalBugBillableHours;
    private Double totalBugNonBillableHours;

    private String projectShortName;
    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getOwnerFullName() {
        if (StringUtils.isBlank(ownerFullName)) {
            return StringUtils.extractNameFromEmail(getAssignuser());
        }
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public Integer getNumOpenTasks() {
        return numOpenTasks;
    }

    public void setNumOpenTasks(Integer numOpenTasks) {
        this.numOpenTasks = numOpenTasks;
    }

    public Integer getNumTasks() {
        return numTasks;
    }

    public void setNumTasks(Integer numTasks) {
        this.numTasks = numTasks;
    }

    public Integer getNumOpenBugs() {
        return numOpenBugs;
    }

    public void setNumOpenBugs(Integer numOpenBugs) {
        this.numOpenBugs = numOpenBugs;
    }

    public Integer getNumOpenRisks() {
        return numOpenRisks;
    }

    public void setNumOpenRisks(Integer numOpenRisks) {
        this.numOpenRisks = numOpenRisks;
    }

    public Integer getNumRisks() {
        return numRisks;
    }

    public void setNumRisks(Integer numRisks) {
        this.numRisks = numRisks;
    }

    public Integer getNumBugs() {
        return numBugs;
    }

    public void setNumBugs(Integer numBugs) {
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

    public String getProjectShortName() {
        return projectShortName;
    }

    public void setProjectShortName(String projectShortName) {
        this.projectShortName = projectShortName;
    }

    public boolean isOverdue() {
        if (!MilestoneStatus.Closed.name().equals(getStatus())) {
            Date now = DateTimeUtils.getCurrentDateWithoutMS();
            return (getEnddate() != null && getEnddate().before(now));
        }

        return false;
    }

    public boolean isClosed() {
        return MilestoneStatus.Closed.name().equals(getStatus());
    }

    public enum Field {
        numOpenTasks, numTasks, numOpenBugs, numBugs, ownerFullName, totalTaskBillableHours,
        totalTaskNonBillableHours, totalBugBillableHours, totalBugNonBillableHours, totalBillableHours, totalNonBillableHours;

        public boolean equalTo(Object value) {
            return name().equals(value);
        }
    }
}
