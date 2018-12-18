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
package com.mycollab.shell.view;

import com.google.common.eventbus.Subscribe;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.module.user.accountsettings.localization.AdminI18nEnum;
import com.mycollab.module.user.ui.SettingAssetsManager;
import com.mycollab.module.user.ui.SettingUIConstants;
import com.mycollab.shell.event.ShellEvent;
import com.mycollab.vaadin.ApplicationEventListener;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ControllerRegistry;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.mycollab.vaadin.web.ui.AbstractAboutWindow;
import com.mycollab.vaadin.web.ui.ModuleHelper;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.web.CustomLayoutExt;
import com.mycollab.web.IDesktopModule;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class AbstractMainView extends MHorizontalLayout implements MainView {
    private static final long serialVersionUID = 1L;

    private CustomLayout headerLayout;
    private MHorizontalLayout bodyLayout;
    protected MHorizontalLayout accountLayout;

    private ApplicationEventListener<ShellEvent.RefreshPage> pageRefreshHandler = new ApplicationEventListener<ShellEvent.RefreshPage>() {
        @Override
        @Subscribe
        public void handle(ShellEvent.RefreshPage event) {
            buildAccountMenuLayout();
        }
    };

    public AbstractMainView() {
        this.setSizeFull();
        ControllerRegistry.addController(new MainViewController(this));
    }

    @Override
    public void attach() {
        super.attach();
        EventBusFactory.getInstance().register(pageRefreshHandler);
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(pageRefreshHandler);
        super.detach();
    }

    @Override
    public void display() {
        this.removeAllComponents();
        bodyLayout = new MHorizontalLayout().withSpacing(false).withStyleName("main-view").withId("main-body")
                .withFullHeight().withFullWidth();
        this.with(createMainMenu(), bodyLayout).expand(bodyLayout);
    }

    @Override
    public void addModule(IDesktopModule module) {
        headerLayout.removeComponent("serviceMenu");
        ModuleHelper.setCurrentModule(module);
        bodyLayout.removeAllComponents();
        bodyLayout.with(module).expand(module);
    }

    private CustomLayout createMainMenu() {
        headerLayout = CustomLayoutExt.createLayout("topNavigation");
        headerLayout.setStyleName("topNavigation");
        headerLayout.setHeight("100%");
        headerLayout.setWidth("70px");

        accountLayout = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, false)).withHeight("45px");
        accountLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        MButton navProjectBtn = new MButton("M", event -> EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, new String[]{})));
        MButton createBtn = new MButton("", (Button.ClickListener) clickEvent -> EventBusFactory.getInstance().post(new ShellEvent.ShowAssociateAddActionsPerModule(this))).withIcon(VaadinIcons.PLUS);
        MButton searchBtn = new MButton("").withIcon(VaadinIcons.SEARCH);

        MVerticalLayout actionControls = new MVerticalLayout(navProjectBtn, createBtn, searchBtn);
        headerLayout.addComponent(actionControls, "actionControls");


        Resource userAvatarRes = UserAvatarControlFactory.createAvatarResource(UserUIContext.getUserAvatarId(), 24);
        final PopupButton accountMenu = new PopupButton("");
        accountMenu.setDirection(Alignment.BOTTOM_RIGHT);
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

        MButton themeCustomizeBtn = new MButton(UserUIContext.getMessage(AdminI18nEnum.VIEW_THEME), clickEvent -> {
            accountMenu.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"setting", "theme"}));
        }).withIcon(SettingAssetsManager.getAsset(SettingUIConstants.THEME_CUSTOMIZE));
        accountPopupContent.addOption(themeCustomizeBtn);


        MButton setupBtn = new MButton(UserUIContext.getMessage(AdminI18nEnum.VIEW_SETUP), clickEvent -> {
            accountMenu.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"setup"}));
        }).withIcon(VaadinIcons.WRENCH);
        accountPopupContent.addOption(setupBtn);

        accountPopupContent.addSeparator();

        MButton helpBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_HELP)).withIcon(VaadinIcons.QUESTION_CIRCLE_O);
        ExternalResource helpRes = new ExternalResource("https://community.mycollab.com/meet-mycollab/");
        BrowserWindowOpener helpOpener = new BrowserWindowOpener(helpRes);
        helpOpener.extend(helpBtn);
        accountPopupContent.addOption(helpBtn);

        MButton supportBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SUPPORT)).withIcon(VaadinIcons.LIFEBUOY);
        ExternalResource supportRes = new ExternalResource("http://support.mycollab.com/");
        BrowserWindowOpener supportOpener = new BrowserWindowOpener(supportRes);
        supportOpener.extend(supportBtn);
        accountPopupContent.addOption(supportBtn);

        MButton translateBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_TRANSLATE)).withIcon(VaadinIcons.CLIPBOARD);
        ExternalResource translateRes = new ExternalResource("https://community.mycollab.com/docs/developing-mycollab/translating/");
        BrowserWindowOpener translateOpener = new BrowserWindowOpener(translateRes);
        translateOpener.extend(translateBtn);
        accountPopupContent.addOption(translateBtn);

        MButton myAccountBtn = new MButton(UserUIContext.getMessage(AdminI18nEnum.VIEW_BILLING), clickEvent -> {
            accountMenu.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"billing"}));
        }).withIcon(SettingAssetsManager.getAsset(SettingUIConstants.BILLING));
        accountPopupContent.addOption(myAccountBtn);

        accountPopupContent.addSeparator();
        MButton aboutBtn = new MButton(UserUIContext.getMessage(ShellI18nEnum.OPT_ABOUT_MYCOLLAB), clickEvent -> {
            accountMenu.setPopupVisible(false);
            Window aboutWindow = ViewManager.getCacheComponent(AbstractAboutWindow.class);
            UI.getCurrent().addWindow(aboutWindow);
        }).withIcon(VaadinIcons.INFO_CIRCLE);
        accountPopupContent.addOption(aboutBtn);

        MButton releaseNotesBtn = new MButton(UserUIContext.getMessage(ShellI18nEnum.OPT_RELEASE_NOTES)).withIcon(VaadinIcons.BULLETS);
        ExternalResource releaseNotesRes = new ExternalResource("https://community.mycollab.com/docs/hosting-mycollab-on-your-own-server/releases/");
        BrowserWindowOpener releaseNotesOpener = new BrowserWindowOpener(releaseNotesRes);
        releaseNotesOpener.extend(releaseNotesBtn);

        accountPopupContent.addOption(releaseNotesBtn);

        MButton signoutBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SIGNOUT), clickEvent -> {
            accountMenu.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.LogOut(this, null));
        }).withIcon(VaadinIcons.SIGN_OUT);
        accountPopupContent.addSeparator();
        accountPopupContent.addOption(signoutBtn);

        accountMenu.setContent(accountPopupContent);

        headerLayout.addComponent(accountMenu, "accountMenu");
        return headerLayout;
    }

    protected abstract MHorizontalLayout buildAccountMenuLayout();
}
