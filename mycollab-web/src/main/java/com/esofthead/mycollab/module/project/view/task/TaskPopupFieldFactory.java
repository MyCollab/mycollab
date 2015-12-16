/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.vaadin.mvp.CacheableComponent;
import com.vaadin.ui.PopupView;

/**
 * @author MyCollab Ltd
 * @since 5.1.2
 */
public interface TaskPopupFieldFactory extends CacheableComponent {

    PopupView createAssigneePopupField(SimpleTask task);

    PopupView createPriorityPopupField(SimpleTask task);

    PopupView createCommentsPopupField(SimpleTask task);

    PopupView createStatusPopupField(SimpleTask task);

    PopupView createMilestonePopupField(SimpleTask task);

    PopupView createPercentagePopupField(SimpleTask task);

    PopupView createDeadlinePopupField(SimpleTask task);

    PopupView createStartDatePopupField(SimpleTask task);

    PopupView createEndDatePopupField(SimpleTask task);

    PopupView createBillableHoursPopupField(SimpleTask task);

    PopupView createNonBillableHoursPopupField(SimpleTask task);

    PopupView createFollowersPopupField(SimpleTask task);
}
