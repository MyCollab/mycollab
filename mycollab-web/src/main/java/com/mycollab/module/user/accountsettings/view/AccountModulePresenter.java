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
package com.mycollab.module.user.accountsettings.view;

import com.mycollab.common.ModuleNameConstants;
import com.mycollab.shell.view.MainView;
import com.mycollab.shell.view.ShellUrlResolver;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class AccountModulePresenter extends AbstractPresenter<AccountModule> {
    private static final long serialVersionUID = 1L;

    public AccountModulePresenter() {
        super(AccountModule.class);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        AppContext.updateLastModuleVisit(ModuleNameConstants.ACCOUNT);
        MainView mainView = (MainView) container;
        mainView.addModule(view);

        String[] params = (String[]) data.getParams();
        if (params == null || params.length == 0) {
            view.gotoUserProfilePage();
        } else {
            ShellUrlResolver.ROOT().getSubResolver("account").handle(params);
        }
    }
}
