/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.activity;

import com.mycollab.core.utils.ClassUtils;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.module.crm.view.parameters.ActivityScreenData;
import com.mycollab.module.crm.view.parameters.AssignmentScreenData;
import com.mycollab.module.crm.view.parameters.CallScreenData;
import com.mycollab.module.crm.view.parameters.MeetingScreenData;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ActivityRootPresenter extends CrmGenericPresenter<ActivityRootView> {
    private static final long serialVersionUID = 1L;

    public ActivityRootPresenter() {
        super(ActivityRootView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        super.onGo(container, data);
        AbstractPresenter presenter;

        if (ClassUtils.instanceOf(data, AssignmentScreenData.Read.class, AssignmentScreenData.Add.class,
                AssignmentScreenData.Edit.class, MeetingScreenData.Add.class,
                MeetingScreenData.Edit.class, MeetingScreenData.Read.class,
                CallScreenData.Read.class, CallScreenData.Add.class, CallScreenData.Edit.class,
                ActivityScreenData.GotoActivityList.class)) {
            presenter = PresenterResolver.getPresenter(ActivityPresenter.class);
        } else {
            presenter = PresenterResolver.getPresenter(ActivityCalendarPresenter.class);
        }

        presenter.go(view, data);
    }

}
