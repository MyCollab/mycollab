package com.mycollab.reporting.datatype;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.exception.DRException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Java8DateTimeType extends AbstractDataType<LocalDateTime, LocalDateTime> {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPattern() {
        return Defaults.getDefaults().getDateType().getPattern();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String valueToString(LocalDateTime value, Locale locale) {
        if (value != null) {
            return DateTimeFormatter.ofPattern(getPattern()).withLocale(locale).format(value);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime stringToValue(String value, Locale locale) throws DRException {
        if (value != null) {
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(getPattern()).withLocale(locale));
        }
        return null;
    }
}
