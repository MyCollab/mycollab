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
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.module.project.ui.components.TimeLogComp;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * @author MyCollab Ltd
 * @since 5.0.5
 */
public class MilestoneTimeLogComp extends TimeLogComp<SimpleMilestone> {
    private ItemTimeLoggingService itemLogService = ApplicationContextUtil.getSpringBean(ItemTimeLoggingService.class);

    @Override
    protected Double getTotalBillableHours(SimpleMilestone bean) {
        return itemLogService.getTotalBillableHoursByMilestone(bean.getId(), AppContext.getAccountId());
    }

    @Override
    protected Double getTotalNonBillableHours(SimpleMilestone bean) {
        return itemLogService.getTotalNonBillableHoursByMilestone(bean.getId(), AppContext.getAccountId());
    }

    @Override
    protected Double getRemainedHours(SimpleMilestone bean) {
        return itemLogService.getRemainHoursByMilestone(bean.getId(), AppContext.getAccountId());
    }

    @Override
    protected boolean hasEditPermission() {
        return false;
    }

    @Override
    protected void showEditTimeWindow(SimpleMilestone bean) {

    }
}
