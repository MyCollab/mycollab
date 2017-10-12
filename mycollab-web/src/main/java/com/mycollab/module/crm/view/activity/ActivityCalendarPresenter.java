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
package com.mycollab.module.crm.view.activity;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.module.crm.view.CrmModule;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ActivityCalendarPresenter extends CrmGenericPresenter<ActivityCalendarView> {
    private static final long serialVersionUID = 1L;

    public ActivityCalendarPresenter() {
        super(ActivityCalendarView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.ACTIVITY);
        super.onGo(container, data);

        AppUI.addFragment("crm/activity/calendar", "Calendar");
    }
}
