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
package com.esofthead.mycollab.web;

import com.esofthead.mycollab.license.LicenseResolver;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AbstractLicenseActivationWindow;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.springframework.web.client.RestTemplate;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
public class AdWindow extends Window {
    public AdWindow() {
        super("Buy MyCollab Pro edition");
        this.setWidth("700px");
        this.setModal(true);
        this.setResizable(false);
        RestTemplate restTemplate = new RestTemplate();
        MVerticalLayout content = new MVerticalLayout();
        try {
            String result = restTemplate.getForObject("https://api.mycollab.com/api/storeweb", String.class);
            Label webPage = new Label(result, ContentMode.HTML);
            webPage.setHeight("600px");
            this.setContent(content.with(webPage).withAlign(webPage, Alignment.TOP_CENTER));
        } catch (Exception e) {
            Div informDiv = new Div().appendText("Can not load the store page. You can check the online edition at ")
                    .appendChild(new A("https://www.mycollab.com/pricing/download/", "_blank").appendText("here"));
            Label webPage = new Label(informDiv.write(), ContentMode.HTML);
            this.setContent(content.with(webPage).withAlign(webPage, Alignment.TOP_CENTER));
        }
        LicenseResolver licenseResolver = AppContextUtil.getSpringBean(LicenseResolver.class);
        if (licenseResolver != null) {
            Button editLicenseBtn = new Button("Enter license code", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    Window activateWindow = ViewManager.getCacheComponent(AbstractLicenseActivationWindow.class);
                    UI.getCurrent().addWindow(activateWindow);
                    close();
                }
            });
            editLicenseBtn.addStyleName(UIConstants.BUTTON_ACTION);
            content.with(editLicenseBtn).withAlign(editLicenseBtn, Alignment.MIDDLE_CENTER);
        }
    }
}
