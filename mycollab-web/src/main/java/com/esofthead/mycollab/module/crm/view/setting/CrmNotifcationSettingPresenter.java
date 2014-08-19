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

import com.esofthead.mycollab.module.crm.domain.CrmNotificationSetting;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.crm.view.CrmGenericPresenter;
import com.esofthead.mycollab.module.crm.view.CrmToolbar;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class CrmNotifcationSettingPresenter extends
		CrmGenericPresenter<CrmNotificationSettingView> {
	private static final long serialVersionUID = 1L;

	public CrmNotifcationSettingPresenter() {
		super(CrmNotificationSettingView.class);
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		CrmSettingContainer settingContainer = (CrmSettingContainer) container;
		settingContainer.gotoSubView("notification");

		CrmToolbar crmToolbar = ViewManager.getCacheComponent(CrmToolbar.class);
		crmToolbar.gotoItem(AppContext
				.getMessage(CrmCommonI18nEnum.TOOLBAR_CRMNOTIFICATION_HEADER));

		CrmNotificationSettingService service = ApplicationContextUtil
				.getSpringBean(CrmNotificationSettingService.class);
		CrmNotificationSetting setting = service.findNotification(
				AppContext.getUsername(), AppContext.getAccountId());
		view.showNotificationSettings(setting);

		AppContext.addFragment("crm/setting/notification",
				"Notification Settings");
	}
}
