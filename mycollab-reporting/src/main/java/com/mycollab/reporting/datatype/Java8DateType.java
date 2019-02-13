package com.mycollab.reporting.datatype;

import net.sf.dynamicreports.report.base.datatype.AbstractDataType;
import net.sf.dynamicreports.report.defaults.Defaults;
import net.sf.dynamicreports.report.exception.DRException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
public class Java8DateType  extends AbstractDataType<LocalDate, LocalDate> {
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
    public String valueToString(LocalDate value, Locale locale) {
        if (value != null) {
            return DateTimeFormatter.ofPattern(getPattern()).withLocale(locale).format(value);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate stringToValue(String value, Locale locale) throws DRException {
        if (value != null) {
            return LocalDate.parse(value, DateTimeFormatter.ofPattern(getPattern()).withLocale(locale));
        }
        return null;
    }
}
