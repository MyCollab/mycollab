package com.mycollab.mobile.shell.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.shell.event.ShellEvent;
import com.mycollab.mobile.ui.AbstractMobileMainView;
import com.mycollab.mobile.ui.MobileUIConstants;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
@ViewComponent
public class MainView extends AbstractMobileMainView {
    private static final long serialVersionUID = 1316340508967377888L;

    public MainView() {
        this.setSizeFull();

        MVerticalLayout contentLayout = new MVerticalLayout().withStyleName("content-wrapper").withFullWidth();
        contentLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        Image mainLogo = new Image(null, new ThemeResource("icons/logo_m.png"));
        contentLayout.addComponent(mainLogo);

        Label introText = new Label("MyCollab helps you do all your office jobs on the computers, phones and tablets you use");
        introText.setStyleName("intro-text");
        contentLayout.addComponent(introText);

        CssLayout welcomeTextWrapper = new CssLayout();
        welcomeTextWrapper.setStyleName("welcometext-wrapper");
        welcomeTextWrapper.setWidth("100%");
        welcomeTextWrapper.setHeight("15px");
        contentLayout.addComponent(welcomeTextWrapper);

        Button crmButton = new Button(UserUIContext.getMessage(GenericI18Enum.MODULE_CRM),
                clickEvent -> EventBusFactory.getInstance().post(new ShellEvent.GotoCrmModule(this, null)));
        crmButton.addStyleName(MobileUIConstants.BUTTON_ACTION);
        crmButton.setWidth("100%");

        contentLayout.addComponent(crmButton);

        Button pmButton = new Button(UserUIContext.getMessage(GenericI18Enum.MODULE_PROJECT),
                clickEvent -> EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, null)));
        pmButton.setWidth("100%");
        pmButton.addStyleName(MobileUIConstants.BUTTON_ACTION);
        contentLayout.addComponent(pmButton);

        this.addComponent(contentLayout);
    }
}
