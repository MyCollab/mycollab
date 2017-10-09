package com.mycollab.vaadin.web.ui;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.ServerConfiguration;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractSingleContainerPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.web.client.RestTemplate;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class NotPresentedView extends AbstractSingleContainerPageView {
    private static final long serialVersionUID = 1L;

    public NotPresentedView() {
        MVerticalLayout bodyLayout = new MVerticalLayout().withMargin(false);
        setContent(bodyLayout);
        bodyLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        final ELabel titleIcon = ELabel.fontIcon(FontAwesome.EXCLAMATION_CIRCLE).withStyleName("warning-icon", ValoTheme.LABEL_NO_MARGIN);
        titleIcon.setWidthUndefined();
        bodyLayout.with(titleIcon);

        Label label = ELabel.h2(UserUIContext.getMessage(GenericI18Enum.NOTIFICATION_FEATURE_NOT_AVAILABLE_IN_VERSION)).withWidthUndefined();
        bodyLayout.with(label).withAlign(label, Alignment.MIDDLE_CENTER);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ServerConfiguration serverConfiguration = AppContextUtil.getSpringBean(ServerConfiguration.class);
            String result = restTemplate.getForObject(serverConfiguration.getApiUrl("storeweb"), String.class);
            ELabel webPage = ELabel.html(result);
            webPage.setHeight("480px");
            bodyLayout.with(new MVerticalLayout(webPage).withMargin(false).withAlign(webPage, Alignment.TOP_CENTER));
        } catch (Exception e) {
            Div informDiv = new Div().appendText("Can not load the store page. You can check the online edition at ")
                    .appendChild(new A("https://www.mycollab.com/pricing/download/", "_blank").appendText("here"));
            ELabel webPage = ELabel.html(informDiv.write()).withWidthUndefined();
            bodyLayout.with(new MVerticalLayout(webPage).withAlign(webPage, Alignment.TOP_CENTER));
        }
    }
}
