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

import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

import java.util.Set;

/**
 * @author MyCollab Ltd
 * @since 5.2.1
 */
public abstract class WrappedFormLayoutFactory implements IFormLayoutFactory {
    protected IFormLayoutFactory wrappedLayoutFactory;

    IFormLayoutFactory getWrappedFactory() {
        return wrappedLayoutFactory;
    }

    @Override
    public final Component attachField(Object propertyId, Field<?> field) {
        return wrappedLayoutFactory.attachField(propertyId, field);
    }

    @Override
    public final Set<String> bindFields() {
        return wrappedLayoutFactory.bindFields();
    }
}
