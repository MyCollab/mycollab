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
package com.mycollab.mobile.ui.grid;

import com.vaadin.ui.*;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
class GridCellWrapper extends CssLayout {
    private static final long serialVersionUID = 1L;

    public GridCellWrapper() {
        this.setStyleName("gridform-field");
        this.setWidth("100%");
    }

    public void addComponent(Component component) {
        if (!(component instanceof Button))
            component.setCaption(null);

        if (component instanceof AbstractTextField || component instanceof RichTextArea) {
            component.setWidth("100%");
        }
        super.addComponent(component);
    }
}
