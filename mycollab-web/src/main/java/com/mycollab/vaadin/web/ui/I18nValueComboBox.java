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
package com.mycollab.vaadin.web.ui;

import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.ItemCaptionGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.3.0
 */
public class I18nValueComboBox extends ValueComboBox {
    private static final long serialVersionUID = 1L;

    public I18nValueComboBox() {
        super();
    }

    public I18nValueComboBox(boolean nullIsAllowable, Enum<?>... keys) {
        this();
        setEmptySelectionAllowed(nullIsAllowable);
        loadData(Arrays.asList(keys));
    }

    public final void loadData(List<? extends Enum<?>> values) {
        if (values.size() > 0) {
            this.setItems(values.stream().map(it-> it.name()));
//            this.setItemCaptionGenerator((ItemCaptionGenerator<Enum<?>>) o -> UserUIContext.getMessage(o));
        }
    }
}