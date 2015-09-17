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

import com.esofthead.mycollab.vaadin.AppContext;

import java.util.Arrays;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.3.0
 */
public class I18nValueComboBox extends StyleComboBox {
    private static final long serialVersionUID = 1L;

    public I18nValueComboBox() {
        super();
        this.setPageLength(20);
    }

    public I18nValueComboBox(boolean nullIsAllowable, Enum<?>... keys) {
        this();
        setNullSelectionAllowed(nullIsAllowable);
        loadData(Arrays.asList(keys));
    }

    public final void loadData(List<? extends Enum<?>> values) {
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);

        for (Enum<?> entry : values) {
            this.addItem(entry.name());
            this.setItemCaption(entry.name(), AppContext.getMessage(entry));
        }

        if (!this.isNullSelectionAllowed()) {
            this.select(this.getItemIds().iterator().next());
        }
    }
}