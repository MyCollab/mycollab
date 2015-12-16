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

import com.esofthead.mycollab.common.domain.OptionVal;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class OptionValComboBox extends CustomField {
    private Class<? extends Enum> enumCls;
    private ComboBox valComboBox;
    private List<OptionVal> options = new ArrayList<>();

    public OptionValComboBox(Class<? extends Enum> enumCls) {
        super();
        valComboBox = new ComboBox();
        valComboBox.setPageLength(20);
        valComboBox.setNullSelectionAllowed(false);
        valComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
        this.enumCls = enumCls;

        valComboBox.setItemStyleGenerator(new ComboBox.ItemStyleGenerator() {
            @Override
            public String getStyle(ComboBox source, Object itemId) {
                OptionVal option = (OptionVal) itemId;
                if (option != null) {
                    return "" + option.hashCode();
                }
                return null;
            }
        });
        valComboBox.setConverter(new StringToOptionConverter());
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        valComboBox.setPropertyDataSource(newDataSource);
    }

    @Override
    public Property getPropertyDataSource() {
        return valComboBox.getPropertyDataSource();
    }

    @Override
    protected Component initContent() {
        return valComboBox;
    }

    @Override
    public Class getType() {
        return Object.class;
    }

    public final void addEntry(OptionVal option) {
        options.add(option);
        String value = option.getTypeval();
        try {
            Enum anEnum = Enum.valueOf(enumCls, value);
            valComboBox.addItem(option);
            valComboBox.setItemCaption(option, StringUtils.trim(AppContext.getMessage(anEnum), 25, true));
        } catch (Exception e) {
            valComboBox.addItem(option);
            valComboBox.setItemCaption(option, StringUtils.trim(value, 25, true));
        }
    }

    private class StringToOptionConverter implements Converter<Object, String> {
        @Override
        public String convertToModel(Object value, Class<? extends String> targetType, Locale locale) throws ConversionException {
            if (value != null) {
                return ((OptionVal) value).getTypeval();
            }
            return "";
        }

        @Override
        public Object convertToPresentation(String value, Class<?> targetType, Locale locale) throws ConversionException {
            for (OptionVal optionVal : options) {
                if (optionVal.getTypeval().equals(value)) {
                    return optionVal;
                }
            }
            return null;
        }

        @Override
        public Class<String> getModelType() {
            return String.class;
        }

        @Override
        public Class<Object> getPresentationType() {
            return Object.class;
        }
    }
}
