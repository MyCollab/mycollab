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
package com.mycollab.vaadin.web.ui.grid;

import com.mycollab.vaadin.web.ui.MultiSelectComp;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.RichTextArea;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.0.8
 */
class GridCellWrapper extends MHorizontalLayout {
    private static final long serialVersionUID = 1L;

    GridCellWrapper() {
        this.withMargin(true).withFullWidth().withStyleName("gridform-field");
    }

    public void addComponent(Component component) {
        if (!(component instanceof Button))
            component.setCaption(null);

        if (component instanceof MultiSelectComp) {
            component.setWidth("200px");
        } else if (component instanceof AbstractTextField || component instanceof RichTextArea) {
            component.setWidth("100%");
        }

        super.addComponent(component);
    }
}