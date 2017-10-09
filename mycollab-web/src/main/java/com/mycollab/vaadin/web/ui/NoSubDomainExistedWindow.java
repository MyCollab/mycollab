package com.mycollab.vaadin.web.ui;

import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class NoSubDomainExistedWindow extends MVerticalLayout {
    private static final long serialVersionUID = 1L;

    public NoSubDomainExistedWindow(final String domain) {
        this.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        final Label titleIcon = ELabel.fontIcon(FontAwesome.EXCLAMATION_CIRCLE).withStyleName("warning-icon",
                ValoTheme.LABEL_NO_MARGIN).withWidthUndefined();

        Label warningMsg = new ELabel(UserUIContext.getMessage(ShellI18nEnum.ERROR_NO_SUB_DOMAIN, domain)).withWidthUndefined();

        Button backToHome = new Button(UserUIContext.getMessage(ShellI18nEnum.BUTTON_BACK_TO_HOME_PAGE),
                clickEvent -> getUI().getPage().setLocation("https://www.mycollab.com"));
        backToHome.addStyleName(WebThemes.BUTTON_ACTION);
        this.with(titleIcon, warningMsg, backToHome);
    }
}