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
package com.esofthead.mycollab.module.project.view.message;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.easyuploads.MultiFileUploadExt;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.Message;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.esofthead.mycollab.module.project.events.MessageEvent;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.module.project.localization.MessageI18nEnum;
import com.esofthead.mycollab.module.project.service.MessageService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.events.HasEditFormHandlers;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanPagedList.RowDisplayHandler;
import com.esofthead.mycollab.vaadin.ui.AttachmentPanel;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.DefaultBeanPagedList;
import com.esofthead.mycollab.vaadin.ui.GenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent
public class MessageListViewImpl extends AbstractPageView implements
		MessageListView, HasEditFormHandlers<Message> {

	private static final long serialVersionUID = 8433776359091397422L;
	private final DefaultBeanPagedList<MessageService, MessageSearchCriteria, SimpleMessage> tableItem;

	private Set<EditFormHandler<Message>> editFormHandlers;

	private MessageSearchCriteria searchCriteria;

	private final TopMessagePanel topMessagePanel;

	public MessageListViewImpl() {
		super();
		this.setWidth("100%");
		this.setMargin(true);

		this.topMessagePanel = new TopMessagePanel();
		this.topMessagePanel.setWidth("100%");

		this.topMessagePanel.getSearchHandlers().addSearchHandler(
				new SearchHandler<MessageSearchCriteria>() {
					@Override
					public void onSearch(final MessageSearchCriteria criteria) {
						MessageListViewImpl.this.tableItem
								.setSearchCriteria(criteria);
					}
				});
		this.addComponent(this.topMessagePanel);
		this.tableItem = new DefaultBeanPagedList<MessageService, MessageSearchCriteria, SimpleMessage>(
				ApplicationContextUtil.getSpringBean(MessageService.class),
				new MessageRowDisplayHandler());
		this.tableItem.setStyleName("message-list");
		this.addComponent(this.tableItem);
	}

	@Override
	public void addFormHandler(final EditFormHandler<Message> handler) {
		if (this.editFormHandlers == null) {
			this.editFormHandlers = new HashSet<EditFormHandler<Message>>();
		}
		this.editFormHandlers.add(handler);
	}

	private void fireSaveItem(final Message message) {
		if (this.editFormHandlers != null) {
			for (final EditFormHandler<Message> handler : this.editFormHandlers) {
				handler.onSave(message);
			}
		}
	}

	@Override
	public HasEditFormHandlers<Message> getEditFormHandlers() {
		return this;
	}

	@Override
	public void setCriteria(final MessageSearchCriteria criteria) {
		this.searchCriteria = criteria;
		this.topMessagePanel.createBasicLayout();
		this.tableItem.setSearchCriteria(this.searchCriteria);

	}

	private class MessageRowDisplayHandler implements
			RowDisplayHandler<SimpleMessage> {

		@Override
		public Component generateRow(final SimpleMessage message,
				final int rowIndex) {
			final HorizontalLayout messageLayout = new HorizontalLayout();
			messageLayout.setStyleName("message");
			messageLayout.setSpacing(true);
			if (message.getIsstick() != null && message.getIsstick()) {
				messageLayout.addStyleName("important-message");
			}
			messageLayout.setWidth("100%");
			VerticalLayout userBlock = new VerticalLayout();
			userBlock.setDefaultComponentAlignment(Alignment.TOP_CENTER);
			userBlock.setWidth("80px");
			userBlock.setSpacing(true);
			ClickListener gotoUser = new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					EventBus.getInstance().fireEvent(
							new ProjectMemberEvent.GotoRead(
									MessageListViewImpl.this, message
											.getPosteduser()));
				}
			};
			Button userAvatarBtn = UserAvatarControlFactory
					.createUserAvatarButtonLink(
							message.getPostedUserAvatarId(),
							message.getFullPostedUserName());
			userAvatarBtn.addClickListener(gotoUser);
			userBlock.addComponent(userAvatarBtn);
			Button userName = new Button(message.getFullPostedUserName());
			userName.setStyleName("user-name");
			userName.addStyleName("link");
			userName.addStyleName(UIConstants.WORD_WRAP);
			userName.addClickListener(gotoUser);
			userBlock.addComponent(userName);
			messageLayout.addComponent(userBlock);

			final CssLayout rowLayout = new CssLayout();
			rowLayout.setStyleName("message-container");
			rowLayout.setWidth("100%");
			final Button title = new Button(message.getTitle(),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							EventBus.getInstance().fireEvent(
									new MessageEvent.GotoRead(
											MessageListViewImpl.this, message
													.getId()));
						}
					});

			title.setWidth("550px");
			title.setStyleName("link");
			title.addStyleName(UIConstants.WORD_WRAP);

			final HorizontalLayout messageHeader = new HorizontalLayout();
			messageHeader.setStyleName("message-header");
			messageHeader.setMargin(new MarginInfo(true, true, false, true));
			final VerticalLayout leftHeader = new VerticalLayout();

			title.addStyleName("message-title");
			leftHeader.addComponent(title);

			final HorizontalLayout rightHeader = new HorizontalLayout();
			rightHeader.setSpacing(true);

			final Label timePostLbl = new Label(
					DateTimeUtils.getStringDateFromNow(message.getPosteddate()));
			timePostLbl.setSizeUndefined();
			timePostLbl.setStyleName("time-post");

			final HorizontalLayout notification = new HorizontalLayout();
			notification.setStyleName("notification");
			notification.setSizeUndefined();
			notification.setSpacing(true);
			if (message.getCommentsCount() > 0) {
				final HorizontalLayout commentNotification = new HorizontalLayout();
				final Label commentCountLbl = new Label(
						Integer.toString(message.getCommentsCount()));
				commentCountLbl.setStyleName("comment-count");
				commentCountLbl.setSizeUndefined();
				commentNotification.addComponent(commentCountLbl);
				final Image commentIcon = new Image(null,
						MyCollabResource
								.newResource("icons/16/project/message.png"));
				commentNotification.addComponent(commentIcon);

				notification.addComponent(commentNotification);

			}
			ResourceService attachmentService = ApplicationContextUtil
					.getSpringBean(ResourceService.class);
			List<Content> attachments = attachmentService
					.getContents(AttachmentUtils
							.getProjectEntityAttachmentPath(
									AppContext.getAccountId(),
									message.getProjectid(),
									AttachmentType.PROJECT_MESSAGE,
									message.getId()));
			if (attachments != null && !attachments.isEmpty()) {
				final HorizontalLayout attachmentNotification = new HorizontalLayout();
				final Label attachmentCountLbl = new Label(
						Integer.toString(attachments.size()));
				attachmentCountLbl.setStyleName("attachment-count");
				attachmentCountLbl.setSizeUndefined();
				attachmentNotification.addComponent(attachmentCountLbl);
				final Image attachmentIcon = new Image(null,
						MyCollabResource.newResource("icons/16/attachment.png"));
				attachmentNotification.addComponent(attachmentIcon);

				notification.addComponent(attachmentNotification);

			}

			Button deleteBtn = new Button("", new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					ConfirmDialogExt.show(
							UI.getCurrent(),
							AppContext.getMessage(
									GenericI18Enum.DELETE_DIALOG_TITLE,
									SiteConfiguration.getSiteName()),
							AppContext
									.getMessage(GenericI18Enum.CONFIRM_DELETE_RECORD_DIALOG_MESSAGE),
							AppContext
									.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
							AppContext
									.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
							new ConfirmDialog.Listener() {
								private static final long serialVersionUID = 1L;

								@Override
								public void onClose(final ConfirmDialog dialog) {
									if (dialog.isConfirmed()) {
										final MessageService messageService = ApplicationContextUtil
												.getSpringBean(MessageService.class);
										messageService.removeWithSession(
												message.getId(),
												AppContext.getUsername(),
												AppContext.getAccountId());
										MessageListViewImpl.this.tableItem
												.setSearchCriteria(searchCriteria);
									}
								}
							});
				}
			});
			deleteBtn.setIcon(MyCollabResource
					.newResource("icons/12/project/icon_x.png"));
			deleteBtn.addStyleName("link");
			deleteBtn.setEnabled(CurrentProjectVariables
					.canAccess(ProjectRolePermissionCollections.MESSAGES));

			rightHeader.addComponent(timePostLbl);
			rightHeader.addComponent(deleteBtn);
			rightHeader.setExpandRatio(timePostLbl, 1.0f);

			messageHeader.addComponent(leftHeader);
			messageHeader.setExpandRatio(leftHeader, 1.0f);
			messageHeader.addComponent(rightHeader);
			messageHeader.setWidth("100%");

			rowLayout.addComponent(messageHeader);

			final Label messageContent = new Label(
					StringUtils.formatExtraLink(message.getMessage()),
					ContentMode.HTML);
			messageContent.setStyleName("message-body");
			rowLayout.addComponent(messageContent);

			if (notification.getComponentCount() > 0) {
				VerticalLayout messageFooter = new VerticalLayout();
				messageFooter.setWidth("100%");
				messageFooter.setStyleName("message-footer");
				messageFooter.addComponent(notification);
				messageFooter.setMargin(true);
				messageFooter.setComponentAlignment(notification,
						Alignment.MIDDLE_RIGHT);
				rowLayout.addComponent(messageFooter);
			}

			messageLayout.addComponent(rowLayout);
			messageLayout.setExpandRatio(rowLayout, 1.0f);

			return messageLayout;
		}
	}

	@SuppressWarnings({ "serial" })
	private class MessageSearchPanel extends
			GenericSearchPanel<MessageSearchCriteria> {

		private final SimpleProject project;
		private MessageSearchCriteria messageSearchCriteria;
		private String textSearch = "";

		public MessageSearchPanel() {
			this.project = CurrentProjectVariables.getProject();
		}

		@Override
		public void attach() {
			super.attach();
			this.createBasicSearchLayout();
		}

		private void createBasicSearchLayout() {
			final HorizontalLayout basicSearchBody = new HorizontalLayout();
			basicSearchBody.setStyleName("message-search");
			basicSearchBody.setSizeUndefined();
			basicSearchBody.setSpacing(true);

			final TextField nameField = new TextField();
			nameField.addTextChangeListener(new TextChangeListener() {
				@Override
				public void textChange(final TextChangeEvent event) {
					MessageSearchPanel.this.messageSearchCriteria = new MessageSearchCriteria();

					MessageSearchPanel.this.messageSearchCriteria
							.setProjectids(new SetSearchField<Integer>(
									SearchField.AND,
									MessageSearchPanel.this.project.getId()));

					MessageSearchPanel.this.textSearch = event.getText()
							.toString().trim();

					MessageSearchPanel.this.messageSearchCriteria
							.setMessage(new StringSearchField(
									MessageSearchPanel.this.textSearch));

					MessageSearchPanel.this
							.notifySearchHandler(MessageSearchPanel.this.messageSearchCriteria);
				}
			});

			nameField.setTextChangeEventMode(TextChangeEventMode.LAZY);
			nameField.setTextChangeTimeout(200);
			nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
			UiUtils.addComponent(basicSearchBody, nameField,
					Alignment.MIDDLE_LEFT);

			final Button searchBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH_LABEL));
			searchBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final Button.ClickEvent event) {
					MessageSearchPanel.this.messageSearchCriteria = new MessageSearchCriteria();

					MessageSearchPanel.this.messageSearchCriteria
							.setProjectids(new SetSearchField<Integer>(
									SearchField.AND,
									MessageSearchPanel.this.project.getId()));

					MessageSearchPanel.this.messageSearchCriteria
							.setMessage(new StringSearchField(
									MessageSearchPanel.this.textSearch));

					MessageSearchPanel.this
							.notifySearchHandler(MessageSearchPanel.this.messageSearchCriteria);
				}
			});
			searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			searchBtn.setIcon(MyCollabResource
					.newResource("icons/16/search.png"));
			basicSearchBody.addComponent(searchBtn);

			this.setCompositionRoot(basicSearchBody);
		}
	}

	private final class TopMessagePanel extends VerticalLayout {

		private static final long serialVersionUID = 1L;
		private final MessageSearchPanel messageSearchPanel;
		private final HorizontalLayout messagePanelBody;

		public TopMessagePanel() {
			this.setWidth("100%");
			this.setMargin(true);
			this.setStyleName("message-toppanel");
			this.messageSearchPanel = new MessageSearchPanel();
			this.messagePanelBody = new HorizontalLayout();
			this.messagePanelBody
					.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

			this.messageSearchPanel.setWidth("400px");
			this.messagePanelBody.setStyleName("message-toppanel-body");
			this.messagePanelBody.setWidth("100%");
			this.addComponent(this.messagePanelBody);

			this.createBasicLayout();
		}

		private void createAddMessageLayout() {
			this.messagePanelBody.removeAllComponents();

			final VerticalLayout addMessageWrapper = new VerticalLayout();
			addMessageWrapper.setSpacing(true);
			addMessageWrapper.setWidth("500px");

			final RichTextArea ckEditorTextField = new RichTextArea();
			final AttachmentPanel attachments = new AttachmentPanel();
			final TextField titleField = new TextField();

			final HorizontalLayout titleLayout = new HorizontalLayout();
			titleLayout.setSpacing(true);
			final Label titleLbl = new Label(
					AppContext.getMessage(MessageI18nEnum.FORM_TITLE_FIELD));
			titleLbl.setWidth(SIZE_UNDEFINED, Sizeable.Unit.PIXELS);

			titleField.setWidth("100%");
			titleField.setNullRepresentation("");
			titleField.setRequired(true);
			titleField.setRequiredError(AppContext
					.getMessage(MessageI18nEnum.FORM_TITLE_REQUIRED_ERROR));

			titleLayout.addComponent(titleLbl);
			titleLayout.addComponent(titleField);
			titleLayout.setExpandRatio(titleField, 1.0f);
			titleLayout.setWidth("100%");

			addMessageWrapper.addComponent(titleLayout);
			addMessageWrapper.setComponentAlignment(titleLayout,
					Alignment.MIDDLE_LEFT);

			ckEditorTextField.setWidth("100%");
			ckEditorTextField.setHeight("200px");
			addMessageWrapper.addComponent(ckEditorTextField);
			addMessageWrapper.setExpandRatio(ckEditorTextField, 1.0f);
			addMessageWrapper.setComponentAlignment(ckEditorTextField,
					Alignment.MIDDLE_CENTER);

			final HorizontalLayout controls = new HorizontalLayout();
			controls.setWidth("100%");
			controls.setSpacing(true);

			final MultiFileUploadExt uploadExt = new MultiFileUploadExt(
					attachments);
			uploadExt.addComponent(attachments);
			controls.addComponent(uploadExt);
			controls.setExpandRatio(uploadExt, 1.0f);
			controls.setComponentAlignment(uploadExt, Alignment.MIDDLE_LEFT);

			final CheckBox chkIsStick = new CheckBox(
					AppContext.getMessage(MessageI18nEnum.FORM_IS_STICK_FIELD));
			controls.addComponent(chkIsStick);
			controls.setComponentAlignment(chkIsStick, Alignment.MIDDLE_CENTER);

			final Button cancelBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							TopMessagePanel.this.createBasicLayout();
						}
					});
			cancelBtn.setStyleName(UIConstants.THEME_BLANK_LINK);
			controls.addComponent(cancelBtn);
			controls.setComponentAlignment(cancelBtn, Alignment.MIDDLE_CENTER);

			final Button saveBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_POST_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							final Message message = new Message();
							message.setProjectid(CurrentProjectVariables
									.getProjectId());
							message.setPosteddate(new GregorianCalendar()
									.getTime());
							if (!titleField.getValue().toString().trim()
									.equals("")) {
								message.setTitle(titleField.getValue());
								message.setMessage(ckEditorTextField.getValue());
								message.setPosteduser(AppContext.getUsername());
								message.setSaccountid(AppContext.getAccountId());
								message.setIsstick(chkIsStick.getValue());
								MessageListViewImpl.this.fireSaveItem(message);

								String attachmentPath = AttachmentUtils
										.getProjectEntityAttachmentPath(
												AppContext.getAccountId(),
												message.getProjectid(),
												AttachmentType.PROJECT_MESSAGE,
												message.getId());
								attachments.saveContentsToRepo(attachmentPath);
							} else {
								titleField.addStyleName("errorField");
								NotificationUtil.showErrorNotification(AppContext
										.getMessage(MessageI18nEnum.FORM_TITLE_REQUIRED_ERROR));
							}
						}
					});
			saveBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			saveBtn.setIcon(MyCollabResource.newResource("icons/16/save.png"));
			controls.addComponent(saveBtn);
			controls.setComponentAlignment(saveBtn, Alignment.MIDDLE_CENTER);

			addMessageWrapper.addComponent(controls);
			addMessageWrapper.setComponentAlignment(controls,
					Alignment.MIDDLE_CENTER);
			this.messagePanelBody.addComponent(addMessageWrapper);
		}

		public void createBasicLayout() {
			this.messagePanelBody.removeAllComponents();
			this.messagePanelBody.addComponent(this.messageSearchPanel);

			final Button createMessageBtn = new Button(
					AppContext.getMessage(MessageI18nEnum.NEW_MESSAGE_ACTION),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							TopMessagePanel.this.createAddMessageLayout();
						}
					});
			createMessageBtn.setEnabled(CurrentProjectVariables
					.canWrite(ProjectRolePermissionCollections.MESSAGES));
			createMessageBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			createMessageBtn.setIcon(MyCollabResource
					.newResource("icons/16/addRecord.png"));
			createMessageBtn.setEnabled(CurrentProjectVariables
					.canWrite(ProjectRolePermissionCollections.MESSAGES));

			this.messagePanelBody.addComponent(createMessageBtn);
			this.messagePanelBody.setComponentAlignment(createMessageBtn,
					Alignment.MIDDLE_RIGHT);
		}

		public HasSearchHandlers<MessageSearchCriteria> getSearchHandlers() {
			return this.messageSearchPanel;
		}
	}
}
