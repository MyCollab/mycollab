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

import org.vaadin.easyuploads.MultiFileUploadExt;

import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.project.LabelLink;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class DefaultFormViewFieldFactory {

	public static interface AttachmentUploadField extends Field {
		void saveContentsToRepo(String attachmentPath);
	}

	public static class FormAttachmentUploadField extends CustomField implements
			AttachmentUploadField {
		private static final long serialVersionUID = 1L;
		private MultiFileUploadExt uploadExt;
		private AttachmentPanel attachmentPanel;

		public FormAttachmentUploadField() {
			attachmentPanel = new AttachmentPanel();
		}

		public void getAttachments(String attachmentPath) {
			attachmentPanel.getAttachments(attachmentPath);
		}

		@Override
		public Class<?> getType() {
			return Object.class;
		}

		@Override
		public void saveContentsToRepo(String attachmentPath) {
			attachmentPanel.saveContentsToRepo(attachmentPath);
		}

		@Override
		protected Component initContent() {
			final VerticalLayout layout = new VerticalLayout();
			uploadExt = new MultiFileUploadExt(attachmentPanel);
			uploadExt.addComponent(attachmentPanel);
			layout.addComponent(uploadExt);
			return layout;
		}
	}

	public static class FormContainerField extends CustomField {

		private static final long serialVersionUID = 1L;

		private ComponentContainer container;

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

		private HorizontalLayout layout;

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
		private Calendar calendar = Calendar.getInstance();

		private Date date;
		private String dateformat;

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

	public static class FormContainerViewField extends CustomField<Object> {
		private static final long serialVersionUID = 1L;
		private CssLayout layout;

		public FormContainerViewField() {
			layout = new CssLayout();
			layout.setWidth("100%");
			layout.setStyleName(UIConstants.FORM_CONTAINER_VIEW);
		}

		public void addComponentField(final Component component) {
			layout.addComponent(component);
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

	public static class FormDateViewField extends CustomField {
		private static final long serialVersionUID = 1L;

		private Date date;

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
			final EmailLink emailLink = new EmailLink(email);
			emailLink.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
			return emailLink;
		}
	}

	public static class FormLinkViewField extends CustomField<String> {

		private static final long serialVersionUID = 1L;

		private String value;
		private String iconResourceLink;
		private String href;

		public FormLinkViewField(String value, String href) {
			this(value, href, null);
		}

		public FormLinkViewField(String value, String href,
				String iconResourceLink) {
			this.value = value;
			this.href = href;
			this.iconResourceLink = iconResourceLink;
		}

		@Override
		public Class<String> getType() {
			return String.class;
		}

		@Override
		protected Component initContent() {
			if (value != null && (!value.equals(""))) {
				final LabelLink l = new LabelLink(value, href);
				if (iconResourceLink != null) {
					l.setIconLink(iconResourceLink);
				}
				l.setWidth("100%");
				return l;
			} else {
				final Label l = new Label("&nbsp;", ContentMode.HTML);
				l.setWidth("100%");
				return l;
			}
		}
	}

	public static class FormDetectAndDisplayUrlViewField extends CustomField {
		private static final long serialVersionUID = 1L;

		private String url;

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

	public static class FormUrlLinkViewField extends CustomField<String> {

		private static final long serialVersionUID = 1L;

		private String url;

		public FormUrlLinkViewField(String url) {
			this.url = url;
		}

		@Override
		public Class<String> getType() {
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
				final Link link = new UrlLink(url);
				link.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
				return link;
			}
		}
	}

	public static class FormUrlSocialNetworkLinkViewField extends
			CustomField<String> {

		private static final long serialVersionUID = 1L;

		private String caption;
		private String linkAccount;

		public FormUrlSocialNetworkLinkViewField(String caption,
				String linkAccount) {
			this.caption = caption;
			this.linkAccount = linkAccount;
		}

		@Override
		public Class<String> getType() {
			return String.class;
		}

		@Override
		protected Component initContent() {
			if (caption == null || caption.trim().equals("")) {
				Label lbl = new Label("&nbsp;");
				lbl.setContentMode(ContentMode.HTML);
				lbl.setWidth("100%");
				return lbl;
			} else {
				linkAccount = (linkAccount == null) ? "" : linkAccount;
				final Link link = new SocialNetworkLink(caption, linkAccount);
				link.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
				return link;
			}
		}
	}

	public static class FormViewField extends CustomField<String> {

		private String value;
		private ContentMode contentMode;

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

	public static class UserLinkViewField extends CustomField {
		private static final long serialVersionUID = 1L;

		private String username;
		private String userAvatarId;
		private String fullName;

		public UserLinkViewField(String username, String userAvatarId,
				final String fullName) {
			this.username = username;
			this.userAvatarId = userAvatarId;
			this.fullName = fullName;
		}

		@Override
		public Class<?> getType() {
			return Object.class;
		}

		@Override
		protected Component initContent() {
			final UserLink userLink = new UserLink(username, userAvatarId,
					fullName);
			userLink.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
			return userLink;
		}
	}
}
