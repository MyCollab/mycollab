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
package com.mycollab.web;

import com.mycollab.core.utils.FileUtils;
import com.mycollab.license.LicenseResolver;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AbstractLicenseActivationWindow;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.springframework.web.client.RestTemplate;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author myCollab Ltd
 * @since 5.3.2
 */
public class BuyPremiumSoftwareWindow extends Window {
    public BuyPremiumSoftwareWindow() {
        super("Buy MyCollab Pro edition");
        this.setWidth("700px");
        this.setModal(true);
        this.setResizable(false);
        RestTemplate restTemplate = new RestTemplate();
        MVerticalLayout content = new MVerticalLayout();
        try {
            String result = restTemplate.getForObject("https://api.mycollab.com/api/linktobuy", String.class);
            Label webPage = new Label(result, ContentMode.HTML);
            webPage.setHeight("600px");
            this.setContent(content.with(webPage).withAlign(webPage, Alignment.TOP_CENTER));
        } catch (Exception e) {
            String result = FileUtils.readFileAsPlainString("buying.html");
            Label webPage = new Label(result, ContentMode.HTML);
            this.setContent(content.with(webPage).withAlign(webPage, Alignment.TOP_CENTER));
        }
        LicenseResolver licenseResolver = AppContextUtil.getSpringBean(LicenseResolver.class);
        if (licenseResolver != null) {
            MButton editLicenseBtn = new MButton("Enter license code", clickEvent -> {
                Window activateWindow = ViewManager.getCacheComponent(AbstractLicenseActivationWindow.class);
                UI.getCurrent().addWindow(activateWindow);
                close();
            }).withStyleName(UIConstants.BUTTON_ACTION);
            content.with(editLicenseBtn).withAlign(editLicenseBtn, Alignment.MIDDLE_CENTER);
        }
    }
}
