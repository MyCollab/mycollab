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
package com.esofthead.mycollab.vaadin.ui;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class AddViewLayout2 extends CssLayout {
    private static final long serialVersionUID = 1L;

    private final HorizontalLayout header;
    private final Image iconEmbed;
    private final Label titleLbl;
    private final VerticalLayout body;

    public AddViewLayout2(final String title, final Resource icon) {
        setStyleName("addview-layout");

        header = new HorizontalLayout();
        header.setWidth("100%");
        header.setSpacing(true);
        header.setMargin(true);
        header.setStyleName("addview-layout-header");
        header.setHeight("39px");
        this.addComponent(header);

        iconEmbed = new Image(null, icon);
        header.addComponent(iconEmbed);
        header.setComponentAlignment(iconEmbed, Alignment.MIDDLE_LEFT);

        titleLbl = new Label(title);
        titleLbl.setContentMode(ContentMode.HTML);
        titleLbl.setStyleName("h2");
        titleLbl.setWidth("100%");
        header.addComponent(titleLbl);
        header.setComponentAlignment(titleLbl, Alignment.MIDDLE_LEFT);
        header.setExpandRatio(titleLbl, 1.0f);

        body = new VerticalLayout();
        body.setStyleName("addview-layout-body");
        this.addComponent(body);
    }

    public void addBody(final ComponentContainer body) {
        this.body.addComponent(body);
        this.body.setExpandRatio(body, 1.0f);
    }

    public VerticalLayout getBody() {
        return this.body;
    }

    public void addControlButtons(final Component controlsBtn) {
        controlsBtn.addStyleName("control-buttons");
        body.addComponent(controlsBtn);
    }

    public void setTitle(final String title) {
        titleLbl.setValue(title);
    }
}
