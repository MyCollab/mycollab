package com.mycollab.module.project.domain;

import com.mycollab.core.arguments.NotBindable;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.security.PermissionMap;

import java.util.Date;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleProjectMember extends ProjectMember {
    private static final long serialVersionUID = 1L;

    private String memberAvatarId;

    private String memberFullName;

    private String roleName;

    @NotBindable
    private PermissionMap permissionMaps;

    private Integer numOpenTasks;

    private Integer numOpenBugs;

    private String projectName;

    private String email;

    private Date lastAccessTime;

    private Double totalBillableLogTime;

    private Double totalNonBillableLogTime;

    public String getMemberFullName() {
        return getDisplayName();
    }

    public void setMemberFullName(String memberFullName) {
        this.memberFullName = memberFullName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public PermissionMap getPermissionMaps() {
        return permissionMaps;
    }

    public void setPermissionMaps(PermissionMap permissionMaps) {
        this.permissionMaps = permissionMaps;
    }

    public Integer getNumOpenTasks() {
        return numOpenTasks;
    }

    public void setNumOpenTasks(Integer numOpenTasks) {
        this.numOpenTasks = numOpenTasks;
    }

    public Integer getNumOpenBugs() {
        return numOpenBugs;
    }

    public void setNumOpenBugs(Integer numOpenBugs) {
        this.numOpenBugs = numOpenBugs;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getMemberAvatarId() {
        return memberAvatarId;
    }

    public void setMemberAvatarId(String memberAvatarId) {
        this.memberAvatarId = memberAvatarId;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public String getDisplayName() {
        if (StringUtils.isBlank(memberFullName)) {
            return StringUtils.extractNameFromEmail(getUsername());
        }
        return memberFullName;
    }

    public boolean isProjectOwner() {
        return Boolean.TRUE.equals(getIsadmin());
    }

    public Double getTotalBillableLogTime() {
        return totalBillableLogTime;
    }

    public void setTotalBillableLogTime(Double totalBillableLogTime) {
        this.totalBillableLogTime = totalBillableLogTime;
    }

    public Double getTotalNonBillableLogTime() {
        return totalNonBillableLogTime;
    }

    public void setTotalNonBillableLogTime(Double totalNonBillableLogTime) {
        this.totalNonBillableLogTime = totalNonBillableLogTime;
    }

    public Boolean canRead(String permissionItem) {
        return permissionMaps != null && permissionMaps.canRead(permissionItem);
    }

    public Boolean canWrite(String permissionItem) {
        return permissionMaps != null && permissionMaps.canWrite(permissionItem);
    }

    public Boolean canAccess(String permissionItem) {
        return permissionMaps != null && permissionMaps.canAccess(permissionItem);
    }

    public enum Field {
        roleName, memberFullName, totalBillableLogTime, totalNonBillableLogTime, projectName, numOpenTasks, numOpenBugs;

        public boolean equalTo(Object value) {
            return name().equals(value);
        }
    }
}
