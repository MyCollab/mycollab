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
import com.esofthead.mycollab.module.project.ProjectTypeConstants;

import java.io.Serializable;
import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectGenericTask implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    private String assignUser;

    private String assignUserFullName;

    private String assignUserAvatarId;

    private Date dueDate;

    private Integer projectId;

    private String projectName;

    private String projectShortName;

    private String type;

    private Integer typeId;

    private Integer extraTypeId;

    private String status;

    private Date lastUpdatedTime;

    private Integer sAccountId;

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

    public boolean isBug() {
        return ProjectTypeConstants.BUG.equals(getType());
    }

    public boolean isTask() {
        return ProjectTypeConstants.TASK.equals(getType());
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

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
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

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
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
        return OptionI18nEnum.StatusI18nEnum.Closed.name().equals(getStatus()) || com.esofthead.mycollab.module
                .project.i18n.OptionI18nEnum.BugStatus.Verified.name().equals(getStatus());
    }

    public String getProjectShortName() {
        return projectShortName;
    }

    public void setProjectShortName(String projectShortName) {
        this.projectShortName = projectShortName;
    }

    public int getExtraTypeId() {
        return extraTypeId;
    }

    public void setExtraTypeId(int extraTypeId) {
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
}
