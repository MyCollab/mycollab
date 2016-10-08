/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui;

import java.util.EventObject;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public class PropertyChangedEvent  extends EventObject {
    private String bindProperty;

    public PropertyChangedEvent(Object source, String bindProperty) {
        super(source);
        this.bindProperty = bindProperty;
    }

    public String getBindProperty() {
        return bindProperty;
    }
}
