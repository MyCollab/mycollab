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
package com.esofthead.mycollab.module.project.view.milestone;

import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.vaadin.mvp.CacheableComponent;
import com.vaadin.ui.PopupView;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
public interface MilestonePopupFieldFactory extends CacheableComponent {
    PopupView createMilestoneAssigneePopupField(SimpleMilestone milestone, boolean isDisplayName);

    PopupView createStartDatePopupField(SimpleMilestone milestone);

    PopupView createEndDatePopupField(SimpleMilestone milestone);

    PopupView createBillableHoursPopupField(SimpleMilestone milestone);

    PopupView createNonBillableHoursPopupField(SimpleMilestone milestone);
}
