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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.domain.AuditChangeItem;
import com.esofthead.mycollab.common.domain.Currency;
import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.CurrencyService;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.HistoryFieldFormat;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class AuditLogShowHandler {

	private static Logger log = LoggerFactory
			.getLogger(AuditLogShowHandler.class);

	protected Map<String, FieldDisplayHandler> fieldsFormat = new HashMap<String, FieldDisplayHandler>();
	public static final String DEFAULT_FIELD = "default";
	public static final String DATE_FIELD = "date";
	public static final String DATETIME_FIELD = "datetime";
	public static final String CURRENCY_FIELD = "currency";
	private static Map<String, HistoryFieldFormat> defaultFieldHandlers;

	static {
		defaultFieldHandlers = new HashMap<String, HistoryFieldFormat>();
		defaultFieldHandlers
				.put(DEFAULT_FIELD, new DefaultHistoryFieldFormat());
		defaultFieldHandlers.put(DATE_FIELD, new DateHistoryFieldFormat());
		defaultFieldHandlers.put(DATETIME_FIELD,
				new DateTimeHistoryFieldFormat());
		defaultFieldHandlers.put(CURRENCY_FIELD,
				new CurrencyHistoryFieldFormat());
	}

	public void generateFieldDisplayHandler(String fieldname,
			Enum<?> displayName) {
		fieldsFormat.put(fieldname, new FieldDisplayHandler(displayName));
	}

	public void generateFieldDisplayHandler(String fieldname,
			Enum<?> displayName, HistoryFieldFormat format) {
		fieldsFormat.put(fieldname,
				new FieldDisplayHandler(displayName, format));
	}

	public void generateFieldDisplayHandler(String fieldname,
			Enum<?> displayName, String formatName) {
		fieldsFormat.put(fieldname, new FieldDisplayHandler(displayName,
				defaultFieldHandlers.get(formatName)));
	}

	public String generateChangeSet(SimpleAuditLog auditLog) {
		StringBuffer str = new StringBuffer("");
		boolean isAppended = false;
		List<AuditChangeItem> changeItems = auditLog.getChangeItems();
		if (changeItems != null && changeItems.size() > 0) {
			for (int i = 0; i < changeItems.size(); i++) {
				AuditChangeItem item = changeItems.get(i);
				String fieldName = item.getField();
				FieldDisplayHandler fieldDisplayHandler = fieldsFormat
						.get(fieldName);
				if (fieldDisplayHandler != null) {
					isAppended = true;
					str.append(fieldDisplayHandler.generateLogItem(item));
				}
			}

		}
		if (isAppended) {
			str.insert(0, "<p>").insert(0, "<ul>");
			str.append("</ul>").append("</p>");
		}
		return str.toString();
	}

	private static class FieldDisplayHandler {

		private Enum<?> displayName;
		private HistoryFieldFormat format;

		public FieldDisplayHandler(Enum<?> displayName) {
			this(displayName, new DefaultHistoryFieldFormat());
		}

		public FieldDisplayHandler(Enum<?> displayName,
				HistoryFieldFormat format) {
			this.displayName = displayName;
			this.format = format;
		}

		public HistoryFieldFormat getFormat() {
			return format;
		}

		public String generateLogItem(AuditChangeItem item) {
			StringBuffer str = new StringBuffer();
			str.append("<li>");
			str.append(AppContext.getMessage(displayName)).append(": ")
					.append("<i>")
					.append(this.getFormat().formatField(item.getOldvalue()))
					.append("</i>").append("&nbsp; &rarr; &nbsp; ")
					.append("<i>")
					.append(this.getFormat().formatField(item.getNewvalue()))
					.append("</i>");
			str.append("</li>");
			return str.toString();
		}
	}

	public static interface HistoryFieldFormat {

		String formatField(String value);
	}

	public static class CurrencyHistoryFieldFormat implements
			HistoryFieldFormat {
		@Override
		public String formatField(String value) {
			if (value != null && !"".equals(value)) {
				try {
					Integer currencyid = Integer.parseInt(value);
					CurrencyService currencyService = ApplicationContextUtil
							.getSpringBean(CurrencyService.class);
					Currency currency = currencyService.getCurrency(currencyid);
					return currency.getSymbol();
				} catch (Exception e) {
					log.error("Error while get currency id" + value, e);
					return AppContext.getMessage(GenericI18Enum.FORM_EMPTY);
				}
			}

			return AppContext.getMessage(GenericI18Enum.FORM_EMPTY);
		}
	}

	public static class DefaultHistoryFieldFormat implements HistoryFieldFormat {

		@Override
		public String formatField(String value) {
			if (value != null && !value.trim().equals("")) {
				return (value.length() > 200) ? (value.substring(0, 150) + "...")
						: value;
			} else {
				return AppContext.getMessage(GenericI18Enum.FORM_EMPTY);
			}
		}
	}

	public static class DateHistoryFieldFormat implements HistoryFieldFormat {

		@Override
		public String formatField(String value) {
			if (value != null && !value.trim().equals("")) {
				Date formatDate = DateTimeUtils.convertDateByFormatW3C(value);
				SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(
						"MM/dd/yyyy");
				return simpleDateTimeFormat.format(formatDate);
			} else {
				return AppContext.getMessage(GenericI18Enum.FORM_EMPTY);
			}

		}
	}

	public static class DateTimeHistoryFieldFormat implements
			HistoryFieldFormat {

		@Override
		public String formatField(String value) {
			if (value != null && !value.trim().equals("")) {
				Date formatDate = DateTimeUtils.convertDateByFormatW3C(value);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(formatDate);
				int timeFormat = calendar.get(Calendar.AM_PM);
				if (timeFormat == 1) {
					calendar.add(Calendar.HOUR_OF_DAY, -12);
				}
				SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(
						"MM/dd/yyyy HH:mm");
				String dateStr = simpleDateTimeFormat
						.format(calendar.getTime())
						+ ((timeFormat == 0) ? " AM" : " PM");
				return dateStr;
			} else {
				return AppContext.getMessage(GenericI18Enum.FORM_EMPTY);
			}

		}
	}

	public static class I18nHistoryFieldFormat implements HistoryFieldFormat {

		private Class<? extends Enum> enumCls;

		public I18nHistoryFieldFormat(Class<? extends Enum> enumCls) {
			this.enumCls = enumCls;
		}

		@Override
		public String formatField(String value) {
			try {
				if (value != null && !"".equals(value)) {
					return AppContext.getMessage(Enum.valueOf(enumCls, value));
				}

				return "";
			} catch (Exception e) {
				return value;
			}
		}

	}
}
