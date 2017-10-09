package com.mycollab.module.project.domain;

import com.mycollab.core.arguments.ValuedBean;

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
public class StandupReportStatistic extends ValuedBean {
    private Integer projectId;

    private String projectKey;

    private String projectName;

    private String projectAvatarId;

    private Integer totalWrittenReports;

    private Integer totalReports;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getTotalWrittenReports() {
        return totalWrittenReports;
    }

    public void setTotalWrittenReports(Integer totalWrittenReports) {
        this.totalWrittenReports = totalWrittenReports;
    }

    public Integer getTotalReports() {
        return totalReports;
    }

    public void setTotalReports(Integer totalReports) {
        this.totalReports = totalReports;
    }

    public String getProjectAvatarId() {
        return projectAvatarId;
    }

    public void setProjectAvatarId(String projectAvatarId) {
        this.projectAvatarId = projectAvatarId;
    }
}
