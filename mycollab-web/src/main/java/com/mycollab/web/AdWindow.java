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

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
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
public class AdWindow extends MWindow {
    public AdWindow() {
        super("Buy MyCollab Pro edition");
        this.withModal(true).withResizable(false).withWidth("700px");
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
    }
}
