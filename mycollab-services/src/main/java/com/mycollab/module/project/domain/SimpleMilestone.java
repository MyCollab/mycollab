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

    public boolean isCompleted() {
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
