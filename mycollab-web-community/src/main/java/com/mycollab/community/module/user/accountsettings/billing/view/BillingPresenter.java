/**
 * This file is part of mycollab-web-community.
 *
 * mycollab-web-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.community.module.user.accountsettings.billing.view;

import com.mycollab.module.user.accountsettings.billing.view.IBillingContainer;
import com.mycollab.module.user.accountsettings.billing.view.IBillingPresenter;
import com.mycollab.module.user.accountsettings.view.AccountModule;
import com.mycollab.module.user.ui.SettingUIConstants;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BillingPresenter extends AbstractPresenter<IBillingContainer> implements IBillingPresenter {
    private static final long serialVersionUID = 1L;

    public BillingPresenter() {
        super(IBillingContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        AccountModule accountContainer = (AccountModule) container;
        accountContainer.gotoSubView(SettingUIConstants.BILLING);
    }
}
