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
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ItemCaptionGenerator;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author MyCollab Ltd.
 * @since 4.3.0
 */
public class I18nValueComboBox<T extends Enum<T>> extends ComboBox<T> implements Converter<T, String> {
    private static final long serialVersionUID = 1L;

    private Class<T> eClass;

    public I18nValueComboBox(Class<T> eClass) {
        this(eClass, true);
    }

    public I18nValueComboBox(Class<T> eClass, T... keys) {
        this(eClass, false, keys);
    }

    public I18nValueComboBox(Class<T> eClass, boolean nullIsAllowable, T... keys) {
        this.eClass = eClass;
        setEmptySelectionAllowed(nullIsAllowable);
        loadData(Arrays.asList(keys));
    }

    public final void loadData(Collection<T> values) {
        this.setItems(values);
        this.setItemCaptionGenerator((ItemCaptionGenerator<T>) o -> UserUIContext.getMessage(o));
    }

    public void setValueByString(String value) {
        T tValue = Enum.valueOf(eClass, value);
        setValue(tValue);
    }

    public String getSelectedValueByString() {
        T value = getValue();
        return UserUIContext.getMessage(value);
    }

    public I18nValueComboBox<T> withWidth(String width) {
        setWidth(width);
        return this;
    }

    @Override
    public Result<String> convertToModel(T value, ValueContext context) {
        return (value != null) ? Result.ok(value.name()) : Result.ok(null);
    }

    @Override
    public T convertToPresentation(String value, ValueContext context) {
        if (value == null) {
            return getValue();
        }
        return Enum.valueOf(eClass, value);
    }
}