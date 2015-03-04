/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.domain.AuditChangeItem;
import com.esofthead.mycollab.common.domain.Currency;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.CurrencyService;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.HistoryFieldFormat;
import com.esofthead.mycollab.vaadin.ui.LabelHTMLDisplayWidget;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.3
 *
 */
public class FieldGroupFormatter {
	private static final Logger LOG = LoggerFactory.getLogger(FieldGroupFormatter.class);

	private static Map<String, HistoryFieldFormat> defaultFieldHandlers;

	public static final String DEFAULT_FIELD = "default";
	public static final String DATE_FIELD = "date";
	public static final String DATETIME_FIELD = "datetime";
	public static final String CURRENCY_FIELD = "currency";

	protected Map<String, FieldDisplayHandler> fieldsFormat = new HashMap<String, FieldDisplayHandler>();

	static {
		defaultFieldHandlers = new HashMap<>();
		defaultFieldHandlers
				.put(DEFAULT_FIELD, new DefaultHistoryFieldFormat());
		defaultFieldHandlers.put(DATE_FIELD, new DateHistoryFieldFormat());
		defaultFieldHandlers.put(DATETIME_FIELD,
				new DateTimeHistoryFieldFormat());
		defaultFieldHandlers.put(CURRENCY_FIELD,
				new CurrencyHistoryFieldFormat());
	}

	@SuppressWarnings("rawtypes")
	public void generateFieldDisplayHandler(String fieldname, Enum displayName) {
		fieldsFormat.put(fieldname, new FieldDisplayHandler(displayName));
	}

	@SuppressWarnings("rawtypes")
	public void generateFieldDisplayHandler(String fieldname, Enum displayName,
			HistoryFieldFormat format) {
		fieldsFormat.put(fieldname,
				new FieldDisplayHandler(displayName, format));
	}

	@SuppressWarnings("rawtypes")
	public void generateFieldDisplayHandler(String fieldname, Enum displayName,
			String formatName) {
		fieldsFormat.put(fieldname, new FieldDisplayHandler(displayName,
				defaultFieldHandlers.get(formatName)));
	}

	public FieldDisplayHandler getFieldDisplayHandler(String fieldname) {
		return fieldsFormat.get(fieldname);
	}

	@SuppressWarnings("rawtypes")
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
			StringBuffer str = new StringBuffer();
			str.append("<li>");
			str.append(AppContext.getMessage(displayName)).append(": ")
					.append("<i>")
					.append(this.getFormat().toString(item.getOldvalue()))
					.append("</i>").append("&nbsp; &rarr; &nbsp; ")
					.append("<i>")
					.append(this.getFormat().toString(item.getNewvalue()))
					.append("</i>");
			str.append("</li>");
			return str.toString();
		}
	}

	public static class DefaultHistoryFieldFormat implements HistoryFieldFormat {

		@Override
		public Component toVaadinComponent(String value) {
			LabelHTMLDisplayWidget lbHtml = new LabelHTMLDisplayWidget(value);
			lbHtml.setWidth("90%");
			return lbHtml;
		}

		@Override
		public String toString(String value) {
			if (StringUtils.isNoneBlank(value)) {
				return (value.length() > 200) ? (value.substring(0, 150) + "...")
						: value;
			} else {
				return AppContext.getMessage(GenericI18Enum.FORM_EMPTY);
			}
		}
	}

	public static class DateHistoryFieldFormat implements HistoryFieldFormat {

		@Override
		public Component toVaadinComponent(String value) {
			Date formatDate = DateTimeUtils.convertDateByFormatW3C(value);
			return new Label(AppContext.formatDate(formatDate));
		}

		@Override
		public String toString(String value) {
			if (StringUtils.isNoneBlank(value)) {
				Date formatDate = DateTimeUtils.convertDateByFormatW3C(value);
				return AppContext.formatDate(formatDate);
			} else {
				return AppContext.getMessage(GenericI18Enum.FORM_EMPTY);
			}
		}
	}

	public static class DateTimeHistoryFieldFormat implements
			HistoryFieldFormat {

		@Override
		public Component toVaadinComponent(String value) {
			if (StringUtils.isNotBlank(value)) {
				Date formatDate = DateTimeUtils.convertDateByFormatW3C(value);
				return new Label(AppContext.formatDateTime(formatDate));
			} else {
				return new Label();
			}
		}

		@Override
		public String toString(String value) {
			if (StringUtils.isNotBlank(value)) {
				Date formatDate = DateTimeUtils.convertDateByFormatW3C(value);
				return AppContext.formatDateTime(formatDate);
			} else {
				return AppContext.getMessage(GenericI18Enum.FORM_EMPTY);
			}
		}
	}

	public static class CurrencyHistoryFieldFormat implements
			HistoryFieldFormat {

		@Override
		public Component toVaadinComponent(String value) {
			if (StringUtils.isNotBlank(value)) {
				try {
					Integer currencyId = Integer.parseInt(value);
					CurrencyService currencyService = ApplicationContextUtil
							.getSpringBean(CurrencyService.class);
					Currency currency = currencyService.getCurrency(currencyId);
					return new Label(currency.getSymbol());
				} catch (Exception e) {
					LOG.error("Error while get currency id" + value, e);
					return new Label("");
				}
			}

			return new Label("");
		}

		@Override
		public String toString(String value) {
			if (StringUtils.isNotBlank(value)) {
				try {
					Integer currencyid = Integer.parseInt(value);
					CurrencyService currencyService = ApplicationContextUtil
							.getSpringBean(CurrencyService.class);
					Currency currency = currencyService.getCurrency(currencyid);
					return currency.getSymbol();
				} catch (Exception e) {
					LOG.error("Error while get currency id" + value, e);
					return AppContext.getMessage(GenericI18Enum.FORM_EMPTY);
				}
			}

			return AppContext.getMessage(GenericI18Enum.FORM_EMPTY);
		}
	}

	@SuppressWarnings("rawtypes")
	public static class I18nHistoryFieldFormat implements HistoryFieldFormat {

		private Class<? extends Enum> enumCls;

		public I18nHistoryFieldFormat(Class<? extends Enum> enumCls) {
			this.enumCls = enumCls;
		}

		@Override
		public Component toVaadinComponent(String value) {
			try {
				if (StringUtils.isNotBlank(value)) {
					return new Label(AppContext.getMessage(enumCls, value));
				}

				return new Label("");
			} catch (Exception e) {
				return new Label(value);
			}
		}

		@Override
		public String toString(String value) {
			try {
				if (StringUtils.isNotBlank(value)) {
					return AppContext.getMessage(enumCls, value);
				}

				return "";
			} catch (Exception e) {
				return value;
			}
		}
	}
}