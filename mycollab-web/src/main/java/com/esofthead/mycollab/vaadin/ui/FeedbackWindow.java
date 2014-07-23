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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;
import org.vaadin.easyuploads.MultiFileUploadExt;

import com.esofthead.mycollab.common.domain.MailRecipientField;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.module.mail.EmailAttachementSource;
import com.esofthead.mycollab.module.mail.FileEmailAttachmentSource;
import com.esofthead.mycollab.module.mail.service.ExtMailService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class FeedbackWindow extends Window {
	private static final long serialVersionUID = 1L;
	private TextField emailTextField;
	private TextField subjectTextField;
	private TextField emailNameTextField;

	public FeedbackWindow() {
		initLayout();
	}

	private void initLayout() {
		this.setCaption("Send us feedback for MyCollab ");
		initUI();
		center();
		this.setModal(true);
		this.setResizable(false);
	}

	private void initDefaultData() {
		if (AppContext.getSession() != null) {
			String name = AppContext.getSession().getDisplayName().equals("") ? ""
					: AppContext.getSession().getDisplayName();
			String email = (AppContext.getSession().getEmail() == null && AppContext
					.getSession().getEmail().equals("")) ? "" : AppContext
					.getSession().getEmail();
			emailNameTextField.setValue(name);
			emailTextField.setValue(email);
		}
	}

	private void initUI() {
		GridLayout mainLayout = new GridLayout(2, 5);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		emailNameTextField = new TextField();
		emailNameTextField.setWidth("500px");
		Label emailName = new Label("Your name: ");

		mainLayout.addComponent(emailName, 0, 0);
		mainLayout.addComponent(emailNameTextField, 1, 0);

		emailTextField = new TextField();
		emailTextField.setWidth("500px");
		emailTextField.setRequired(true);
		Label emailLbl = new Label("Your email: ");

		mainLayout.addComponent(emailLbl, 0, 1);
		mainLayout.addComponent(emailTextField, 1, 1);

		subjectTextField = new TextField();
		subjectTextField.setWidth("500px");
		subjectTextField.setRequired(true);
		Label subjectLbl = new Label("Subject: ");

		mainLayout.addComponent(subjectLbl, 0, 2);
		mainLayout.addComponent(subjectTextField, 1, 2);

		final RichTextArea contentArea = new RichTextArea();
		contentArea.setImmediate(true);
		contentArea.setWidth(500, Sizeable.Unit.PIXELS);
		contentArea.setHeight(200, Sizeable.Unit.PIXELS);
		Label contentLbl = new Label("Your feedback: ");

		mainLayout.addComponent(contentLbl, 0, 3);
		mainLayout.addComponent(contentArea, 1, 3);

		initDefaultData();

		HorizontalLayout controlsLayout = new HorizontalLayout();
		controlsLayout.setWidth("100%");

		final AttachmentPanel attachments = new AttachmentPanel();
		attachments.setWidth("350px");

		MultiFileUploadExt uploadExt = new MultiFileUploadExt(attachments);
		uploadExt.addComponent(attachments);

		// Panel attachedFilepanel = new Panel();
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setHeight("80px");
		contentLayout.setStyleName("noneBorder-panel");
		contentLayout.setSizeUndefined();

		contentLayout.addComponent(uploadExt);

		// attachedFilepanel.setContent(contentLayout);

		controlsLayout.addComponent(contentLayout);
		controlsLayout.setComponentAlignment(contentLayout,
				Alignment.BOTTOM_LEFT);
		controlsLayout.setExpandRatio(contentLayout, 1.0f);

		controlsLayout.setSpacing(true);

		Button cancelBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						FeedbackWindow.this.close();
					}
				});

		cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
		controlsLayout.addComponent(cancelBtn);
		controlsLayout.setComponentAlignment(cancelBtn, Alignment.MIDDLE_RIGHT);

		Button sendBtn = new Button("Send", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				String email = emailTextField.getValue().toString().trim();
				String subject = subjectTextField.getValue().toString().trim();
				EmailValidator emailValidator = EmailValidator.getInstance();
				if (!emailValidator.isValid(email)) {
					NotificationUtil
							.showWarningNotification("The email is not valid, please check it again!");
					return;
				}
				if (!email.equals("") && !subject.equals("")) {
					ExtMailService systemMailService = ApplicationContextUtil
							.getSpringBean(ExtMailService.class);
					List<File> listFile = attachments.getListFile();
					List<EmailAttachementSource> emailAttachmentSource = null;
					if (listFile != null && listFile.size() > 0) {
						emailAttachmentSource = new ArrayList<EmailAttachementSource>();
						for (File file : listFile) {
							emailAttachmentSource
									.add(new FileEmailAttachmentSource(file));
						}
					}

					String nameEmailFrom = emailNameTextField.getValue()
							.toString().trim();
					nameEmailFrom = nameEmailFrom.equals("") ? email
							: nameEmailFrom;
					String toEmail = SiteConfiguration.getSendErrorEmail();

					FeedbackWindow.this.close();

					systemMailService.sendHTMLMail(email, nameEmailFrom, Arrays
							.asList(new MailRecipientField(toEmail, toEmail)),
							null, null, subject, contentArea.getValue()
									.toString(), emailAttachmentSource);

				} else {
					NotificationUtil
							.showWarningNotification("The email field and subject field must be not empty! Please fulfil them before pressing enter button.");
				}
			}
		});
		sendBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		controlsLayout.addComponent(sendBtn);
		controlsLayout.setComponentAlignment(sendBtn, Alignment.MIDDLE_RIGHT);
		mainLayout.addComponent(controlsLayout, 0, 4, 1, 4);

		this.setContent(mainLayout);
	}
}
