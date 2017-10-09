package com.mycollab.shell.view;

import com.google.common.eventbus.Subscribe;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.ApplicationEventListener;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.shell.event.ShellEvent;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ControllerRegistry;
import com.mycollab.vaadin.ui.AccountAssetsResolver;
import com.mycollab.vaadin.web.ui.ModuleHelper;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.web.CustomLayoutExt;
import com.mycollab.web.IDesktopModule;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomLayout;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.teemu.VaadinIcons;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class AbstractMainView extends AbstractVerticalPageView implements MainView {
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
        this.with(createTopMenu(), bodyLayout).expand(bodyLayout);
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
    }

    private CustomLayout createTopMenu() {
        headerLayout = CustomLayoutExt.createLayout("topNavigation");
        headerLayout.setStyleName("topNavigation");
        headerLayout.setHeight("45px");
        headerLayout.setWidth("100%");

        final PopupButton modulePopup = new PopupButton("");
        modulePopup.setIcon(AccountAssetsResolver.createLogoResource(AppUI.getBillingAccount().getLogopath(), 150));
        modulePopup.setHeightUndefined();
        modulePopup.setDirection(Alignment.BOTTOM_LEFT);
        OptionPopupContent modulePopupContent = new OptionPopupContent();
        modulePopup.setContent(modulePopupContent);

        MButton projectModuleBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.MODULE_PROJECT), clickEvent -> {
            modulePopup.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, null));
        }).withIcon(VaadinIcons.TASKS);
        modulePopupContent.addOption(projectModuleBtn);

        MButton crmModuleBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.MODULE_CRM), clickEvent -> {
            modulePopup.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoCrmModule(this, null));
        }).withIcon(VaadinIcons.MONEY);
        modulePopupContent.addOption(crmModuleBtn);

        MButton fileModuleBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.MODULE_DOCUMENT), clickEvent -> {
            modulePopup.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoFileModule(this, null));
        }).withIcon(VaadinIcons.SUITCASE);
        modulePopupContent.addOption(fileModuleBtn);

        MButton peopleBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.MODULE_PEOPLE), clickEvent -> {
            modulePopup.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"user", "list"}));
        }).withIcon(VaadinIcons.USERS);
        modulePopupContent.addOption(peopleBtn);


        headerLayout.addComponent(new MHorizontalLayout(modulePopup).alignAll(Alignment.MIDDLE_LEFT).withFullHeight(), "mainLogo");

        accountLayout = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, false)).withHeight("45px");
        accountLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        buildAccountMenuLayout();

        headerLayout.addComponent(accountLayout, "accountMenu");
        return headerLayout;
    }

    protected abstract MHorizontalLayout buildAccountMenuLayout();
}
