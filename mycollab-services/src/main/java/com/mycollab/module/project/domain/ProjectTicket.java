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

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.mycollab.module.tracker.domain.SimpleBug;

import java.io.Serializable;
import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectTicket extends ValuedBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    private String assignUser;

    private String assignUserFullName;

    private String assignUserAvatarId;

    private String createdUser;

    private String createdUserFullName;

    private String createdUserAvatarId;

    private Date dueDate;

    private Integer projectId;

    private String projectName;

    private String projectShortName;

    private String type;

    private Integer typeId;

    private Integer extraTypeId;

    private String status;

    private String priority;

    private Date createdTime;

    private Date lastUpdatedTime;

    private Integer sAccountId;

    private Double billableHours;

    private Double nonBillableHours;

    private Integer numFollowers;

    private Date startDate;

    private Date endDate;

    private Integer milestoneId;

    private String milestoneName;

    private Integer numComments;

    private Double originalestimate;

    private Double remainestimate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssignUser() {
        return assignUser;
    }

    public void setAssignUser(String assignUser) {
        this.assignUser = assignUser;
    }

    public String getAssignUserFullName() {
        if (StringUtils.isBlank(assignUserFullName)) {
            return StringUtils.extractNameFromEmail(getAssignUser());
        }
        return assignUserFullName;
    }

    public Integer getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(Integer milestoneId) {
        this.milestoneId = milestoneId;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public boolean isBug() {
        return ProjectTypeConstants.BUG.equals(getType());
    }

    public boolean isMilestone() {
        return ProjectTypeConstants.MILESTONE.equals(getType());
    }

    public boolean isTask() {
        return ProjectTypeConstants.TASK.equals(getType());
    }

    public boolean isRisk() {
        return ProjectTypeConstants.RISK.equals(getType());
    }

    public void setAssignUserFullName(String assignUserFullName) {
        this.assignUserFullName = assignUserFullName;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public Integer getNumComments() {
        return numComments;
    }

    public void setNumComments(Integer numComments) {
        this.numComments = numComments;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getAssignUserAvatarId() {
        return assignUserAvatarId;
    }

    public void setAssignUserAvatarId(String assignUserAvatarId) {
        this.assignUserAvatarId = assignUserAvatarId;
    }

    public boolean isOverdue() {
        if (getDueDate() != null && !isClosed()) {
            Date currentDay = DateTimeUtils.getCurrentDateWithoutMS();
            return currentDay.after(getDueDate());
        }
        return false;
    }

    public boolean isClosed() {
        return StatusI18nEnum.Closed.name().equals(getStatus()) || BugStatus.Verified.name().equals(getStatus());
    }

    public String getProjectShortName() {
        return projectShortName;
    }

    public void setProjectShortName(String projectShortName) {
        this.projectShortName = projectShortName;
    }

    public Integer getExtraTypeId() {
        return extraTypeId;
    }

    public void setExtraTypeId(Integer extraTypeId) {
        this.extraTypeId = extraTypeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumFollowers() {
        return numFollowers;
    }

    public void setNumFollowers(Integer numFollowers) {
        this.numFollowers = numFollowers;
    }

    public Integer getsAccountId() {
        return sAccountId;
    }

    public void setsAccountId(Integer sAccountId) {
        this.sAccountId = sAccountId;
    }

    public Date getDueDatePlusOne() {
        Date value = getDueDate();
        return (value != null) ? DateTimeUtils.subtractOrAddDayDuration(value, 1) : null;
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

    public Date getStartDate() {
        if (startDate != null) {
            return startDate;
        } else {
            if (endDate != null && dueDate != null) {
                return (endDate.before(dueDate)) ? endDate : dueDate;
            } else {
                return (endDate != null) ? endDate : dueDate;
            }
        }
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        if (endDate != null) {
            return endDate;
        } else {
            if (startDate != null && dueDate != null) {
                return (startDate.before(dueDate)) ? dueDate : startDate;
            } else {
                return (startDate != null) ? startDate : dueDate;
            }
        }
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getCreatedUserFullName() {
        return createdUserFullName;
    }

    public void setCreatedUserFullName(String createdUserFullName) {
        this.createdUserFullName = createdUserFullName;
    }

    public String getCreatedUserAvatarId() {
        return createdUserAvatarId;
    }

    public void setCreatedUserAvatarId(String createdUserAvatarId) {
        this.createdUserAvatarId = createdUserAvatarId;
    }

    public Double getOriginalestimate() {
        return originalestimate;
    }

    public void setOriginalestimate(Double originalestimate) {
        this.originalestimate = originalestimate;
    }

    public Double getRemainestimate() {
        return remainestimate;
    }

    public void setRemainestimate(Double remainestimate) {
        this.remainestimate = remainestimate;
    }

    public static Task buildTask(ProjectTicket bean) {
        Task task = new Task();
        task.setId(bean.getTypeId());
        task.setProjectid(bean.getProjectId());
        task.setName(bean.getName());
        task.setStartdate(bean.getStartDate());
        task.setEnddate(bean.getEndDate());
        task.setDuedate(bean.getDueDate());
        task.setStatus(bean.getStatus());
        task.setSaccountid(bean.getsAccountId());
        task.setPriority(bean.getPriority());
        task.setAssignuser(bean.getAssignUser());
        task.setMilestoneid(bean.getMilestoneId());
        return task;
    }

    public static SimpleBug buildBug(ProjectTicket bean) {
        SimpleBug bug = new SimpleBug();
        bug.setId(bean.getTypeId());
        bug.setProjectid(bean.getProjectId());
        bug.setName(bean.getName());
        bug.setStartdate(bean.getStartDate());
        bug.setEnddate(bean.getEndDate());
        bug.setDuedate(bean.getDueDate());
        bug.setStatus(bean.getStatus());
        bug.setPriority(bean.getPriority());
        bug.setSaccountid(bean.getsAccountId());
        bug.setAssignuser(bean.getAssignUser());
        bug.setMilestoneid(bean.getMilestoneId());
        return bug;
    }

    public static Risk buildRisk(ProjectTicket bean) {
        Risk risk = new Risk();
        risk.setId(bean.getTypeId());
        risk.setProjectid(bean.getProjectId());
        risk.setName(bean.getName());
        risk.setStartdate(bean.getStartDate());
        risk.setEnddate(bean.getEndDate());
        risk.setDuedate(bean.getDueDate());
        risk.setStatus(bean.getStatus());
        risk.setSaccountid(bean.getsAccountId());
        risk.setPriority(bean.getPriority());
        risk.setAssignuser(bean.getAssignUser());
        risk.setMilestoneid(bean.getMilestoneId());
        return risk;
    }
}
