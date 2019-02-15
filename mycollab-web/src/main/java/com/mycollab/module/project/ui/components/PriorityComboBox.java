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
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.IconGenerator;
import com.vaadin.ui.StyleGenerator;

import static com.mycollab.module.project.i18n.OptionI18nEnum.Priority.*;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class PriorityComboBox extends I18nValueComboBox<Priority> {
    private static final long serialVersionUID = 1L;

    public PriorityComboBox() {
        super(Priority.class, Urgent, High, Medium, Low, None);
        this.setWidth(WebThemes.FORM_CONTROL_WIDTH);
        this.setItemIconGenerator((IconGenerator<Priority>) item -> {
            if (item == Urgent || item == High || item == Medium) {
                return VaadinIcons.ARROW_UP;
            } else {
                return VaadinIcons.ARROW_DOWN;
            }
        });
        this.setStyleGenerator((StyleGenerator<Priority>) itemId -> String.format("priority-%s", itemId.name().toLowerCase()));
        this.setValue(Medium);
    }
}
