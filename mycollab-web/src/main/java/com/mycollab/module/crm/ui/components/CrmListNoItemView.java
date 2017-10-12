/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.ui.components;

import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public abstract class CrmListNoItemView extends AbstractVerticalPageView {
    public CrmListNoItemView() {
        ELabel image = ELabel.h2(titleIcon().getHtml()).withWidthUndefined();
        ELabel title = ELabel.h2(titleMessage()).withWidthUndefined();
        ELabel hintLabel = new ELabel(hintMessage()).withWidthUndefined();

        MButton createItemBtn = new MButton(actionMessage(), actionListener()).withStyleName(WebThemes.BUTTON_ACTION)
                .withVisible(hasPermission());
        MHorizontalLayout links = new MHorizontalLayout(createItemBtn);
        MVerticalLayout layout = new MVerticalLayout(image, title, hintLabel, links).withWidth("800px").alignAll(Alignment.TOP_CENTER);
        this.with(layout).withAlign(layout, Alignment.TOP_CENTER);
    }

    abstract protected FontAwesome titleIcon();

    abstract protected String titleMessage();

    abstract protected String hintMessage();

    abstract protected String actionMessage();

    abstract protected Button.ClickListener actionListener();

    abstract protected boolean hasPermission();
}
