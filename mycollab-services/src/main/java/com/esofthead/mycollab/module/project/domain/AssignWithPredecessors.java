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

import com.esofthead.mycollab.core.arguments.NotBindable;

import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public abstract class AssignWithPredecessors {
    private Date startDate;
    private Date endDate;
    private Date deadline;
    private String projectName;
    private String name;
    private Long duration;
    private Integer ganttIndex;
    private String prjKey;
    private Integer prjId;
    private String assignUser;
    private String assignUserFullName;
    private String assignUserAvatarId;
    private Integer sAccountId;
    private String status;
    private String type;
    private Double progress;
    private Integer id;

    @NotBindable
    private List<TaskPredecessor> predecessors;

    @NotBindable
    private List<TaskPredecessor> dependents;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getGanttIndex() {
        return ganttIndex;
    }

    public void setGanttIndex(Integer ganttIndex) {
        this.ganttIndex = ganttIndex;
    }

    public String getPrjKey() {
        return prjKey;
    }

    public void setPrjKey(String prjKey) {
        this.prjKey = prjKey;
    }

    public Integer getPrjId() {
        return prjId;
    }

    public void setPrjId(Integer prjId) {
        this.prjId = prjId;
    }

    public String getAssignUser() {
        return assignUser;
    }

    public void setAssignUser(String assignUser) {
        this.assignUser = assignUser;
    }

    public String getAssignUserFullName() {
        return assignUserFullName;
    }

    public void setAssignUserFullName(String assignUserFullName) {
        this.assignUserFullName = assignUserFullName;
    }

    public String getAssignUserAvatarId() {
        return assignUserAvatarId;
    }

    public void setAssignUserAvatarId(String assignUserAvatarId) {
        this.assignUserAvatarId = assignUserAvatarId;
    }

    public Integer getsAccountId() {
        return sAccountId;
    }

    public void setsAccountId(Integer sAccountId) {
        this.sAccountId = sAccountId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<TaskPredecessor> getPredecessors() {
        return predecessors;
    }

    public void setPredecessors(List<TaskPredecessor> predecessors) {
        this.predecessors = predecessors;
    }

    public List<TaskPredecessor> getDependents() {
        return dependents;
    }

    public void setDependents(List<TaskPredecessor> dependents) {
        this.dependents = dependents;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public boolean hasSubAssignments() {
        return false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssignWithPredecessors)) return false;

        AssignWithPredecessors that = (AssignWithPredecessors) o;
        if (!prjId.equals(that.prjId)) return false;
        if (!type.equals(that.type)) return false;
        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        int result = 31 + prjId.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
