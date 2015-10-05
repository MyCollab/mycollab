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
package com.esofthead.mycollab.vaadin.ui;

import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.ComboBox;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class MixValueComboBox extends ComboBox {
    private Class<? extends Enum> enumCls;

    public MixValueComboBox(Class<? extends Enum> enumCls) {
        super();
        this.setPageLength(20);
        this.setNullSelectionAllowed(false);
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
        this.enumCls = enumCls;
    }

    public final void addEntry(String value) {
        try {
            Enum anEnum = Enum.valueOf(enumCls, value);
            this.addItem(value);
            this.setItemCaption(value, StringUtils.trim(AppContext.getMessage(anEnum), 25, true));
        } catch (Exception e) {
            this.addItem(value);
            this.setItemCaption(value, value);
        }
    }
}
