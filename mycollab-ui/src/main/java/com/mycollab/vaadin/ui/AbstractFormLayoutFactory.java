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

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class AbstractFormLayoutFactory implements IFormLayoutFactory {
    private Set<String> fields = new HashSet<>();

    public abstract AbstractComponent getLayout();

    protected abstract Component onAttachField(Object propertyId, Field<?> field);

    public Component attachField(Object propertyId, Field<?> field) {
        Component component = onAttachField(propertyId, field);
        if (component != null) {
            fields.add((String) propertyId);
            return component;
        }
        return null;
    }

    public Set<String> bindFields() {
        return fields;
    }
}
