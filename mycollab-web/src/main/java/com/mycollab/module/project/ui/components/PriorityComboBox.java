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
package com.mycollab.module.project.ui.components;

import com.mycollab.module.project.i18n.OptionI18nEnum.Priority;
import com.mycollab.vaadin.web.ui.I18nValueComboBox;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.IconGenerator;
import com.vaadin.ui.StyleGenerator;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
// TODO
public class PriorityComboBox extends I18nValueComboBox {
    private static final long serialVersionUID = 1L;

    public PriorityComboBox(boolean nullIsAllowable, Enum<?>... keys) {
        super(nullIsAllowable, keys);
    }

    public PriorityComboBox() {
        this.setEmptySelectionAllowed(false);
        this.setWidth("150px");

        this.loadData(Arrays.asList(Priority.Urgent, Priority.High, Priority.Medium, Priority.Low, Priority.None));
        this.setItemIconGenerator((IconGenerator<String>) item -> {
            if (item.equals(Priority.Urgent.name()) || item.equals(Priority.High.name())
                    || item.equals(Priority.Medium.name())) {
                return VaadinIcons.ARROW_UP;
            } else {
                return VaadinIcons.ARROW_DOWN;
            }
        });
        this.setStyleGenerator((StyleGenerator<String>) itemId -> String.format("task-%s", itemId.toLowerCase()));
    }

//    @Override
//    public void setPropertyDataSource(Property newDataSource) {
//        Object value = newDataSource.getValue();
//        if (value == null) {
//            newDataSource.setValue(Priority.Medium.name());
//        }
//        super.setPropertyDataSource(newDataSource);
//    }
}
