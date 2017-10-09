package com.mycollab.module.tracker.domain;

import com.mycollab.core.arguments.NotBindable;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.mycollab.core.utils.StringUtils.isBlank;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleBug extends BugWithBLOBs {
    private static final long serialVersionUID = 1L;

    private String loguserFullName;
    private String loguserAvatarId;
    private String assignUserAvatarId;
    private String assignuserFullName;
    private String projectname;
    private String projectShortName;
    private Integer numComments;
    private Double billableHours;
    private Double nonBillableHours;
    private Integer numFollowers;

    @NotBindable
    private List<Version> affectedVersions;

    @NotBindable
    private List<Version> fixedVersions;

    @NotBindable
    private List<Component> components;

    private String comment;
    private String milestoneName;

    public enum Field {
        selected,
        components,
        fixedVersions,
        affectedVersions,
        loguserFullName,
        assignuserFullName,
        milestoneName;

        public boolean equalTo(Object value) {
            return name().equals(value);
        }
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getLoguserFullName() {
        if (isBlank(loguserFullName)) {
            return StringUtils.extractNameFromEmail(getCreateduser());
        }
        return loguserFullName;
    }

    public void setLoguserFullName(String loguserFullName) {
        this.loguserFullName = loguserFullName;
    }

    public String getAssignuserFullName() {
        if (isBlank(assignuserFullName)) {
            String displayName = getAssignuser();
            return StringUtils
                    .extractNameFromEmail(displayName);
        }
        return assignuserFullName;
    }

    public void setAssignuserFullName(String assignuserFullName) {
        this.assignuserFullName = assignuserFullName;
    }

    public List<Version> getAffectedVersions() {
        return affectedVersions;
    }

    public void setAffectedVersions(List<Version> affectedVersions) {
        this.affectedVersions = affectedVersions;
    }

    public List<Version> getFixedVersions() {
        return fixedVersions;
    }

    public void setFixedVersions(List<Version> fixedVersions) {
        this.fixedVersions = fixedVersions;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public String getAssignUserAvatarId() {
        return assignUserAvatarId;
    }

    public void setAssignUserAvatarId(String assignUserAvatarId) {
        this.assignUserAvatarId = assignUserAvatarId;
    }

    public String getLoguserAvatarId() {
        return loguserAvatarId;
    }

    public Integer getNumFollowers() {
        return numFollowers;
    }

    public void setNumFollowers(Integer numFollowers) {
        this.numFollowers = numFollowers;
    }

    public void setLoguserAvatarId(String loguserAvatarId) {
        this.loguserAvatarId = loguserAvatarId;
    }

    public boolean isCompleted() {
        return isCompleted(this);
    }

    public static boolean isCompleted(BugWithBLOBs bug) {
        return BugStatus.Verified.name().equals(bug.getStatus());
    }

    public Double getBillableHours() {
        return billableHours;
    }

    public void setBillableHours(Double billableHours) {
        this.billableHours = billableHours;
    }

    public Double getNonBillableHours() {
        return nonBillableHours;
    }

    public void setNonBillableHours(Double nonBillableHours) {
        this.nonBillableHours = nonBillableHours;
    }

    public boolean isOverdue() {
        return isOverdue(this);
    }

    public static boolean isOverdue(BugWithBLOBs bug) {
        if (BugStatus.Verified.name().equals(bug.getStatus())) {
            return false;
        }

        if (bug.getDuedate() != null) {
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            Date todayDate = today.getTime();
            return todayDate.after(bug.getDuedate());
        } else {
            return false;
        }
    }

    public String getProjectShortName() {
        return projectShortName;
    }

    public void setProjectShortName(String projectShortName) {
        this.projectShortName = projectShortName;
    }

    public Integer getNumComments() {
        return numComments;
    }

    public void setNumComments(Integer numComments) {
        this.numComments = numComments;
    }

    public Date getDueDateRoundPlusOne() {
        Date value = getDuedate();
        return (value != null) ? DateTimeUtils.subtractOrAddDayDuration(value, 1) : null;
    }
}
