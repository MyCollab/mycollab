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
package com.esofthead.mycollab.module.crm.view.setting;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.ClassUtils;
import com.esofthead.mycollab.module.crm.data.CustomViewScreenData;
import com.esofthead.mycollab.module.crm.data.NotificationSettingScreenData;
import com.esofthead.mycollab.module.crm.view.CrmGenericPresenter;
import com.esofthead.mycollab.module.crm.view.CrmModule;
import com.esofthead.mycollab.vaadin.mvp.IPresenter;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class CrmSettingPresenter extends
		CrmGenericPresenter<CrmSettingContainer> {
	private static final long serialVersionUID = 1L;

	public CrmSettingPresenter() {
		super(CrmSettingContainer.class);
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		IPresenter presenter;
		CrmModule crmModule = (CrmModule) container;
		crmModule.addView(view);

		if (ClassUtils.instanceOf(data,
				NotificationSettingScreenData.Read.class)) {
			presenter = PresenterResolver
					.getPresenter(CrmNotifcationSettingPresenter.class);
		} else if (ClassUtils.instanceOf(data, CustomViewScreenData.Read.class)) {
			presenter = PresenterResolver
					.getPresenter(ICrmCustomViewPresenter.class);
		} else {
			throw new MyCollabException("Do not support screen data " + data);
		}

		presenter.go(view, data);
	}
}
