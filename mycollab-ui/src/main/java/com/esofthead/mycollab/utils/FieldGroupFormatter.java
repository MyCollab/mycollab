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
package com.esofthead.mycollab.utils;

import com.esofthead.mycollab.common.domain.AuditChangeItem;
import com.esofthead.mycollab.common.domain.Currency;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.CurrencyService;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.hp.gagawa.java.elements.Li;
import com.hp.gagawa.java.elements.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
public class FieldGroupFormatter {
    private static final Logger LOG = LoggerFactory.getLogger(FieldGroupFormatter.class);

    private static Map<String, HistoryFieldFormat> defaultFieldHandlers;

    public static final String DEFAULT_FIELD = "default";
    public static final String DATE_FIELD = "date";
    public static final String DATETIME_FIELD = "datetime";
    public static final String PRETTY_DATE_FIELD = "prettydate";
    public static final String PRETTY_DATE_TIME_FIELD = "prettydatetime";
    public static final String CURRENCY_FIELD = "currency";
    public static final String TRIM_HTMLS = "trim_htmls";

    protected Map<String, FieldDisplayHandler> fieldsFormat = new HashMap<>();

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
        fieldsFormat.put(fieldName, new FieldDisplayHandler(displayName));
    }

    public void generateFieldDisplayHandler(String fieldName, Enum displayName, HistoryFieldFormat format) {
        fieldsFormat.put(fieldName, new FieldDisplayHandler(displayName, format));
    }

    public void generateFieldDisplayHandler(String fieldName, Enum displayName, String formatName) {
        fieldsFormat.put(fieldName, new FieldDisplayHandler(displayName, defaultFieldHandlers.get(formatName)));
    }

    public FieldDisplayHandler getFieldDisplayHandler(String fieldName) {
        return fieldsFormat.get(fieldName);
    }

    public static class FieldDisplayHandler {
        private Enum displayName;
        private HistoryFieldFormat format;

        public FieldDisplayHandler(Enum displayName) {
            this(displayName, new DefaultHistoryFieldFormat());
        }

        public FieldDisplayHandler(Enum displayName, HistoryFieldFormat format) {
            this.displayName = displayName;
            this.format = format;
        }

        public Enum getDisplayName() {
            return displayName;
        }

        public HistoryFieldFormat getFormat() {
            return format;
        }

        public String generateLogItem(AuditChangeItem item) {
            Li li = new Li().appendText(AppContext.getMessage(displayName) + ": ")
                    .appendText(format.toString(item.getOldvalue()))
                    .appendText("&nbsp; &rarr; &nbsp; ")
                    .appendText(format.toString(item.getNewvalue()));
            return li.write();
        }
    }

    public static class LocalizationHistoryFieldFormat implements HistoryFieldFormat {
        private Class<? extends Enum> enumCls;

        public LocalizationHistoryFieldFormat(Class<? extends Enum> enumCls) {
            this.enumCls = enumCls;
        }

        @Override
        public String toString(String value) {
            return toString(value, true, AppContext.getMessage(GenericI18Enum.FORM_EMPTY));
        }

        @Override
        public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
            String content;
            if (StringUtils.isNotBlank(value)) {
                content = AppContext.getMessage(enumCls, value);
                content = (content.length() > 150) ? (content.substring(0, 150) + "...") : content;
            } else {
                content = msgIfBlank;
            }

            return content;
        }
    }

    public static class DefaultHistoryFieldFormat implements HistoryFieldFormat {

        @Override
        public String toString(String value) {
            return toString(value, true, AppContext.getMessage(GenericI18Enum.FORM_EMPTY));
        }

        @Override
        public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
            String content;
            if (StringUtils.isNotBlank(value)) {
                content = (value.length() > 150) ? (value.substring(0, 150) + "...") : value;
            } else {
                content = msgIfBlank;
            }

            return content;
        }
    }

    public static final class TrimHtmlHistoryFieldFormat implements HistoryFieldFormat {
        @Override
        public String toString(String value) {
            return toString(value, true, AppContext.getMessage(GenericI18Enum.FORM_EMPTY));
        }

        @Override
        public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
            String content;
            if (StringUtils.isNotBlank(value)) {
                content = StringUtils.trimHtmlTags(value);
                content = (content.length() > 150) ? (content.substring(0, 150) + "...") : content;
            } else {
                content = msgIfBlank;
            }

            return content;
        }
    }

    public static class PrettyDateHistoryFieldFormat implements HistoryFieldFormat {

        @Override
        public String toString(String value) {
            return toString(value, true, AppContext.getMessage(GenericI18Enum.FORM_EMPTY));
        }

        @Override
        public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
            if (StringUtils.isNotBlank(value)) {
                Date formatDate = DateTimeUtils.parseDateByW3C(value);
                if (displayAsHtml) {
                    Span lbl = new Span().appendText(AppContext.formatPrettyTime(formatDate));
                    lbl.setTitle(AppContext.formatDate(formatDate));
                    return lbl.write();
                } else {
                    return AppContext.formatDate(formatDate);
                }
            } else {
                return msgIfBlank;
            }
        }
    }

    public static class PrettyDateTimeHistoryFieldFormat implements HistoryFieldFormat {

        @Override
        public String toString(String value) {
            return toString(value, true, AppContext.getMessage(GenericI18Enum.FORM_EMPTY));
        }

        @Override
        public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
            if (StringUtils.isNotBlank(value)) {
                Date formatDate = DateTimeUtils.parseDateByW3C(value);
                if (displayAsHtml) {
                    Span lbl = new Span().appendText(AppContext.formatPrettyTime(formatDate));
                    lbl.setTitle(AppContext.formatDateTime(formatDate));
                    return lbl.write();
                } else {
                    return AppContext.formatDateTime(formatDate);
                }
            } else {
                return msgIfBlank;
            }
        }
    }

    public static class DateHistoryFieldFormat implements HistoryFieldFormat {

        @Override
        public String toString(String value) {
            return toString(value, true, AppContext.getMessage(GenericI18Enum.FORM_EMPTY));
        }

        @Override
        public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
            String content;
            if (StringUtils.isNotBlank(value)) {
                Date formatDate = DateTimeUtils.parseDateByW3C(value);
                content = AppContext.formatDate(formatDate);
            } else {
                content = msgIfBlank;
            }
            return content;
        }
    }

    public static class DateTimeHistoryFieldFormat implements HistoryFieldFormat {

        @Override
        public String toString(String value) {
            return toString(value, true, AppContext.getMessage(GenericI18Enum.FORM_EMPTY));
        }

        @Override
        public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
            String content;
            if (StringUtils.isNotBlank(value)) {
                Date formatDate = DateTimeUtils.parseDateByW3C(value);
                content = AppContext.formatDateTime(formatDate);
            } else {
                content = msgIfBlank;
            }
            return content;
        }
    }

    public static class CurrencyHistoryFieldFormat implements HistoryFieldFormat {

        @Override
        public String toString(String value) {
            return toString(value, true, AppContext.getMessage(GenericI18Enum.FORM_EMPTY));
        }

        @Override
        public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
            String content;
            if (StringUtils.isNotBlank(value)) {
                try {
                    Integer currencyId = Integer.parseInt(value);
                    CurrencyService currencyService = ApplicationContextUtil.getSpringBean(CurrencyService.class);
                    Currency currency = currencyService.getCurrency(currencyId);
                    content = currency.getSymbol();
                } catch (Exception e) {
                    LOG.error("Error while get currency id" + value, e);
                    content = msgIfBlank;
                }
            } else {
                content = msgIfBlank;
            }

            return content;
        }
    }

    public static class I18nHistoryFieldFormat implements HistoryFieldFormat {
        private Class<? extends Enum> enumCls;

        public I18nHistoryFieldFormat(Class<? extends Enum> enumCls) {
            this.enumCls = enumCls;
        }

        @Override
        public String toString(String value) {
            return toString(value, true, "");
        }

        @Override
        public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
            return (StringUtils.isNotBlank(value)) ? AppContext.getMessage(enumCls, value) : msgIfBlank;
        }
    }
}