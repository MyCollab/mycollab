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
package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.TaskPriority;
import com.esofthead.mycollab.vaadin.ui.I18nValueComboBox;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.ComboBox;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TaskPriorityComboBox extends I18nValueComboBox {
    private static final long serialVersionUID = 1L;

    public TaskPriorityComboBox() {
        this.setNullSelectionAllowed(false);
        this.setWidth("150px");

        this.loadData(Arrays.asList(TaskPriority.Urgent, TaskPriority.High,
                TaskPriority.Medium, TaskPriority.Low, TaskPriority.None));

        this.setItemIcon(TaskPriority.Urgent.name(), FontAwesome.ARROW_UP);
        this.setItemIcon(TaskPriority.High.name(), FontAwesome.ARROW_UP);
        this.setItemIcon(TaskPriority.Medium.name(), FontAwesome.ARROW_UP);
        this.setItemIcon(TaskPriority.Low.name(), FontAwesome.ARROW_DOWN);
        this.setItemIcon(TaskPriority.None.name(), FontAwesome.ARROW_DOWN);

        this.setItemStyleGenerator(new ItemStyleGenerator() {
            @Override
            public String getStyle(ComboBox source, Object itemId) {
                if (itemId != null) {
                    return String.format("task-%s", itemId.toString().toLowerCase());
                }
                return null;
            }
        });

        this.setValue(this.getItemIds().iterator().next());
    }
}
