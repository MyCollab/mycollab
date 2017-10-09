package com.mycollab.module.project.domain;

import com.mycollab.common.domain.SimpleActivityStream;

/**
 * @author MyCollab Ltd.
 * @version 1.0
 */
public class ProjectActivityStream extends SimpleActivityStream {
    private static final long serialVersionUID = 1L;

    private int projectId;
    private String projectName;
    private String projectShortName;
    private Integer itemKey;

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

    public String getProjectShortName() {
        return projectShortName;
    }

    public void setProjectShortName(String projectShortName) {
        this.projectShortName = projectShortName;
    }

    public Integer getItemKey() {
        return itemKey;
    }

    public void setItemKey(Integer itemKey) {
        this.itemKey = itemKey;
    }
}
