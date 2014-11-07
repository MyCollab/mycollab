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
package com.esofthead.mycollab.mobile.module.project.view.message;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.ui.form.field.ProjectFormAttachmentUploadField;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePageView;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.i18n.MessageI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.events.HasEditFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.0
 *
 */
@ViewComponent
public class MessageAddViewImpl extends AbstractMobilePageView implements
		MessageAddView, HasEditFormHandlers<SimpleMessage> {

	private static final long serialVersionUID = -5665807255892654312L;
	private CssLayout content;
	private Button saveBtn;

	private final TextField subjectField;
	private final TextArea contentField;
	private final Switch isStickField;

	private MessageAttachmentField attachment;

	private Set<EditFormHandler<SimpleMessage>> handlers = new HashSet<EditFormHandler<SimpleMessage>>();

	public MessageAddViewImpl() {
		this.addStyleName("message-add-view");
		this.setCaption(AppContext.getMessage(MessageI18nEnum.M_VIEW_ADD_TITLE));

		this.content = new CssLayout();
		this.content.setStyleName("content-layout");
		this.content.setSizeFull();
		this.setContent(this.content);

		VerticalLayout addFormLayout = new VerticalLayout();
		addFormLayout.setStyleName("addform-layout");
		addFormLayout.setWidth("100%");

		subjectField = new TextField();
		subjectField.setStyleName("title-field");
		subjectField.setWidth("100%");
		subjectField.setInputPrompt(AppContext
				.getMessage(MessageI18nEnum.FORM_TITLE));
		addFormLayout.addComponent(subjectField);

		contentField = new TextArea();
		contentField.setStyleName("content-field");
		contentField.setWidth("100%");
		contentField.setInputPrompt(AppContext
				.getMessage(MessageI18nEnum.M_FORM_CONTENT_FIELD_PROMPT));
		addFormLayout.addComponent(contentField);

		VerticalComponentGroup bottomRow = new VerticalComponentGroup();
		bottomRow.setStyleName("bottom-row");
		bottomRow.setWidth("100%");
		isStickField = new Switch(
				AppContext.getMessage(MessageI18nEnum.FORM_IS_STICK), false);
		bottomRow.addComponent(isStickField);

		attachment = new MessageAttachmentField();

		attachment.setCaption(null);
		bottomRow.addComponent(attachment);

		this.content.addComponent(addFormLayout);

		this.content.addComponent(bottomRow);

		this.saveBtn = new Button(
				AppContext.getMessage(GenericI18Enum.M_BUTTON_DONE));
		this.saveBtn.addStyleName("save-btn");
		this.saveBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -2038682412445718948L;

			@Override
			public void buttonClick(Button.ClickEvent event) {
				final SimpleMessage message = new SimpleMessage();
				message.setProjectid(CurrentProjectVariables.getProjectId());
				message.setPosteddate(new GregorianCalendar().getTime());
				if (!subjectField.getValue().toString().trim().equals("")) {
					message.setTitle(subjectField.getValue());
					message.setMessage(contentField.getValue());
					message.setPosteduser(AppContext.getUsername());
					message.setSaccountid(AppContext.getAccountId());
					message.setIsstick(isStickField.getValue());
					MessageAddViewImpl.this.fireSaveItem(message);

				} else {
					subjectField.addStyleName("errorField");
					NotificationUtil.showErrorNotification(AppContext
							.getMessage(MessageI18nEnum.FORM_TITLE_REQUIRED_ERROR));
				}
			}
		});
		this.setRightComponent(saveBtn);
	}

	@Override
	public void addFormHandler(EditFormHandler<SimpleMessage> handler) {
		handlers.add(handler);
	}

	@Override
	public HasEditFormHandlers<SimpleMessage> getEditFormHandlers() {
		return this;
	}

	private void fireSaveItem(final SimpleMessage message) {
		if (this.handlers != null) {
			for (final EditFormHandler<SimpleMessage> handler : this.handlers) {
				handler.onSave(message);
			}
		}
	}

	@Override
	public void initView() {
		subjectField.setValue("");
		contentField.setValue("");
		isStickField.setValue(false);
	}

	@Override
	protected void onBecomingVisible() {
		super.onBecomingVisible();
		initView();
	}

	@Override
	public ProjectFormAttachmentUploadField getUploadField() {
		return attachment;
	}

}
