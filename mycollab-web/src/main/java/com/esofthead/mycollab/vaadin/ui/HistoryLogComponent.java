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

package com.esofthead.mycollab.vaadin.ui;

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
import com.esofthead.mycollab.common.domain.criteria.AuditLogSearchCriteria;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.common.service.CurrencyService;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class HistoryLogComponent extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory
			.getLogger(HistoryLogComponent.class);

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
	protected BeanList<AuditLogService, AuditLogSearchCriteria, SimpleAuditLog> logTable;
	protected Map<String, FieldDisplayHandler> fieldsFormat = new HashMap<String, FieldDisplayHandler>();
	private String module;
	private String type;

	public HistoryLogComponent(String module, String type) {
		this.module = module;
		this.type = type;

		logTable = new BeanList<AuditLogService, AuditLogSearchCriteria, SimpleAuditLog>(
				this,
				ApplicationContextUtil.getSpringBean(AuditLogService.class),
				HistoryLogRowDisplay.class);
		this.setWidth("100%");
		this.setHeight(Sizeable.SIZE_UNDEFINED, Sizeable.Unit.PIXELS);
		this.setMargin(true);
		this.setStyleName("historylog-component");

		this.addComponent(logTable);
	}

	public void loadHistory(int typeid) {
		AuditLogSearchCriteria criteria = new AuditLogSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
		criteria.setModule(new StringSearchField(module));
		criteria.setType(new StringSearchField(type));
		criteria.setTypeid(new NumberSearchField(typeid));
		logTable.setSearchCriteria(criteria);
	}

	public void generateFieldDisplayHandler(String fieldname, String displayName) {
		fieldsFormat.put(fieldname, new FieldDisplayHandler(displayName));
	}

	public void generateFieldDisplayHandler(String fieldname,
			String displayName, HistoryFieldFormat format) {
		fieldsFormat.put(fieldname,
				new FieldDisplayHandler(displayName, format));
	}

	public void generateFieldDisplayHandler(String fieldname,
			String displayName, String formatName) {
		fieldsFormat.put(fieldname, new FieldDisplayHandler(displayName,
				defaultFieldHandlers.get(formatName)));
	}

	public class HistoryLogRowDisplay implements
			BeanList.RowDisplayHandler<SimpleAuditLog> {
		private static final long serialVersionUID = 1L;

		@Override
		public Component generateRow(SimpleAuditLog log, int rowIndex) {

			List<AuditChangeItem> changeItems = log.getChangeItems();
			if (changeItems != null && changeItems.size() > 0) {
				CssLayout layout = new CssLayout();
				layout.setWidth("100%");
				layout.setStyleName("activity-stream");

				GridLayout gridLayout = new GridLayout(3,
						changeItems.size() + 2);
				gridLayout.setWidth("100%");

				int visibleRows = 0;

				String strDate = "";

				for (int i = 0; i < changeItems.size(); i++) {
					AuditChangeItem item = changeItems.get(i);
					String fieldName = item.getField();

					FieldDisplayHandler fieldDisplayHandler = fieldsFormat
							.get(fieldName);
					if (fieldDisplayHandler != null) {
						gridLayout
								.addComponent(
										new Label(fieldDisplayHandler
												.getDisplayName()), 0,
										visibleRows + 2);
						gridLayout.addComponent(fieldDisplayHandler.getFormat()
								.formatField(item.getOldvalue()), 1,
								visibleRows + 2);
						gridLayout.addComponent(fieldDisplayHandler.getFormat()
								.formatField(item.getNewvalue()), 2,
								visibleRows + 2);
						visibleRows++;
					}

					if (fieldName.equals("lastupdatedtime")) {
						strDate = item.getNewvalue();
					}
				}

				if (visibleRows == 0) {
					return null;
				} else {
					HorizontalLayout header = new HorizontalLayout();
					header.setWidth("100%");
					header.setSpacing(true);
					UserLink userLink = new UserLink(log.getPosteduser(),
							log.getPostedUserAvatarId(),
							log.getPostedUserFullName(), false);

					header.addComponent(userLink);
					header.setComponentAlignment(userLink,
							Alignment.MIDDLE_LEFT);

					Label lbDate = new Label("changed "
							+ DateTimeUtils.getStringDateFromNow(DateTimeUtils
									.convertDateByFormatW3C(strDate),
									AppContext.getUserLocale()));
					header.addComponent(lbDate);
					header.setComponentAlignment(lbDate, Alignment.MIDDLE_LEFT);
					header.setExpandRatio(lbDate, 1.0f);
					gridLayout.addComponent(header, 0, 0, 2, 0);

					gridLayout.addComponent(new Label(
							"<div style=\"font-weight: bold;\">Field</div>",
							ContentMode.HTML), 0, 1);
					gridLayout
							.addComponent(
									new Label(
											"<div style=\"font-weight: bold;\">Old Value</div>",
											ContentMode.HTML), 1, 1);
					gridLayout
							.addComponent(
									new Label(
											"<div style=\"font-weight: bold;\">New Value</div>",
											ContentMode.HTML), 2, 1);

					gridLayout.setRows(visibleRows + 2);
					layout.addComponent(gridLayout);
					return layout;
				}

			} else {
				return null;
			}

		}
	}

	private static class FieldDisplayHandler {

		private String displayName;
		private HistoryFieldFormat format;

		public FieldDisplayHandler(String displayName) {
			this(displayName, new DefaultHistoryFieldFormat());
		}

		public FieldDisplayHandler(String displayName, HistoryFieldFormat format) {
			this.displayName = displayName;
			this.format = format;
		}

		public String getDisplayName() {
			return displayName;
		}

		public HistoryFieldFormat getFormat() {
			return format;
		}
	}

	public static class DefaultHistoryFieldFormat implements HistoryFieldFormat {

		@Override
		public Component formatField(String value) {
			LabelHTMLDisplayWidget lbHtml = new LabelHTMLDisplayWidget(value);
			lbHtml.setWidth("90%");
			return lbHtml;
		}
	}

	public static class DateHistoryFieldFormat implements HistoryFieldFormat {

		@Override
		public Component formatField(String value) {
			Date formatDate = DateTimeUtils.convertDateByFormatW3C(value);
			return new Label(AppContext.formatDate(formatDate));
		}
	}

	public static class DateTimeHistoryFieldFormat implements
			HistoryFieldFormat {

		@Override
		public Component formatField(String value) {
			if (value != null && !value.trim().equals("")) {
				Date formatDate = DateTimeUtils.convertDateByFormatW3C(value);
				SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(
						"MM/dd/yyyy HH:mm");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(formatDate);
				int timeFormat = calendar.get(Calendar.AM_PM);
				if (timeFormat == 1) {
					calendar.add(Calendar.HOUR_OF_DAY, -12);
				}
				String dateStr = simpleDateTimeFormat
						.format(calendar.getTime())
						+ ((timeFormat == 0) ? " AM" : " PM");
				return new Label(dateStr);
			} else {
				return new Label();
			}
		}
	}

	public static class CurrencyHistoryFieldFormat implements
			HistoryFieldFormat {

		@Override
		public Component formatField(String value) {
			if (value != null && !"".equals(value)) {
				try {
					Integer currencyid = Integer.parseInt(value);
					CurrencyService currencyService = ApplicationContextUtil
							.getSpringBean(CurrencyService.class);
					Currency currency = currencyService.getCurrency(currencyid);
					return new Label(currency.getSymbol());
				} catch (Exception e) {
					log.error("Error while get currency id" + value, e);
					return new Label("");
				}
			}

			return new Label("");
		}
	}

	public static class I18nHistoryFieldFormat implements HistoryFieldFormat {

		private Class<? extends Enum> enumCls;

		public I18nHistoryFieldFormat(Class<? extends Enum> enumCls) {
			this.enumCls = enumCls;
		}

		@Override
		public Component formatField(String value) {
			if (value != null && !"".equals(value)) {
				return new Label(AppContext.getMessage(Enum.valueOf(enumCls,
						value)));
			}

			return new Label("");
		}

	}
}
