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
package com.mycollab.module.project.view.service;

import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.vaadin.mvp.CacheableComponent;
import com.vaadin.ui.AbstractComponent;

/**
 * @author MyCollab Ltd
 * @since 5.1.2
 */
public interface TaskComponentFactory {

    AbstractComponent createAssigneePopupField(SimpleTask task);

    AbstractComponent createPriorityPopupField(SimpleTask task);

    AbstractComponent createCommentsPopupField(SimpleTask task);

    AbstractComponent createStatusPopupField(SimpleTask task);

    AbstractComponent createMilestonePopupField(SimpleTask task);

    AbstractComponent createPercentagePopupField(SimpleTask task);

    AbstractComponent createDeadlinePopupField(SimpleTask task);

    AbstractComponent createStartDatePopupField(SimpleTask task);

    AbstractComponent createEndDatePopupField(SimpleTask task);

    AbstractComponent createBillableHoursPopupField(SimpleTask task);

    AbstractComponent createNonBillableHoursPopupField(SimpleTask task);

    AbstractComponent createFollowersPopupField(SimpleTask task);
}
