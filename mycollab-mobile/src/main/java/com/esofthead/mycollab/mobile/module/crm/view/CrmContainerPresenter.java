/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.view;

import java.util.Arrays;

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.mobile.MobileApplication;
import com.esofthead.mycollab.mobile.module.crm.CrmModuleScreenData;
import com.esofthead.mycollab.mobile.shell.ModuleHelper;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePresenter;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
 *
 */
public class CrmContainerPresenter extends
		AbstractMobilePresenter<CrmContainerView> {

	private static final long serialVersionUID = -2422488836026839744L;

	public CrmContainerPresenter() {
		super(CrmContainerView.class);
	}

	@Override
	protected void onGo(ComponentContainer navigator, ScreenData<?> data) {
		ModuleHelper.setCurrentModule(view);
		super.onGo(navigator, data);
		AppContext.getInstance().updateLastModuleVisit(ModuleNameConstants.CRM);
		if (data == null) {
			view.goToAccounts();
			String url = ((MobileApplication)UI.getCurrent()).getInitialUrl();
			if (url != null && !url.equals("")) {
				String[] tokens = url.split("/");
				if (tokens.length > 1) {
					String[] fragments = Arrays.copyOfRange(tokens, 1,
							tokens.length);
					MobileApplication.rootUrlResolver.getSubResolver("crm")
							.handle(fragments);
					((MobileApplication) UI.getCurrent()).setInitialUrl("");
				}
			}
			return;
		}

		String submodule = ((CrmModuleScreenData.GotoModule) data).getParams();
		if (AppContext.getMessage(CrmCommonI18nEnum.TOOLBAR_ACCOUNTS_HEADER)
				.equals(submodule)) {
			view.goToAccounts();
		} else if (AppContext.getMessage(
				CrmCommonI18nEnum.TOOLBAR_CONTACTS_HEADER).equals(submodule)) {
			view.goToContacts();
		} else if (AppContext
				.getMessage(CrmCommonI18nEnum.TOOLBAR_CASES_HEADER).equals(
						submodule)) {
			view.goToCases();
		} else if (AppContext.getMessage(
				CrmCommonI18nEnum.TOOLBAR_CAMPAIGNS_HEADER).equals(submodule)) {
			view.goToCampaigns();
		} else if (AppContext
				.getMessage(CrmCommonI18nEnum.TOOLBAR_LEADS_HEADER).equals(
						submodule)) {
			view.goToLeads();
		} else if (AppContext.getMessage(
				CrmCommonI18nEnum.TOOLBAR_OPPORTUNTIES_HEADER)
				.equals(submodule)) {
			view.goToOpportunities();
		} else if (AppContext.getMessage(
				CrmCommonI18nEnum.TOOLBAR_ACTIVITIES_HEADER).equals(submodule)) {
			view.goToActivities();
		}
	}

}
