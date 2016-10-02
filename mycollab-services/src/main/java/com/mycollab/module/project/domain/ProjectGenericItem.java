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

import com.mycollab.module.project.ProjectTypeConstants;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public class ProjectGenericItem {
    private String type;

    private String typeId;

    private String createdUser;

    private String createdUserAvatarId;

    private String createdUserDisplayName;

    private Date createdTime;

    private Date lastUpdatedTime;

    private String name;

    private String description;

    private Integer projectId;

    private String projectName;

    private String projectShortName;

    private Integer extraTypeId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getCreatedUserAvatarId() {
        return createdUserAvatarId;
    }

    public void setCreatedUserAvatarId(String createdUserAvatarId) {
        this.createdUserAvatarId = createdUserAvatarId;
    }

    public String getCreatedUserDisplayName() {
        return createdUserDisplayName;
    }

    public void setCreatedUserDisplayName(String createdUserDisplayName) {
        this.createdUserDisplayName = createdUserDisplayName;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public boolean isBug() {
        return ProjectTypeConstants.BUG.equals(getType());
    }

    public boolean isTask() {
        return ProjectTypeConstants.TASK.equals(getType());
    }
}
