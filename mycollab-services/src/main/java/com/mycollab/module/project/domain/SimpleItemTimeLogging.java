package com.mycollab.module.project.domain;

import com.mycollab.core.utils.DateTimeUtils;

import java.util.Date;

public class SimpleItemTimeLogging extends ItemTimeLogging {
    private static final long serialVersionUID = 1L;

    private String logUserAvatarId;

    private String logUserFullName;

    private Double logUserRate;

    private Double logUserOvertimeRate;

    private String projectName;

    private String projectShortName;

    private String name;

    private Double percentageComplete;

    private String status;

    private Date dueDate;

    private Integer extraTypeId;

    public String getLogUserFullName() {
        return logUserFullName;
    }

    public void setLogUserFullName(String logUserFullName) {
        this.logUserFullName = logUserFullName;
    }

    public String getProjectName() {
        return projectName == null ? "" : projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectShortName() {
        return projectShortName == null ? "" : projectShortName;
    }

    public void setProjectShortName(String projectShortName) {
        this.projectShortName = projectShortName;
    }

    public Double getPercentageComplete() {
        return percentageComplete;
    }

    public void setPercentageComplete(Double percentageComplete) {
        this.percentageComplete = percentageComplete;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getLogUserAvatarId() {
        return logUserAvatarId;
    }

    public void setLogUserAvatarId(String logUserAvatarId) {
        this.logUserAvatarId = logUserAvatarId;
    }

    public Double getLogUserRate() {
        return logUserRate;
    }

    public void setLogUserRate(Double logUserRate) {
        this.logUserRate = logUserRate;
    }

    public Double getLogUserOvertimeRate() {
        return logUserOvertimeRate;
    }

    public void setLogUserOvertimeRate(Double logUserOvertimeRate) {
        this.logUserOvertimeRate = logUserOvertimeRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOverdue() {
        if (getDueDate() != null) {
            return getDueDate().before(DateTimeUtils.getCurrentDateWithoutMS());
        }

        return false;
    }

    public Integer getExtraTypeId() {
        return extraTypeId;
    }

    public void setExtraTypeId(Integer extraTypeId) {
        this.extraTypeId = extraTypeId;
    }

    public enum Field {
        summary, projectName, logUserFullName;

        public boolean equalTo(Object value) {
            return name().equals(value);
        }
    }
}
