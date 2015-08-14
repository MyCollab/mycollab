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

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class AddViewLayout2 extends VerticalLayout {
    private static final long serialVersionUID = 1L;

    private MHorizontalLayout header;
    private Label titleLbl;
    private Resource viewIcon;
    private MVerticalLayout body;

    public AddViewLayout2(String title, Resource icon) {
        setStyleName("addview-layout");

        this.viewIcon = icon;

        this.header = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false)).withWidth("100%")
                .withStyleName(UIConstants.HEADER_VIEW);
        this.header.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        if (!(icon instanceof FontAwesome)) {
            Image iconEmbed = new Image();
            iconEmbed.setSource(icon);
            this.header.with(iconEmbed);
        }

        this.titleLbl = new Label("", ContentMode.HTML);
        this.titleLbl.setStyleName(UIConstants.HEADER_TEXT);
        this.header.with(this.titleLbl).expand(titleLbl);

        if (title == null) {
            if (icon != null) {
                this.setTitle("Undefined");
            }
        } else {
            this.setTitle(title);
        }

        this.addComponent(header);

        body = new MVerticalLayout().withSpacing(false).withMargin(false).withStyleName("addview-layout-body");
        this.addComponent(body);
    }

    public void resetTitleStyle() {
        this.titleLbl.setStyleName(UIConstants.HEADER_TEXT);
    }

    public void setTitleStyleName(String style) {
        this.titleLbl.setStyleName(style);
    }

    public void addBody(ComponentContainer bodyContainer) {
        this.body.with(bodyContainer).expand(bodyContainer);
    }

    public VerticalLayout getBody() {
        return this.body;
    }

    public void addControlButtons(Component controlsBtn) {
        controlsBtn.addStyleName("control-buttons");
        addHeaderRight(controlsBtn);
    }

    public void setTitle(String viewTitle) {
        if (viewIcon instanceof FontAwesome) {
            String title = ((FontAwesome) viewIcon).getHtml() + " " + viewTitle;
            this.titleLbl.setValue(title);
        } else {
            this.titleLbl.setValue(viewTitle);
        }
    }

    public void addHeaderRight(Component headerRight) {
        this.header.addComponent(headerRight);
    }
}
