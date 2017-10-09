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
