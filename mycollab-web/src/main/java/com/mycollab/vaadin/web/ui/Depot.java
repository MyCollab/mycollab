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
package com.mycollab.vaadin.web.ui;

import com.vaadin.ui.*;
import fi.jasoft.dragdroplayouts.DDVerticalLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class Depot extends DDVerticalLayout {
    private static final long serialVersionUID = 1L;

    private boolean isOpened = true;
    protected MHorizontalLayout header;
    protected Label headerLbl;
    protected MHorizontalLayout headerContent;
    protected ComponentContainer bodyContent;

    public Depot(String title, ComponentContainer content) {
        this.addStyleName("depotComp");
        header = new MHorizontalLayout().withHeight("40px").withStyleName("depotHeader");
        bodyContent = content;
        bodyContent.setWidth("100%");
        headerContent = new MHorizontalLayout().withFullHeight().withWidthUndefined().withVisible(false);
        this.addComponent(header);

        headerLbl = new Label(title);
        final MHorizontalLayout headerLeft = new MHorizontalLayout(headerLbl).withStyleName("depot-title")
                .withAlign(headerLbl, Alignment.MIDDLE_LEFT).withFullWidth();
        headerLeft.addLayoutClickListener(layoutClickEvent -> {
            isOpened = !isOpened;
            if (isOpened) {
                bodyContent.setVisible(true);
                removeStyleName("collapsed");
                header.removeStyleName("border-bottom");
            } else {
                bodyContent.setVisible(false);
                addStyleName("collapsed");
                header.addStyleName("border-bottom");
            }
        });
        header.with(headerLeft, headerContent).withAlign(headerLeft, Alignment.MIDDLE_LEFT).withAlign(headerContent,
                Alignment.MIDDLE_RIGHT).expand(headerLeft);

        bodyContent.addStyleName("depotContent");
        this.addComponent(bodyContent);
    }

    public void addHeaderElement(final Component component) {
        if (component != null) {
            headerContent.with(component).withAlign(component, Alignment.MIDDLE_RIGHT);
            headerContent.setVisible(true);
        }
    }

    public void setContentBorder(final boolean hasBorder) {
        if (hasBorder) {
            bodyContent.addStyleName("bordered");
        } else {
            bodyContent.removeStyleName("bordered");
        }
    }

    public void setTitle(final String title) {
        headerLbl.setValue(title);
    }

    public ComponentContainer getContent() {
        return bodyContent;
    }
}
