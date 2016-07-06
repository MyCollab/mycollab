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
