/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class DefaultFormViewFieldFactory {

	public static interface AttachmentUploadField extends Field {
		void saveContentsToRepo(String attachmentPath);
	}

	public static class FormContainerField extends CustomField {

		private static final long serialVersionUID = 1L;

		private final ComponentContainer container;

		public FormContainerField(final ComponentContainer container) {
			this.container = container;
		}

		@Override
		public Class<?> getType() {
			return Object.class;
		}

		@Override
		protected Component initContent() {
			return container;
		}
	}

	public static class FormContainerHorizontalViewField extends CustomField {
		private static final long serialVersionUID = 1L;

		private final HorizontalLayout layout;

		public FormContainerHorizontalViewField() {
			layout = new HorizontalLayout();
			layout.setWidth("100%");
			layout.setSpacing(true);
		}

		public void addComponentField(final Component component) {
			layout.addComponent(component);
		}

		public HorizontalLayout getLayout() {
			return layout;
		}

		@Override
		public Class<?> getType() {
			return Object.class;
		}

		@Override
		protected Component initContent() {
			return layout;
		}
	}

	public static class DateFieldWithUserTimeZone extends CustomField<String> {
		private static final long serialVersionUID = 1L;

		private static String DATE_FORMAT = "MM/dd/yyyy";
		private static String DATETIME_FORMAT = "MM/dd/yyyy HH:mm";
		private SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(
				DATE_FORMAT);
		private final Calendar calendar = Calendar.getInstance();

		private final Date date;
		private final String dateformat;

		public DateFieldWithUserTimeZone(final Date date, String dateformat) {
			this.date = date;
			this.dateformat = dateformat;
		}

		@Override
		public Class<String> getType() {
			return String.class;
		}

		@Override
		protected Component initContent() {
			if (date == null) {
				return new Label();
			} else {
				if (dateformat.equals("DATETIME_FIELD")) {
					simpleDateTimeFormat = new SimpleDateFormat(DATETIME_FORMAT);
				}
				calendar.setTime(date);
				int timeFormat = calendar.get(Calendar.AM_PM);
				if (timeFormat == 1) {
					calendar.add(Calendar.HOUR_OF_DAY, -12);
				}
				String timeStr = simpleDateTimeFormat
						.format(calendar.getTime())
						+ " "
						+ ((timeFormat == 0) ? "AM" : "PM");
				Label label = new Label();
				label.setValue(timeStr);
				HorizontalLayout layout = new HorizontalLayout();
				layout.addComponent(label);
				return layout;
			}
		}
	}

	public static class FormDateViewField extends CustomField {
		private static final long serialVersionUID = 1L;

		private final Date date;

		public FormDateViewField(final Date date) {
			this.date = date;
		}

		@Override
		public Class<?> getType() {
			return Object.class;
		}

		@Override
		protected Component initContent() {
			final Label l = new Label();
			l.setWidth("100%");
			if (date == null) {
				l.setValue("&nbsp;");
				l.setContentMode(ContentMode.HTML);
			} else {
				l.setValue(AppContext.formatDate(date));
			}
			return l;
		}
	}

	public static class FormEmailLinkViewField extends CustomField<String> {

		private static final long serialVersionUID = 1L;

		private String email;

		public FormEmailLinkViewField(final String email) {
			this.email = email;
		}

		@Override
		public Class<String> getType() {
			return String.class;
		}

		@Override
		protected Component initContent() {
			final Link emailLink = new Link(email, new ExternalResource(
					"mailto:" + email));
			// emailLink.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
			return emailLink;
		}
	}

	public static class FormDetectAndDisplayUrlViewField extends CustomField {
		private static final long serialVersionUID = 1L;

		private final String url;

		public FormDetectAndDisplayUrlViewField(String url) {
			this.url = url;
		}

		@Override
		public Class<?> getType() {
			return String.class;
		}

		@Override
		protected Component initContent() {
			if (url == null || url.trim().equals("")) {
				Label lbl = new Label("&nbsp;");
				lbl.setContentMode(ContentMode.HTML);
				lbl.setWidth("100%");
				return lbl;
			} else {
				final Label link = new Label(StringUtils.formatExtraLink(url),
						ContentMode.HTML);
				return link;
			}
		}
	}

	public static class FormViewField extends CustomField<String> {

		private final String value;
		private final ContentMode contentMode;

		private static final long serialVersionUID = 1L;

		public FormViewField(final String value) {
			this(value, ContentMode.TEXT);
		}

		public FormViewField(final String value, final ContentMode contentMode) {
			this.value = value;
			this.contentMode = contentMode;
		}

		@Override
		public Class<String> getType() {
			return String.class;
		}

		@Override
		protected Component initContent() {
			final Label label = new Label();
			label.setWidth("100%");
			label.setContentMode(contentMode);

			if (value != null && (!value.equals(""))) {
				label.setValue(value);
			} else {
				label.setValue("");
			}

			return label;
		}
	}

}
