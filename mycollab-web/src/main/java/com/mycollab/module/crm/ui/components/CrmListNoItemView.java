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
package com.mycollab.module.crm.ui.components;

import com.mycollab.vaadin.mvp.AbstractPageView;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public abstract class CrmListNoItemView extends AbstractPageView {
    public CrmListNoItemView() {
        MVerticalLayout layout = new MVerticalLayout().withWidth("800px");
        layout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        ELabel image = ELabel.h2(titleIcon().getHtml()).withWidthUndefined();
        layout.with(image);

        ELabel title = ELabel.h2(titleMessage()).withWidthUndefined();

        Label hintLabel = new Label(hintMessage());
        hintLabel.setWidthUndefined();

        Button btCreateContact = new Button(actionMessage(), actionListener());
        btCreateContact.setEnabled(hasPermission());

        MHorizontalLayout links = new MHorizontalLayout();

        links.addComponent(btCreateContact);
        btCreateContact.addStyleName(UIConstants.BUTTON_ACTION);

        layout.with(image, title, hintLabel, links);
        this.with(layout).withAlign(layout, Alignment.TOP_CENTER);
    }

    abstract protected FontAwesome titleIcon();

    abstract protected String titleMessage();

    abstract protected String hintMessage();

    abstract protected String actionMessage();

    abstract protected Button.ClickListener actionListener();

    abstract protected boolean hasPermission();
}
