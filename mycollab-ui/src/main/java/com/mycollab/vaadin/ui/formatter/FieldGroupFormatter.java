package com.mycollab.vaadin.ui.formatter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
public class FieldGroupFormatter {

    private static Map<String, HistoryFieldFormat> defaultFieldHandlers;

    public static final String DEFAULT_FIELD = "default";
    public static final String DATE_FIELD = "date";
    public static final String DATETIME_FIELD = "datetime";
    public static final String PRETTY_DATE_FIELD = "prettydate";
    public static final String PRETTY_DATE_TIME_FIELD = "prettydatetime";
    public static final String CURRENCY_FIELD = "currency";
    public static final String TRIM_HTMLS = "trim_htmls";

    protected Map<String, DefaultFieldDisplayHandler> fieldsFormat = new HashMap<>();

    static {
        defaultFieldHandlers = new HashMap<>();
        defaultFieldHandlers.put(DEFAULT_FIELD, new DefaultHistoryFieldFormat());
        defaultFieldHandlers.put(DATE_FIELD, new DateHistoryFieldFormat());
        defaultFieldHandlers.put(DATETIME_FIELD, new DateTimeHistoryFieldFormat());
        defaultFieldHandlers.put(PRETTY_DATE_FIELD, new PrettyDateHistoryFieldFormat());
        defaultFieldHandlers.put(PRETTY_DATE_TIME_FIELD, new PrettyDateTimeHistoryFieldFormat());
        defaultFieldHandlers.put(CURRENCY_FIELD, new CurrencyHistoryFieldFormat());
        defaultFieldHandlers.put(TRIM_HTMLS, new TrimHtmlHistoryFieldFormat());
    }

    public void generateFieldDisplayHandler(String fieldName, Enum displayName) {
        fieldsFormat.put(fieldName, new DefaultFieldDisplayHandler(displayName));
    }

    public void generateFieldDisplayHandler(String fieldName, Enum displayName, HistoryFieldFormat format) {
        fieldsFormat.put(fieldName, new DefaultFieldDisplayHandler(displayName, format));
    }

    public void generateFieldDisplayHandler(String fieldName, Enum displayName, String formatName) {
        fieldsFormat.put(fieldName, new DefaultFieldDisplayHandler(displayName, defaultFieldHandlers.get(formatName)));
    }

    public DefaultFieldDisplayHandler getFieldDisplayHandler(String fieldName) {
        return fieldsFormat.get(fieldName);
    }

}