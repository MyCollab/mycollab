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
package com.esofthead.mycollab.module.user.view;

import java.util.Date;

import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.domain.SimpleBillingAccount;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.UserPreference;
import com.esofthead.mycollab.module.user.events.UserEvent.PlainLogin;
import com.esofthead.mycollab.module.user.service.BillingAccountService;
import com.esofthead.mycollab.module.user.service.UserPreferenceService;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.MyCollabUI;
import com.esofthead.mycollab.vaadin.mvp.PageView.ViewEvent;
import com.esofthead.mycollab.vaadin.mvp.PageView.ViewListener;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.web.DesktopApplication;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class LoginPresenter extends AbstractPresenter<LoginView> {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(LoginPresenter.class);

    public LoginPresenter() {
        super(LoginView.class);
    }

    @Override
    protected void postInitView() {
        view.addViewListener(new ViewListener<PlainLogin>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void receiveEvent(ViewEvent<PlainLogin> event) {
                PlainLogin data = (PlainLogin) event.getData();
                doLogin(data.getUsername(), data.getPassword(),
                        data.isRememberMe());
            }
        });
    }

    public void doLogin(String username, String password, boolean isRememberPassword) {
        UserService userService = ApplicationContextUtil.getSpringBean(UserService.class);
        SimpleUser user = userService.authentication(username, password, AppContext.getSubDomain(), false);

        if (isRememberPassword) {
            ((DesktopApplication) UI.getCurrent()).rememberPassword(username, password);
        }

        BillingAccountService billingAccountService = ApplicationContextUtil
                .getSpringBean(BillingAccountService.class);

        SimpleBillingAccount billingAccount = billingAccountService
                .getBillingAccountById(AppContext.getAccountId());

        LOG.debug("Get billing account successfully: " + BeanUtility.printBeanObj(billingAccount));

        UserPreferenceService preferenceService = ApplicationContextUtil
                .getSpringBean(UserPreferenceService.class);
        UserPreference pref = preferenceService.getPreferenceOfUser(username, AppContext.getAccountId());

        LOG.debug("Login to system successfully. Save user and preference "
                + pref + " to session");

        AppContext.getInstance().setSessionVariables(user, pref, billingAccount);
        pref.setLastaccessedtime(new Date());
        preferenceService.updateWithSession(pref, AppContext.getUsername());
        EventBusFactory.getInstance().post(new ShellEvent.GotoMainPage(this, null));
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        container.removeAllComponents();
        container.addComponent(view.getWidget());
        AppContext.addFragment("user/login", "Login Page");
    }
}
