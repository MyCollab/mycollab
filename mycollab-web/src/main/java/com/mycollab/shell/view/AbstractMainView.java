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
package com.mycollab.shell.view;

import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.eventmanager.ApplicationEventListener;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.shell.events.ShellEvent;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.AbstractPageView;
import com.mycollab.vaadin.mvp.ControllerRegistry;
import com.mycollab.vaadin.ui.AccountAssetsResolver;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.ModuleHelper;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.web.CustomLayoutExt;
import com.mycollab.web.IDesktopModule;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomLayout;
import org.joda.time.LocalDate;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.teemu.VaadinIcons;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class AbstractMainView extends AbstractPageView implements MainView {
    private static final long serialVersionUID = 1L;

    protected CustomLayout headerLayout;
    protected MHorizontalLayout bodyLayout;
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
        this.with(createTopMenu(), bodyLayout, createFooter()).expand(bodyLayout);
    }

    @Override
    public void addModule(IDesktopModule module) {
        headerLayout.removeComponent("serviceMenu");
        ModuleHelper.setCurrentModule(module);
        bodyLayout.removeAllComponents();
        bodyLayout.with(module).expand(module);

        MHorizontalLayout serviceMenu = module.buildMenu();
        if (serviceMenu != null) {
            headerLayout.addComponent(serviceMenu, "serviceMenu");
        }
        postAddModule(module);
    }

    protected void postAddModule(IDesktopModule module) {
    }

    private ComponentContainer createFooter() {
        MHorizontalLayout footer = new MHorizontalLayout().withFullWidth()
                .withMargin(new MarginInfo(false, true, false, true)).withStyleName("footer").withHeight("40px");

        Div copyrightInfoDiv = new Div().appendText(AppContext.getMessage(ShellI18nEnum.OPT_MYCOLLAB_COPY_RIGHT, new A("https://www.mycollab.com",
                "_blank").appendText("MyCollab").write(), new LocalDate().getYear() + ""));
        ELabel companyInfoLbl = ELabel.html(copyrightInfoDiv.write()).withWidthUndefined();
        footer.with(companyInfoLbl).withAlign(companyInfoLbl, Alignment.MIDDLE_LEFT);

        Div socialLinksDiv = new Div().appendText(FontAwesome.RSS.getHtml())
                .appendChild(new A("https://www.mycollab.com/blog", "_blank").appendText(" Blog"))
                .appendText("  " + FontAwesome.REPLY_ALL.getHtml())
                .appendChild(new A("http://support.mycollab.com", "_blank").appendText(" Support"));

        socialLinksDiv.appendText("  " + FontAwesome.FACEBOOK.getHtml())
                .appendChild(new A("https://www.facebook.com/mycollab2", "_blank").appendText(" FB page"));
        socialLinksDiv.appendText("  " + FontAwesome.TWITTER.getHtml())
                .appendChild(new A("https://twitter.com/intent/tweet?text=I am using MyCollab to manage all project " +
                        "activities, accounts and it works great @mycollabdotcom &source=webclient", "_blank")
                        .appendText(" Tweet"));

        ELabel socialsLbl = ELabel.html(socialLinksDiv.write()).withWidthUndefined();
        footer.with(socialsLbl).withAlign(socialsLbl, Alignment.MIDDLE_RIGHT);
        return footer;
    }

    private CustomLayout createTopMenu() {
        headerLayout = CustomLayoutExt.createLayout("topNavigation");
        headerLayout.setStyleName("topNavigation");
        headerLayout.setHeight("45px");
        headerLayout.setWidth("100%");

        final PopupButton modulePopup = new PopupButton("");
        modulePopup.setHeightUndefined();
        modulePopup.setDirection(Alignment.BOTTOM_LEFT);
        modulePopup.setIcon(AccountAssetsResolver.createLogoResource(AppContext.getBillingAccount().getLogopath(), 150));
        OptionPopupContent modulePopupContent = new OptionPopupContent();
        modulePopup.setContent(modulePopupContent);

        MButton projectModuleBtn = new MButton(AppContext.getMessage(GenericI18Enum.MODULE_PROJECT), clickEvent -> {
            modulePopup.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, null));
        }).withIcon(VaadinIcons.TASKS);
        modulePopupContent.addOption(projectModuleBtn);

        MButton crmModuleBtn = new MButton(AppContext.getMessage(GenericI18Enum.MODULE_CRM), clickEvent -> {
            modulePopup.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoCrmModule(this, null));
        }).withIcon(VaadinIcons.MONEY);
        modulePopupContent.addOption(crmModuleBtn);

        MButton fileModuleBtn = new MButton(AppContext.getMessage(GenericI18Enum.MODULE_DOCUMENT), clickEvent -> {
            modulePopup.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoFileModule(this, null));
        }).withIcon(VaadinIcons.SUITCASE);
        modulePopupContent.addOption(fileModuleBtn);

        MButton peopleBtn = new MButton(AppContext.getMessage(GenericI18Enum.MODULE_PEOPLE), clickEvent -> {
            modulePopup.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"user", "list"}));
        }).withIcon(VaadinIcons.USERS);
        modulePopupContent.addOption(peopleBtn);

        headerLayout.addComponent(new MHorizontalLayout().with(modulePopup).withAlign(modulePopup, Alignment.MIDDLE_LEFT), "mainLogo");

        accountLayout = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, false)).withHeight("45px");
        accountLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        buildAccountMenuLayout();

        headerLayout.addComponent(accountLayout, "accountMenu");
        return headerLayout;
    }

    protected abstract MHorizontalLayout buildAccountMenuLayout();
}
