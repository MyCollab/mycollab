package com.mycollab.module.project.domain;

import com.mycollab.core.utils.StringUtils;

import java.io.Serializable;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectTicketCount implements Serializable {
    private static final long serialVersionUID = 1L;

    private String assignUser;

    private String assignUserFullName;

    private int projectId;

    private String projectName;

    private int taskCount;

    public String getAssignUser() {
        return assignUser;
    }

    public void setAssignUser(String assignUser) {
        this.assignUser = assignUser;
    }

    public String getAssignUserFullName() {
        if (StringUtils.isBlank(assignUserFullName)) {
            String displayName = getAssignUser();
            int index = (displayName != null) ? displayName.indexOf("@") : 0;
            if (index > 0) {
                return displayName.substring(0, index);
            }
        }
        return assignUserFullName;
    }

    public void setAssignUserFullName(String assignUserFullName) {
        this.assignUserFullName = assignUserFullName;
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

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }
}
