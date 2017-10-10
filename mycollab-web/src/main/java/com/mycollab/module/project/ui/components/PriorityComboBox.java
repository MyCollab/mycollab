/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.ui.components;

import com.mycollab.module.project.i18n.OptionI18nEnum.Priority;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.ComboBox;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class PriorityComboBox extends I18nValueComboBox {
    private static final long serialVersionUID = 1L;

    public PriorityComboBox() {
        this.setNullSelectionAllowed(false);
        this.setWidth("150px");

        this.loadData(Arrays.asList(Priority.Urgent, Priority.High, Priority.Medium, Priority.Low, Priority.None));

        this.setItemIcon(Priority.Urgent.name(), FontAwesome.ARROW_UP);
        this.setItemIcon(Priority.High.name(), FontAwesome.ARROW_UP);
        this.setItemIcon(Priority.Medium.name(), FontAwesome.ARROW_UP);
        this.setItemIcon(Priority.Low.name(), FontAwesome.ARROW_DOWN);
        this.setItemIcon(Priority.None.name(), FontAwesome.ARROW_DOWN);

        this.setItemStyleGenerator((source, itemId) -> {
            if (itemId != null) {
                return String.format("task-%s", itemId.toString().toLowerCase());
            }
            return null;
        });
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        Object value = newDataSource.getValue();
        if (value == null) {
            newDataSource.setValue(Priority.Medium.name());
        }
        super.setPropertyDataSource(newDataSource);
    }
}
