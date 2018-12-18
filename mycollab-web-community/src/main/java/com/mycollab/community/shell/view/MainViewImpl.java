/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.community.shell.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.LicenseI18nEnum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.common.ui.components.notification.RequestUploadAvatarNotification;
import com.mycollab.common.ui.components.notification.SmtpSetupNotification;
import com.mycollab.community.shell.view.components.AdRequestWindow;
import com.mycollab.community.vaadin.web.ui.NotificationComponent;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.mail.service.ExtMailService;
import com.mycollab.module.user.accountsettings.localization.AdminI18nEnum;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.ui.SettingAssetsManager;
import com.mycollab.module.user.ui.SettingUIConstants;
import com.mycollab.shell.event.ShellEvent;
import com.mycollab.shell.view.AbstractMainView;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.mycollab.vaadin.web.ui.AbstractAboutWindow;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.time.LocalDate;

/**
 * @author MyCollab Ltd
 * @since 5.3.5
 */
@ViewComponent
public class MainViewImpl extends AbstractMainView {

    @Override
    protected MHorizontalLayout buildAccountMenuLayout() {
        accountLayout.removeAllComponents();

        Label accountNameLabel = new Label(AppUI.getSubDomain());
        accountNameLabel.addStyleName("subDomain");
        accountLayout.addComponent(accountNameLabel);

        MButton buyPremiumBtn = new MButton("", clickEvent -> UI.getCurrent().addWindow(new AdWindow()))
                .withIcon(VaadinIcons.CART).withStyleName("ad")
                .withDescription(UserUIContext.getMessage(LicenseI18nEnum.OPT_TRIAL_THE_PRO_EDITION));
        accountLayout.addComponent(buyPremiumBtn);

        NotificationComponent notificationComponent = new NotificationComponent();
        accountLayout.addComponent(notificationComponent);

        if (StringUtils.isBlank(UserUIContext.getUser().getAvatarid())) {
            EventBusFactory.getInstance().post(new ShellEvent.NewNotification(this, new RequestUploadAvatarNotification()));
        }

        ExtMailService mailService = AppContextUtil.getSpringBean(ExtMailService.class);
        if (!mailService.isMailSetupValid()) {
            EventBusFactory.getInstance().post(new ShellEvent.NewNotification(this, new SmtpSetupNotification()));
        }

        SimpleUser user = UserUIContext.getUser();
        LocalDate tenDaysAgo = LocalDate.now().minusDays(10);

        if (!Boolean.TRUE.equals(user.getRequestad()) && user.getRegisteredtime().toLocalDate().isBefore(tenDaysAgo)) {
            UI.getCurrent().addWindow(new AdRequestWindow(user));
        }

        Resource userAvatarRes = UserAvatarControlFactory.createAvatarResource(UserUIContext.getUserAvatarId(), 24);
        final PopupButton accountMenu = new PopupButton("");
        accountMenu.setIcon(userAvatarRes);
        accountMenu.setDescription(UserUIContext.getUserDisplayName());

        OptionPopupContent accountPopupContent = new OptionPopupContent();

        MButton myProfileBtn = new MButton(UserUIContext.getMessage(AdminI18nEnum.VIEW_PROFILE), clickEvent -> {
            accountMenu.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"preview"}));
        }).withIcon(SettingAssetsManager.getAsset(SettingUIConstants.PROFILE));
        accountPopupContent.addOption(myProfileBtn);

        MButton userMgtBtn = new MButton(UserUIContext.getMessage(AdminI18nEnum.VIEW_USERS_AND_ROLES), clickEvent -> {
            accountMenu.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"user", "list"}));
        }).withIcon(SettingAssetsManager.getAsset(SettingUIConstants.USERS));
        accountPopupContent.addOption(userMgtBtn);

        MButton generalSettingBtn = new MButton(UserUIContext.getMessage(AdminI18nEnum.VIEW_SETTING), clickEvent -> {
            accountMenu.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"setting", "general"}));
        }).withIcon(SettingAssetsManager.getAsset(SettingUIConstants.GENERAL_SETTING));
        accountPopupContent.addOption(generalSettingBtn);

        MButton setupBtn = new MButton(UserUIContext.getMessage(AdminI18nEnum.VIEW_SETUP), clickEvent -> {
            accountMenu.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"setup"}));
        }).withIcon(VaadinIcons.WRENCH);
        accountPopupContent.addOption(setupBtn);

        accountPopupContent.addSeparator();

        MButton helpBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_HELP)).withIcon(VaadinIcons.ACADEMY_CAP);
        ExternalResource helpRes = new ExternalResource("https://community.mycollab.com/meet-mycollab/");
        BrowserWindowOpener helpOpener = new BrowserWindowOpener(helpRes);
        helpOpener.extend(helpBtn);
        accountPopupContent.addOption(helpBtn);

        MButton supportBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SUPPORT)).withIcon(VaadinIcons.ACADEMY_CAP);
        ExternalResource supportRes = new ExternalResource("http://support.mycollab.com/");
        BrowserWindowOpener supportOpener = new BrowserWindowOpener(supportRes);
        supportOpener.extend(supportBtn);
        accountPopupContent.addOption(supportBtn);

        MButton translateBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_TRANSLATE)).withIcon(VaadinIcons.PENCIL);
        ExternalResource translateRes = new ExternalResource("https://community.mycollab.com/docs/developing-mycollab/translating/");
        BrowserWindowOpener translateOpener = new BrowserWindowOpener(translateRes);
        translateOpener.extend(translateBtn);
        accountPopupContent.addOption(translateBtn);

        accountPopupContent.addSeparator();
        MButton aboutBtn = new MButton(UserUIContext.getMessage(ShellI18nEnum.OPT_ABOUT_MYCOLLAB), clickEvent -> {
            accountMenu.setPopupVisible(false);
            Window aboutWindow = ViewManager.getCacheComponent(AbstractAboutWindow.class);
            UI.getCurrent().addWindow(aboutWindow);
        }).withIcon(VaadinIcons.INFO_CIRCLE);
        accountPopupContent.addOption(aboutBtn);

        Button releaseNotesBtn = new Button(UserUIContext.getMessage(ShellI18nEnum.OPT_RELEASE_NOTES));
        ExternalResource releaseNotesRes = new ExternalResource("https://community.mycollab.com/docs/hosting-mycollab-on-your-own-server/releases/");
        BrowserWindowOpener releaseNotesOpener = new BrowserWindowOpener(releaseNotesRes);
        releaseNotesOpener.extend(releaseNotesBtn);

        releaseNotesBtn.setIcon(VaadinIcons.LINES_LIST);
        accountPopupContent.addOption(releaseNotesBtn);

        MButton signoutBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SIGNOUT), clickEvent -> {
            accountMenu.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.LogOut(this, null));
        }).withIcon(VaadinIcons.SIGN_OUT);
        accountPopupContent.addSeparator();
        accountPopupContent.addOption(signoutBtn);

        accountMenu.setContent(accountPopupContent);
        accountLayout.addComponent(accountMenu);
        return accountLayout;
    }
}
