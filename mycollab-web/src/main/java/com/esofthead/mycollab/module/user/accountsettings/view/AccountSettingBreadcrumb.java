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
package com.esofthead.mycollab.module.user.accountsettings.view;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.accountsettings.localization.AccountBreadcrumbI18nEnum;
import com.esofthead.mycollab.module.user.accountsettings.view.events.ProfileEvent;
import com.esofthead.mycollab.module.user.domain.Role;
import com.esofthead.mycollab.module.user.domain.SimpleRole;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.events.RoleEvent;
import com.esofthead.mycollab.module.user.events.UserEvent;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.CacheableComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.web.ui.CommonUIFactory;
import com.esofthead.mycollab.vaadin.web.ui.utils.LabelStringGenerator;
import com.lexaden.breadcrumb.Breadcrumb;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
@ViewComponent
public class AccountSettingBreadcrumb extends Breadcrumb implements CacheableComponent {
    private static final long serialVersionUID = 1L;

    private static LabelStringGenerator menuLinkGenerator = new BreadcrumbLabelStringGenerator();

    public AccountSettingBreadcrumb() {
        this.setShowAnimationSpeed(Breadcrumb.AnimSpeed.SLOW);
        this.setHideAnimationSpeed(Breadcrumb.AnimSpeed.SLOW);
        this.setUseDefaultClickBehaviour(false);
        this.addLink(new Button(null, new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                EventBusFactory.getInstance().post(new ProfileEvent.GotoProfileView(this, null));
            }
        }));
    }

    public void gotoProfile() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(AccountBreadcrumbI18nEnum.VIEW_PROFILE)));
        AppContext.addFragment("account/preview", "Profile");
    }

    public void gotoSetup() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(AccountBreadcrumbI18nEnum.VIEW_SETUP)));
        AppContext.addFragment("account/setup", "Setup");
    }

    public void gotoBillingPage() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(AccountBreadcrumbI18nEnum.VIEW_BILLING)));
        AppContext.addFragment("account/billing", "Billing");
    }

    public void gotoCancelAccountPage() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(AccountBreadcrumbI18nEnum.BUTTON_CANCEL_ACCOUNT)));
        AppContext.addFragment("account/cancel_account", "Cancel Account");
    }

    public void gotoUserList() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(AccountBreadcrumbI18nEnum.VIEW_USERS)));
        AppContext.addFragment("account/user/list", "Users");
    }

    public void gotoUserRead(SimpleUser user) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(AccountBreadcrumbI18nEnum.VIEW_USERS), new GotoUserListListener()));
        this.addLink(generateBreadcrumbLink(user.getDisplayName()));

        AppContext.addFragment("account/user/preview/" + UrlEncodeDecoder.encode(user.getUsername()),
                "Preview User " + user.getDisplayName());
    }

    public void gotoUserAdd() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(AccountBreadcrumbI18nEnum.VIEW_USERS), new GotoUserListListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ADD)));
        AppContext.addFragment("account/user/add", "Invite New User");
    }

    public void gotoUserEdit(final SimpleUser user) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(AccountBreadcrumbI18nEnum.VIEW_USERS), new GotoUserListListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(generateBreadcrumbLink(user.getDisplayName(), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                EventBusFactory.getInstance().post(new UserEvent.GotoRead(this, user.getUsername()));

            }
        }));
        this.setLinkEnabled(true, 2);
        this.addLink(new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        AppContext.addFragment("account/user/edit/" + UrlEncodeDecoder.encode(user.getUsername()), "Edit User " + user.getUsername());
    }

    public void gotoGeneralSetting() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(AccountBreadcrumbI18nEnum.VIEW_SETTING)));
        AppContext.addFragment("account/setting/general", AppContext.getMessage(AccountBreadcrumbI18nEnum.VIEW_SETTING));
    }

    public void gotoMakeTheme() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(AccountBreadcrumbI18nEnum.VIEW_SETTING)));
        AppContext.addFragment("account/setting/theme", AppContext.getMessage(AccountBreadcrumbI18nEnum.VIEW_SETTING));
    }

    private static class GotoUserListListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new UserEvent.GotoList(this, null));
        }
    }

    public void gotoRoleList() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(AccountBreadcrumbI18nEnum.VIEW_ROLES)));
        AppContext.addFragment("account/role/list", "Roles");
    }

    public void gotoRoleAdd() {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(AccountBreadcrumbI18nEnum.VIEW_ROLES), new GotoRoleListListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ADD)));
        AppContext.addFragment("account/role/add", "New Role");
    }

    public void gotoRoleRead(SimpleRole role) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(AccountBreadcrumbI18nEnum.VIEW_ROLES), new GotoRoleListListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(generateBreadcrumbLink(role.getRolename()));
        AppContext.addFragment("account/role/preview/" + UrlEncodeDecoder.encode(role.getId()), "Preview Role " + role.getRolename());
    }

    public void gotoRoleEdit(final Role role) {
        this.select(0);
        this.addLink(new Button(AppContext.getMessage(AccountBreadcrumbI18nEnum.VIEW_ROLES), new GotoRoleListListener()));
        this.setLinkEnabled(true, 1);
        this.addLink(generateBreadcrumbLink(role.getRolename(), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                EventBusFactory.getInstance().post(new RoleEvent.GotoRead(this, role.getId()));
            }
        }));
        this.setLinkEnabled(true, 2);
        this.addLink(new Button(AppContext.getMessage(GenericI18Enum.BUTTON_EDIT)));
        AppContext.addFragment("account/role/edit/" + UrlEncodeDecoder.encode(role.getId()), "Edit Role: " + role.getRolename());
    }

    private static class GotoRoleListListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new RoleEvent.GotoList(this, null));
        }
    }

    private static Button generateBreadcrumbLink(String linkname) {
        return CommonUIFactory.createButtonTooltip(menuLinkGenerator.handleText(linkname), linkname);
    }

    private static Button generateBreadcrumbLink(String linkname, Button.ClickListener listener) {
        return CommonUIFactory.createButtonTooltip(menuLinkGenerator.handleText(linkname), linkname, listener);
    }

    private static class BreadcrumbLabelStringGenerator implements LabelStringGenerator {

        @Override
        public String handleText(String value) {
            if (value.length() > 35) {
                return value.substring(0, 35) + "...";
            }
            return value;
        }

    }
}
