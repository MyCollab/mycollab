package com.mycollab.community.shell.view;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.mycollab.common.i18n.LicenseI18nEnum;
import com.mycollab.configuration.ServerConfiguration;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import org.springframework.web.client.RestTemplate;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
class AdWindow extends MWindow {
    AdWindow() {
        super(UserUIContext.getMessage(LicenseI18nEnum.OPT_TRIAL_THE_PRO_EDITION));
        this.withModal(true).withResizable(false).withWidth("1000px");
        RestTemplate restTemplate = new RestTemplate();
        MVerticalLayout content = new MVerticalLayout();
        try {
            ServerConfiguration serverConfiguration = AppContextUtil.getSpringBean(ServerConfiguration.class);
            String result = restTemplate.getForObject(serverConfiguration.getApiUrl("storeweb"), String.class);
            ELabel webPage = ELabel.html(result);
            webPage.setHeight("600px");
            this.setContent(content.with(webPage).withAlign(webPage, Alignment.TOP_CENTER));
        } catch (Exception e) {
            Div informDiv = new Div().appendText("Can not load the store page. You can check the online edition at ")
                    .appendChild(new A("https://www.mycollab.com/pricing/download/", "_blank").appendText("here"));
            Label webPage = new Label(informDiv.write(), ContentMode.HTML);
            this.setContent(content.with(webPage).withAlign(webPage, Alignment.TOP_CENTER));
        }
    }
}
