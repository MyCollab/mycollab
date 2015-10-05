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

import com.esofthead.mycollab.common.i18n.OptionI18nEnum;
import com.esofthead.mycollab.core.arguments.NotBindable;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.esofthead.mycollab.core.utils.StringUtils.isBlank;

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleTaskList extends TaskList {
	private static final long serialVersionUID = 1L;

	private String projectName;
	private String milestoneName;
	private String createdUserAvatarId;
	private String createdUserFullName;
	private String ownerAvatarId;
	private String ownerFullName;

	@NotBindable
	private List<SimpleTask> subTasks = new ArrayList<>();

	@NotBindable
	private double percentageComplete;

	@NotBindable
	private int numOpenTasks;
	private int numAllTasks;
	private int numComments;
	private String comment;

	public String getMilestoneName() {
		return milestoneName;
	}

	public void setMilestoneName(String milestoneName) {
		this.milestoneName = milestoneName;
	}

	public String getOwnerFullName() {
		if (isBlank(ownerFullName)) {
			return StringUtils.extractNameFromEmail(getOwner());
		}
		return ownerFullName;
	}

	public void setOwnerFullName(String ownerFullName) {
		this.ownerFullName = ownerFullName;
	}

	public List<SimpleTask> getSubTasks() {
		return subTasks;
	}

	public void setSubTasks(List<SimpleTask> subTasks) {
		this.subTasks = subTasks;
	}

	public double getPercentageComplete() {
		return percentageComplete;
	}

	public void setPercentageComplete(double percentageComplete) {
		this.percentageComplete = percentageComplete;
	}

	public int getNumOpenTasks() {
		return numOpenTasks;
	}

	public void setNumOpenTasks(int numOpenTasks) {
		this.numOpenTasks = numOpenTasks;
	}

	public int getNumAllTasks() {
		return numAllTasks;
	}

	public void setNumAllTasks(int numAllTasks) {
		this.numAllTasks = numAllTasks;
	}

	public String getOwnerAvatarId() {
		return ownerAvatarId;
	}

	public void setOwnerAvatarId(String ownerAvatarId) {
		this.ownerAvatarId = ownerAvatarId;
	}

	public int getNumComments() {
		return numComments;
	}

	public void setNumComments(int numComments) {
		this.numComments = numComments;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public boolean isArchieved() {
		return OptionI18nEnum.StatusI18nEnum.Archived.name().equals(getStatus()) || OptionI18nEnum.StatusI18nEnum.Closed
				.name().equals(getStatus());
	}

	public Date getStartDate() {
		Date value = DateTimeUtils.getCurrentDateWithoutMS();
        for (SimpleTask task: getSubTasks()) {
            Date startDate = task.getStartdate();
            if (startDate != null && startDate.before(value)) {
                value = startDate;
            }
        }
        return value;
	}

	public Date getEndDate() {
		Date value = DateTimeUtils.getCurrentDateWithoutMS();
        for (SimpleTask task: getSubTasks()) {
            Date endDate = task.getEnddate();
            if (endDate == null) {
                endDate = task.getDeadline();
            }

            if (endDate != null && endDate.after(value)) {
                value = endDate;
            }
        }
		value = DateTimeUtils.subtractOrAddDayDuration(value, 1);
        return value;
	}
}
