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
package com.esofthead.mycollab.vaadin.ui.grid;

import com.esofthead.mycollab.vaadin.ui.MultiSelectComp;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.0.8
 */
class GridCellWrapper extends HorizontalLayout {
    private static final long serialVersionUID = 1L;

    public GridCellWrapper() {
        this.setStyleName("gridform-field");
        this.setMargin(true);
        this.setWidth("100%");
    }

    public void addComponent(Component component) {
        if (!(component instanceof Button))
            component.setCaption(null);

        if (component instanceof MultiSelectComp) {
            component.setWidth("200px");
        } else {
            component.setWidth("100%");
        }
        super.addComponent(component);
    }
}
