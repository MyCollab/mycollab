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
import com.esofthead.mycollab.core.utils.StringUtils;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleTask extends Task {
    private static final long serialVersionUID = 1L;
    private String projectName;
    private String projectShortname;
    private String milestoneName;
    private String assignUserAvatarId;
    private String assignUserFullName;
    private String assignUserTimeZone;
    private String logByAvatarId;
    private String logByFullName;
    private String logByUserTimeZone;
    private Integer numComments;
    private String comment;
    private String parentTaskName;
    private Integer parentTaskKey;
    private Integer numSubTasks;
    private Double billableHours;
    private Double nonBillableHours;

    public Integer getNumComments() {
        return numComments;
    }

    public void setNumComments(Integer numComments) {
        this.numComments = numComments;
    }

    public Integer getParentTaskKey() {
        return parentTaskKey;
    }

    public void setParentTaskKey(Integer parentTaskKey) {
        this.parentTaskKey = parentTaskKey;
    }

    public Integer getNumSubTasks() {
        return numSubTasks;
    }

    public void setNumSubTasks(Integer numSubTasks) {
        this.numSubTasks = numSubTasks;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public String getAssignUserFullName() {
        if (StringUtils.isBlank(assignUserFullName)) {
            return StringUtils.extractNameFromEmail(getAssignuser());
        }
        return assignUserFullName;
    }

    public void setAssignUserFullName(String assignUserFullName) {
        this.assignUserFullName = assignUserFullName;
    }

    public String getLogByFullName() {
        if (StringUtils.isBlank(logByFullName)) {
            return StringUtils.extractNameFromEmail(getLogby());
        }
        return logByFullName;
    }

    public void setLogByFullName(String logByFullName) {
        this.logByFullName = logByFullName;
    }

    public String getAssignUserAvatarId() {
        return assignUserAvatarId;
    }

    public void setAssignUserAvatarId(String assignUserAvatarId) {
        this.assignUserAvatarId = assignUserAvatarId;
    }

    public String getLogByAvatarId() {
        return logByAvatarId;
    }

    public void setLogByAvatarId(String logByAvatarId) {
        this.logByAvatarId = logByAvatarId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public String getAssignUserTimeZone() {
        return assignUserTimeZone;
    }

    public void setAssignUserTimeZone(String assignUserTimeZone) {
        this.assignUserTimeZone = assignUserTimeZone;
    }

    public String getLogByUserTimeZone() {
        return logByUserTimeZone;
    }

    public void setLogByUserTimeZone(String logByUserTimeZone) {
        this.logByUserTimeZone = logByUserTimeZone;
    }

    public String getProjectShortname() {
        return projectShortname;
    }

    public void setProjectShortname(String projectShortname) {
        this.projectShortname = projectShortname;
    }

    public String getParentTaskName() {
        return parentTaskName;
    }

    public void setParentTaskName(String parentTaskName) {
        this.parentTaskName = parentTaskName;
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

    public boolean isCompleted() {
        return OptionI18nEnum.StatusI18nEnum.Closed.name().equals(getStatus()) || ((getPercentagecomplete() != null)
                && (100d == getPercentagecomplete()));
    }

    public boolean isPending() {
        return OptionI18nEnum.StatusI18nEnum.Pending.name().equals(getStatus());
    }

    public boolean isOverdue() {
        if (!isCompleted()) {
            Date now = DateTimeUtils.getCurrentDateWithoutMS();
            return (getDeadline() != null && getDeadline().before(now));
        }

        return false;
    }

    public Date getDeadlineRoundPlusOne() {
        Date value = getDeadline();
        return (value != null) ? DateTimeUtils.subtractOrAddDayDuration(value, 1) : null;
    }

    public enum Field {
        assignUserFullName, taskListName;

        public boolean equalTo(Object value) {
            return name().equals(value);
        }
    }
}
