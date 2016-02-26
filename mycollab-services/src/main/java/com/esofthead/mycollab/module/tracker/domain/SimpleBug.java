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
package com.esofthead.mycollab.module.tracker.domain;

import com.esofthead.mycollab.core.arguments.NotBindable;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.esofthead.mycollab.core.utils.StringUtils.isBlank;

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
            String displayName = getLogby();
            return com.esofthead.mycollab.core.utils.StringUtils
                    .extractNameFromEmail(displayName);
        }
        return loguserFullName;
    }

    public void setLoguserFullName(String loguserFullName) {
        this.loguserFullName = loguserFullName;
    }

    public String getAssignuserFullName() {
        if (isBlank(assignuserFullName)) {
            String displayName = getAssignuser();
            return com.esofthead.mycollab.core.utils.StringUtils
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
        return BugStatus.Verified.name().equals(getStatus()) || BugStatus.Resolved.name().equals(getStatus());
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
        if (BugStatus.Verified.name().equals(getStatus())) {
            return false;
        }

        if (this.getDuedate() != null) {
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            Date todayDate = today.getTime();

            return todayDate.after(this.getDuedate());
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
