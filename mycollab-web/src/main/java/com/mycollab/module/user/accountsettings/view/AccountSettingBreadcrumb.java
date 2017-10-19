/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.accountsettings.view;

import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.user.accountsettings.localization.AdminI18nEnum;
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.accountsettings.view.event.AccountBillingEvent;
import com.mycollab.module.user.domain.Role;
import com.mycollab.module.user.domain.SimpleRole;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.event.RoleEvent;
import com.mycollab.module.user.event.UserEvent;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.CacheableComponent;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.CommonUIFactory;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.utils.LabelStringGenerator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
@ViewComponent
public class AccountSettingBreadcrumb extends MHorizontalLayout implements CacheableComponent {
    private static final long serialVersionUID = 1L;

    private static LabelStringGenerator menuLinkGenerator = new BreadcrumbLabelStringGenerator();

    public AccountSettingBreadcrumb() {
        setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
    }

    public void gotoProfile() {
        removeAllComponents();
        this.addComponent(new MButton(UserUIContext.getMessage(UserI18nEnum.OPT_PROFILE)).withStyleName(WebThemes.BUTTON_LINK));
        AppUI.addFragment("account/preview", UserUIContext.getMessage(UserI18nEnum.OPT_PROFILE));
    }

    public void gotoSetup() {
        removeAllComponents();
        this.addComponent(new MButton(UserUIContext.getMessage(AdminI18nEnum.VIEW_SETUP)).withStyleName(WebThemes.BUTTON_LINK));
        AppUI.addFragment("account/setup", UserUIContext.getMessage(AdminI18nEnum.VIEW_SETUP));
    }

    public void gotoBillingPage() {
        removeAllComponents();
        this.addComponent(new MButton(UserUIContext.getMessage(AdminI18nEnum.VIEW_BILLING)).withStyleName(WebThemes.BUTTON_LINK));
        AppUI.addFragment("account/billing", UserUIContext.getMessage(AdminI18nEnum.VIEW_BILLING));
    }

    public void gotoBillingHistory() {
        removeAllComponents();
        this.addComponent(new MButton(UserUIContext.getMessage(AdminI18nEnum.VIEW_BILLING),
                clickEvent -> EventBusFactory.getInstance().post(new AccountBillingEvent.GotoSummary(this, null))).withStyleName(WebThemes.BUTTON_LINK));
        this.addComponent(new ELabel("/"));
        this.addComponent(new MButton(UserUIContext.getMessage(AdminI18nEnum.VIEW_BILLING_HISTORY)).withStyleName(WebThemes.BUTTON_LINK));
        AppUI.addFragment("account/billing/history", UserUIContext.getMessage(AdminI18nEnum.VIEW_BILLING_HISTORY));
    }

    public void gotoCancelAccountPage() {
        removeAllComponents();
        this.addComponent(new MButton(UserUIContext.getMessage(AdminI18nEnum.VIEW_BILLING),
                clickEvent -> EventBusFactory.getInstance().post(new AccountBillingEvent.GotoSummary(this, null))).withStyleName(WebThemes.BUTTON_LINK));
        this.addComponent(new ELabel("/"));
        this.addComponent(new MButton(UserUIContext.getMessage(AdminI18nEnum.ACTION_CANCEL_ACCOUNT)).withStyleName(WebThemes.BUTTON_LINK));
        AppUI.addFragment("account/billing/cancel", UserUIContext.getMessage(AdminI18nEnum.ACTION_CANCEL_ACCOUNT));
    }

    public void gotoUserList() {
        removeAllComponents();
        this.addComponent(new MButton(UserUIContext.getMessage(UserI18nEnum.LIST)).withStyleName(WebThemes.BUTTON_LINK));
        AppUI.addFragment("account/user/list", UserUIContext.getMessage(UserI18nEnum.LIST));
    }

    public void gotoUserRead(SimpleUser user) {
        removeAllComponents();
        this.addComponent(new MButton(UserUIContext.getMessage(UserI18nEnum.LIST), new GotoUserListListener()).withStyleName(WebThemes.BUTTON_LINK));
        this.addComponent(new ELabel("/"));
        this.addComponent(generateBreadcrumbLink(user.getDisplayName()));

        AppUI.addFragment("account/user/preview/" + UrlEncodeDecoder.encode(user.getUsername()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE, UserUIContext.getMessage(UserI18nEnum.SINGLE), user.getDisplayName()));
    }

    public void gotoUserAdd() {
        removeAllComponents();
        this.addComponent(new MButton(UserUIContext.getMessage(UserI18nEnum.LIST), new GotoUserListListener()).withStyleName(WebThemes.BUTTON_LINK));
        this.addComponent(new ELabel("/"));
        this.addComponent(new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD)).withStyleName(WebThemes.BUTTON_LINK));
        AppUI.addFragment("account/user/add", UserUIContext.getMessage(AdminI18nEnum.ACTION_INVITE_NEW_USER));
    }

    public void gotoUserEdit(final SimpleUser user) {
        removeAllComponents();
        this.addComponent(new MButton(UserUIContext.getMessage(UserI18nEnum.LIST), new GotoUserListListener()).withStyleName(WebThemes.BUTTON_LINK));
        this.addComponent(new ELabel("/"));
        this.addComponent(generateBreadcrumbLink(user.getDisplayName(),
                clickEvent -> EventBusFactory.getInstance().post(new UserEvent.GotoRead(this, user.getUsername()))));
        this.addComponent(new ELabel("/"));
        this.addComponent(new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT)).withStyleName(WebThemes.BUTTON_LINK));
        AppUI.addFragment("account/user/edit/" + UrlEncodeDecoder.encode(user.getUsername()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE, UserUIContext.getMessage(UserI18nEnum.SINGLE), user.getDisplayName()));
    }

    public void gotoGeneralSetting() {
        removeAllComponents();
        this.addComponent(new MButton(UserUIContext.getMessage(AdminI18nEnum.VIEW_SETTING)).withStyleName(WebThemes.BUTTON_LINK));
        AppUI.addFragment("account/setting/general", UserUIContext.getMessage(AdminI18nEnum.VIEW_SETTING));
    }

    public void gotoMakeTheme() {
        removeAllComponents();
        this.addComponent(new MButton(UserUIContext.getMessage(AdminI18nEnum.VIEW_SETTING)).withStyleName(WebThemes.BUTTON_LINK));
        AppUI.addFragment("account/setting/theme", UserUIContext.getMessage(AdminI18nEnum.VIEW_SETTING));
    }

    private static class GotoUserListListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new UserEvent.GotoList(this, null));
        }
    }

    public void gotoRoleList() {
        removeAllComponents();
        this.addComponent(new MButton(UserUIContext.getMessage(RoleI18nEnum.LIST)).withStyleName(WebThemes.BUTTON_LINK));
        AppUI.addFragment("account/role/list", UserUIContext.getMessage(RoleI18nEnum.LIST));
    }

    public void gotoRoleAdd() {
        removeAllComponents();
        this.addComponent(new MButton(UserUIContext.getMessage(RoleI18nEnum.LIST), new GotoRoleListListener()).withStyleName(WebThemes.BUTTON_LINK));
        this.addComponent(new ELabel("/"));
        this.addComponent(new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_ADD)).withStyleName(WebThemes.BUTTON_LINK));
        AppUI.addFragment("account/role/add", UserUIContext.getMessage(RoleI18nEnum.NEW));
    }

    public void gotoRoleRead(SimpleRole role) {
        removeAllComponents();
        this.addComponent(new MButton(UserUIContext.getMessage(RoleI18nEnum.LIST), new GotoRoleListListener()).withStyleName(WebThemes.BUTTON_LINK));
        this.addComponent(new ELabel("/"));
        this.addComponent(generateBreadcrumbLink(role.getRolename()));
        AppUI.addFragment("account/role/preview/" + UrlEncodeDecoder.encode(role.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE, UserUIContext.getMessage(RoleI18nEnum.SINGLE), role.getRolename()));
    }

    public void gotoRoleEdit(final Role role) {
        removeAllComponents();
        this.addComponent(new MButton(UserUIContext.getMessage(RoleI18nEnum.LIST), new GotoRoleListListener()).withStyleName(WebThemes.BUTTON_LINK));
        this.addComponent(new ELabel("/"));
        this.addComponent(generateBreadcrumbLink(role.getRolename(),
                clickEvent -> EventBusFactory.getInstance().post(new RoleEvent.GotoRead(this, role.getId()))));
        this.addComponent(new ELabel("/"));
        this.addComponent(new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_EDIT)).withStyleName(WebThemes.BUTTON_LINK));
        AppUI.addFragment("account/role/edit/" + UrlEncodeDecoder.encode(role.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE, UserUIContext.getMessage(RoleI18nEnum.SINGLE), role.getRolename()));
    }

    private static class GotoRoleListListener implements Button.ClickListener {
        private static final long serialVersionUID = 1L;

        @Override
        public void buttonClick(ClickEvent event) {
            EventBusFactory.getInstance().post(new RoleEvent.GotoList(this, null));
        }
    }

    private static MButton generateBreadcrumbLink(String linkName) {
        return CommonUIFactory.createButtonTooltip(menuLinkGenerator.handleText(linkName), linkName).withStyleName(WebThemes.BUTTON_LINK);
    }

    private static MButton generateBreadcrumbLink(String linkName, Button.ClickListener listener) {
        return CommonUIFactory.createButtonTooltip(menuLinkGenerator.handleText(linkName), linkName, listener);
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
