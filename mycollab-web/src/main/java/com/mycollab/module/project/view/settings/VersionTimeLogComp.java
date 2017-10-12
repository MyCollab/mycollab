/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.settings;

import com.mycollab.module.project.service.ItemTimeLoggingService;
import com.mycollab.module.project.ui.components.TimeLogComp;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;

/**
 * @author MyCollab Ltd
 * @since 5.0.5
 */
public class VersionTimeLogComp extends TimeLogComp<Version> {
    private ItemTimeLoggingService itemTimeLoggingService = AppContextUtil.getSpringBean(ItemTimeLoggingService.class);

    @Override
    protected Double getTotalBillableHours(Version bean) {
        return itemTimeLoggingService.getTotalBillableHoursByVersion(bean.getId(), AppUI.getAccountId());
    }

    @Override
    protected Double getTotalNonBillableHours(Version bean) {
        return itemTimeLoggingService.getTotalNonBillableHoursByVersion(bean.getId(), AppUI.getAccountId());
    }

    @Override
    protected Double getRemainedHours(Version bean) {
        return itemTimeLoggingService.getRemainHoursByVersion(bean.getId(), AppUI.getAccountId());
    }

    @Override
    protected boolean hasEditPermission() {
        return false;
    }

    @Override
    protected void showEditTimeWindow(Version bean) {

    }
}
