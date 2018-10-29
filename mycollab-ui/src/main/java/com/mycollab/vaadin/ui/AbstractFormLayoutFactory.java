/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class AbstractFormLayoutFactory implements IFormLayoutFactory {
    private Set<String> fields = new HashSet<>();

    public abstract AbstractComponent getLayout();

    protected abstract HasValue<?> onAttachField(Object propertyId, HasValue<?> field);

    public HasValue<?> attachField(Object propertyId, HasValue<?> field) {
        HasValue component = onAttachField(propertyId, field);
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
