/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.ui;

import com.mycollab.mobile.ui.I18nValueComboBox;
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public class PriorityComboBox extends I18nValueComboBox {
    private static final long serialVersionUID = 5484692572022056722L;

    public PriorityComboBox() {
        this.setNullSelectionAllowed(false);

        this.loadData(Arrays.asList(Priority.Urgent, Priority.High,
                Priority.Medium, Priority.Low, Priority.None));
        this.setValue(this.getItemIds().iterator().next());
    }
}
