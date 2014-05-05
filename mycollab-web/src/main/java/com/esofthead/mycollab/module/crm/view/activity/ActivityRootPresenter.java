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
package com.esofthead.mycollab.module.crm.view.activity;

import com.esofthead.mycollab.core.utils.ClassUtils;
import com.esofthead.mycollab.module.crm.localization.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.view.CrmGenericPresenter;
import com.esofthead.mycollab.module.crm.view.CrmToolbar;
import com.esofthead.mycollab.module.crm.view.parameters.ActivityScreenData;
import com.esofthead.mycollab.module.crm.view.parameters.AssignmentScreenData;
import com.esofthead.mycollab.module.crm.view.parameters.CallScreenData;
import com.esofthead.mycollab.module.crm.view.parameters.MeetingScreenData;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class ActivityRootPresenter extends
		CrmGenericPresenter<ActivityRootView> {
	private static final long serialVersionUID = 1L;

	public ActivityRootPresenter() {
		super(ActivityRootView.class);
	}

	@Override
	public void go(ComponentContainer container, ScreenData<?> data) {
		super.go(container, data, false);
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		super.onGo(container, data);

		CrmToolbar crmToolbar = ViewManager.getView(CrmToolbar.class);
		crmToolbar.gotoItem(AppContext
				.getMessage(CrmCommonI18nEnum.TOOLBAR_ACTIVITIES_HEADER));

		AbstractPresenter presenter = null;

		if (ClassUtils.instanceOf(data, AssignmentScreenData.Read.class,
				AssignmentScreenData.Add.class,
				AssignmentScreenData.Edit.class, MeetingScreenData.Add.class,
				MeetingScreenData.Edit.class, MeetingScreenData.Read.class,
				CallScreenData.Read.class, CallScreenData.Add.class,
				CallScreenData.Edit.class,
				ActivityScreenData.GotoActivityList.class)) {
			presenter = PresenterResolver.getPresenter(ActivityPresenter.class);
		} else {
			presenter = PresenterResolver
					.getPresenter(ActivityCalendarPresenter.class);
		}

		presenter.go(view.getWidget(), data);
	}

}
