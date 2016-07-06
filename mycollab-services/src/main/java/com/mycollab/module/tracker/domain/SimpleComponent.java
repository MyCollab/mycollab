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
package com.mycollab.module.tracker.domain;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleComponent extends Component {
    private static final long serialVersionUID = 1L;

    private String userLeadAvatarId;

    private String userLeadFullName;

    private String createdUserAvatarId;

    private String createdUserFullName;

    private Integer numOpenBugs;

    private Integer numBugs;

    private String projectName;

    public String getUserLeadFullName() {
        return userLeadFullName;
    }

    public void setUserLeadFullName(String userLeadFullName) {
        this.userLeadFullName = userLeadFullName;
    }

    public Integer getNumOpenBugs() {
        return numOpenBugs;
    }

    public void setNumOpenBugs(Integer numOpenBugs) {
        this.numOpenBugs = numOpenBugs;
    }

    public Integer getNumBugs() {
        return numBugs;
    }

    public void setNumBugs(Integer numBugs) {
        this.numBugs = numBugs;
    }

    public String getUserLeadAvatarId() {
        return userLeadAvatarId;
    }

    public void setUserLeadAvatarId(String userLeadAvatarId) {
        this.userLeadAvatarId = userLeadAvatarId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCreatedUserAvatarId() {
        return createdUserAvatarId;
    }

    public void setCreatedUserAvatarId(String createdUserAvatarId) {
        this.createdUserAvatarId = createdUserAvatarId;
    }

    public String getCreatedUserFullName() {
        return createdUserFullName;
    }

    public void setCreatedUserFullName(String createdUserFullName) {
        this.createdUserFullName = createdUserFullName;
    }

    public enum Field {
        numOpenBugs, numBugs;
        public boolean equalTo(Object value) {
            return name().equals(value);
        }
    }
}
