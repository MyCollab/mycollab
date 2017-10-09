package com.mycollab.vaadin.web.ui.field.converter;

import com.vaadin.data.util.converter.Converter;
import org.joda.time.LocalDate;

import java.util.Date;
import java.util.Locale;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public class LocalDateConverter implements Converter<Date, LocalDate> {
    @Override
    public LocalDate convertToModel(Date value, Class<? extends LocalDate> targetType, Locale locale) throws ConversionException {
        return new LocalDate(value.getTime());
    }

    @Override
    public Date convertToPresentation(LocalDate value, Class<? extends Date> targetType, Locale locale) throws ConversionException {
        return value.toDate();
    }

    @Override
    public Class<LocalDate> getModelType() {
        return LocalDate.class;
    }

    @Override
    public Class<Date> getPresentationType() {
        return Date.class;
    }
}
