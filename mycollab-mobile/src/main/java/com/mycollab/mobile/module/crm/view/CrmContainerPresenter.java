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
package com.mycollab.mobile.module.crm.view;

import com.mycollab.common.ModuleNameConstants;
import com.mycollab.mobile.MobileApplication;
import com.mycollab.mobile.module.crm.CrmModuleScreenData;
import com.mycollab.mobile.shell.ModuleHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.module.crm.i18n.*;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class CrmContainerPresenter extends AbstractCrmPresenter<CrmContainerView> {
    private static final long serialVersionUID = -2422488836026839744L;

    public CrmContainerPresenter() {
        super(CrmContainerView.class);
    }

    @Override
    protected void onGo(ComponentContainer navigator, ScreenData<?> data) {
        ModuleHelper.setCurrentModule(view);
        super.onGo(navigator, data);
        UserUIContext.updateLastModuleVisit(ModuleNameConstants.CRM);
        if (data == null) {
            view.goToAccounts();
            String url = ((MobileApplication) UI.getCurrent()).getCurrentFragmentUrl();
            if (url != null && !url.equals("")) {
                String[] tokens = url.split("/");
                if (tokens.length > 1) {
                    String[] fragments = Arrays.copyOfRange(tokens, 1, tokens.length);
                    MobileApplication.rootUrlResolver.getSubResolver("crm").handle(fragments);
                    ((MobileApplication) UI.getCurrent()).setCurrentFragmentUrl("");
                }
            }
            return;
        }

        String submodule = ((CrmModuleScreenData.GotoModule) data).getParams();
        if (UserUIContext.getMessage(AccountI18nEnum.LIST).equals(submodule)) {
            view.goToAccounts();
        } else if (UserUIContext.getMessage(ContactI18nEnum.LIST).equals(submodule)) {
            view.goToContacts();
        } else if (UserUIContext.getMessage(CaseI18nEnum.LIST).equals(submodule)) {
            view.goToCases();
        } else if (UserUIContext.getMessage(CampaignI18nEnum.LIST).equals(submodule)) {
            view.goToCampaigns();
        } else if (UserUIContext.getMessage(LeadI18nEnum.LIST).equals(submodule)) {
            view.goToLeads();
        } else if (UserUIContext.getMessage(OpportunityI18nEnum.LIST).equals(submodule)) {
            view.goToOpportunities();
        } else if (UserUIContext.getMessage(CrmCommonI18nEnum.TOOLBAR_ACTIVITIES_HEADER).equals(submodule)) {
            view.goToActivities();
        }
    }

}
