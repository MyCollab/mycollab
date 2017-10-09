package com.mycollab.vaadin.ui;

import com.mycollab.common.domain.OptionVal;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.ComboBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class OptionValComboBox extends ComboBox {
    private Class<? extends Enum> enumCls;
    private List<OptionVal> options = new ArrayList<>();

    public OptionValComboBox(Class<? extends Enum> enumCls) {
        super();
        this.setPageLength(20);
        this.setNullSelectionAllowed(false);
        this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
        this.enumCls = enumCls;

        this.setItemStyleGenerator((source, itemId) -> {
            OptionVal option = (OptionVal) itemId;
            if (option != null) {
                return "" + option.hashCode();
            }
            return null;
        });
        this.setConverter(new StringToOptionConverter());
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
            this.addItem(option);
            this.setItemCaption(option, StringUtils.trim(UserUIContext.getMessage(anEnum), 25, true));
        } catch (Exception e) {
            this.addItem(option);
            this.setItemCaption(option, StringUtils.trim(value, 25, true));
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
