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
package com.mycollab.vaadin.web.ui;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.ServerConfiguration;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.AbstractSingleContainerPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.icons.VaadinIcons;
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

        final ELabel titleIcon = ELabel.fontIcon(VaadinIcons.EXCLAMATION_CIRCLE).withStyleName("warning-icon", ValoTheme.LABEL_NO_MARGIN);
        titleIcon.setWidthUndefined();
        bodyLayout.with(titleIcon);

        Label label = ELabel.h2(UserUIContext.getMessage(GenericI18Enum.NOTIFICATION_FEATURE_NOT_AVAILABLE_IN_VERSION)).withUndefinedWidth();
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
            ELabel webPage = ELabel.html(informDiv.write()).withUndefinedWidth();
            bodyLayout.with(new MVerticalLayout(webPage).withAlign(webPage, Alignment.TOP_CENTER));
        }
    }
}
