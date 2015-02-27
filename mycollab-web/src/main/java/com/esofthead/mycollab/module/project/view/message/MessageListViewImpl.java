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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.AttachmentType;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.Message;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.esofthead.mycollab.module.project.events.MessageEvent;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.module.project.i18n.MessageI18nEnum;
import com.esofthead.mycollab.module.project.service.MessageService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.ProjectListNoItemView;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.events.HasEditFormHandlers;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanPagedList.RowDisplayHandler;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.easyuploads.MultiFileUploadExt;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class MessageListViewImpl extends AbstractPageView implements
		MessageListView, HasEditFormHandlers<Message> {

	private static final long serialVersionUID = 8433776359091397422L;
	private final DefaultBeanPagedList<MessageService, MessageSearchCriteria, SimpleMessage> tableItem;

	private Set<EditFormHandler<Message>> editFormHandlers;

	private MessageSearchCriteria searchCriteria;

	private final TopMessagePanel topMessagePanel;

	private boolean isEmpty;

	public MessageListViewImpl() {
		super();
		this.withMargin(true).withWidth("100%");

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
		this.tableItem = new DefaultBeanPagedList<>(
				ApplicationContextUtil.getSpringBean(MessageService.class),
				new MessageRowDisplayHandler());
		this.tableItem.setStyleName("message-list");
	}

	@Override
	public void addFormHandler(final EditFormHandler<Message> handler) {
		if (this.editFormHandlers == null) {
			this.editFormHandlers = new HashSet<>();
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
		this.removeAllComponents();
		this.searchCriteria = criteria;
		MessageService messageService = ApplicationContextUtil
				.getSpringBean(MessageService.class);
		int totalCount = messageService.getTotalCount(searchCriteria);

		this.isEmpty = !(totalCount > 0);

		this.topMessagePanel.createBasicLayout();
		this.addComponent(topMessagePanel);

		if (this.isEmpty) {
			this.addComponent(new MessageListNoItemView());
		} else {
			this.tableItem.setSearchCriteria(searchCriteria);
			this.addComponent(tableItem);
		}

	}

	private class MessageRowDisplayHandler implements
			RowDisplayHandler<SimpleMessage> {

		@Override
		public Component generateRow(final SimpleMessage message,
				final int rowIndex) {
			final MHorizontalLayout messageLayout = new MHorizontalLayout().withStyleName("message").withWidth("100%");
			if (message.getIsstick() != null && message.getIsstick()) {
				messageLayout.addStyleName("important-message");
			}
			MVerticalLayout userBlock = new MVerticalLayout().withMargin(false).withWidth("80px");
			userBlock.setDefaultComponentAlignment(Alignment.TOP_CENTER);
			ClickListener gotoUser = new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					EventBusFactory.getInstance().post(
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
							EventBusFactory.getInstance().post(
									new MessageEvent.GotoRead(
											MessageListViewImpl.this, message
													.getId()));
						}
					});

			title.setWidth("550px");
			title.setStyleName("link");
			title.addStyleName(UIConstants.WORD_WRAP);

			final MHorizontalLayout messageHeader = new MHorizontalLayout().withMargin(new MarginInfo(true, true,
                    false, true)).withStyleName("message-header");
			final VerticalLayout leftHeader = new VerticalLayout();

			title.addStyleName("message-title");
			leftHeader.addComponent(title);

			final Label timePostLbl = new Label(
					DateTimeUtils.getPrettyDateValue(message.getPosteddate(),
							AppContext.getUserLocale()));
			timePostLbl.setSizeUndefined();
			timePostLbl.setStyleName("time-post");

			Button deleteBtn = new Button("", new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					ConfirmDialogExt.show(
							UI.getCurrent(),
							AppContext.getMessage(
									GenericI18Enum.DIALOG_DELETE_TITLE,
									SiteConfiguration.getSiteName()),
							AppContext
									.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
							AppContext.getMessage(GenericI18Enum.BUTTON_YES),
							AppContext.getMessage(GenericI18Enum.BUTTON_NO),
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
			deleteBtn.setIcon(FontAwesome.TRASH_O);
			deleteBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);
			deleteBtn.setEnabled(CurrentProjectVariables
					.canAccess(ProjectRolePermissionCollections.MESSAGES));

            final MHorizontalLayout rightHeader = new MHorizontalLayout();
            rightHeader.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
			rightHeader.with(timePostLbl, deleteBtn);

			messageHeader.with(leftHeader, rightHeader).expand(leftHeader);

			rowLayout.addComponent(messageHeader);

			final Label messageContent = new Label(
					StringUtils.formatRichText(message.getMessage()),
					ContentMode.HTML);
			messageContent.setStyleName("message-body");
			rowLayout.addComponent(messageContent);

            final MHorizontalLayout notification = new MHorizontalLayout().withStyleName("notification");
            notification.setSizeUndefined();
            if (message.getCommentsCount() > 0) {
                final MHorizontalLayout commentNotification = new MHorizontalLayout();
                final Label commentCountLbl = new Label(
                        Integer.toString(message.getCommentsCount()));
                commentCountLbl.setStyleName("comment-count");
                commentCountLbl.setSizeUndefined();
                commentNotification.addComponent(commentCountLbl);
                final Button commentIcon = new Button(FontAwesome.COMMENTS);
                commentIcon.addStyleName(UIConstants.BUTTON_ICON_ONLY);
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
            if (CollectionUtils.isNotEmpty(attachments)) {
                final HorizontalLayout attachmentNotification = new HorizontalLayout();
                final Label attachmentCountLbl = new Label(
                        Integer.toString(attachments.size()));
                attachmentCountLbl.setStyleName("attachment-count");
                attachmentCountLbl.setSizeUndefined();
                attachmentNotification.addComponent(attachmentCountLbl);
                final Button attachmentIcon = new Button(FontAwesome.PAPERCLIP);
                attachmentIcon.addStyleName(UIConstants.BUTTON_ICON_ONLY);
                attachmentNotification.addComponent(attachmentIcon);
                notification.addComponent(attachmentNotification);
            }

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

			messageLayout.with(rowLayout).expand(rowLayout);

			return messageLayout;
		}
	}

	@SuppressWarnings({ "serial" })
	private class MessageSearchPanel extends GenericSearchPanel<MessageSearchCriteria> {
		private SimpleProject project;
		private MessageSearchCriteria messageSearchCriteria;
        private TextField nameField;

		public MessageSearchPanel() {
			this.project = CurrentProjectVariables.getProject();
		}

		@Override
		public void attach() {
			super.attach();
			this.createBasicSearchLayout();
		}

		private void createBasicSearchLayout() {
			final MHorizontalLayout basicSearchBody = new MHorizontalLayout()
					.withStyleName("message-search");
			basicSearchBody.setSizeUndefined();

			nameField = new TextField();
			nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
            nameField.addShortcutListener(new ShortcutListener("MessageTextSearch", ShortcutAction.KeyCode.ENTER,
                    null) {
                @Override
                public void handleAction(Object o, Object o1) {
                    doSearch();
                }
            });

			basicSearchBody.with(nameField).withAlign(nameField, Alignment.MIDDLE_LEFT);

			final Button searchBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
			searchBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final Button.ClickEvent event) {
					doSearch();
				}
			});
			searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			searchBtn.setIcon(FontAwesome.SEARCH);
			basicSearchBody.addComponent(searchBtn);

			this.setCompositionRoot(basicSearchBody);
		}

        private void doSearch() {
            messageSearchCriteria = new MessageSearchCriteria();
            messageSearchCriteria.setProjectids(new SetSearchField<>(SearchField.AND, project.getId()));
            messageSearchCriteria.setMessage(new StringSearchField(nameField.getValue()));
            notifySearchHandler(messageSearchCriteria);
        }
	}

	private final class TopMessagePanel extends MVerticalLayout {
		private static final long serialVersionUID = 1L;
		private final MessageSearchPanel messageSearchPanel;
		private final HorizontalLayout messagePanelBody;

		public TopMessagePanel() {
			this.withWidth("100%").withStyleName("message-toppanel");
			this.messageSearchPanel = new MessageSearchPanel();
			this.messagePanelBody = new HorizontalLayout();
			this.messagePanelBody.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

			this.messageSearchPanel.setWidth("400px");
			this.messagePanelBody.setStyleName("message-toppanel-body");
			this.messagePanelBody.setWidth("100%");
			this.addComponent(this.messagePanelBody);

			this.createBasicLayout();
		}

		private void createAddMessageLayout() {
			this.messagePanelBody.removeAllComponents();
			final MVerticalLayout addMessageWrapper = new MVerticalLayout().withWidth("700px");

			final RichTextArea ckEditorTextField = new RichTextArea();
            ckEditorTextField.setWidth("100%");
            ckEditorTextField.setHeight("200px");

			final AttachmentPanel attachments = new AttachmentPanel();
			final TextField titleField = new TextField();

			final MHorizontalLayout titleLayout = new MHorizontalLayout().withWidth("100%");
			final Label titleLbl = new Label(AppContext.getMessage(MessageI18nEnum.FORM_TITLE));
			titleLbl.setWidthUndefined();

			titleField.setWidth("100%");
			titleField.setNullRepresentation("");
			titleField.setRequired(true);
            titleField.setRequiredError(AppContext.getMessage(MessageI18nEnum.FORM_TITLE_REQUIRED_ERROR));

			titleLayout.with(titleLbl, titleField).expand(titleField);

            addMessageWrapper.with(titleLayout, ckEditorTextField).withAlign(titleLayout, Alignment.MIDDLE_LEFT)
                    .withAlign(ckEditorTextField, Alignment.MIDDLE_CENTER).expand(ckEditorTextField);

			final MHorizontalLayout controls = new MHorizontalLayout().withWidth("100%");

			final MultiFileUploadExt uploadExt = new MultiFileUploadExt(
					attachments);
			uploadExt.addComponent(attachments);
            controls.with(uploadExt).withAlign(uploadExt, Alignment.TOP_LEFT).expand(uploadExt);

			final CheckBox chkIsStick = new CheckBox(
					AppContext.getMessage(MessageI18nEnum.FORM_IS_STICK));
			controls.with(chkIsStick).withAlign(chkIsStick, Alignment.TOP_RIGHT);

			final Button cancelBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							MessageListViewImpl.this
									.setCriteria(searchCriteria);
						}
					});
			cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
			controls.with(cancelBtn).withAlign(cancelBtn, Alignment.TOP_RIGHT);

			final Button saveBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_POST),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							final Message message = new Message();
							message.setProjectid(CurrentProjectVariables
									.getProjectId());
							message.setPosteddate(new GregorianCalendar()
									.getTime());
							if (!titleField.getValue().trim()
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
			saveBtn.setIcon(FontAwesome.SAVE);

			controls.with(saveBtn).withAlign(saveBtn, Alignment.TOP_RIGHT);

            addMessageWrapper.with(controls).withAlign(controls, Alignment.MIDDLE_CENTER);
			this.messagePanelBody.addComponent(addMessageWrapper);
		}

		public void createBasicLayout() {
			this.messagePanelBody.removeAllComponents();
			this.messagePanelBody.addComponent(this.messageSearchPanel);

			if (!MessageListViewImpl.this.isEmpty) {
				final Button createMessageBtn = new Button(
						AppContext
								.getMessage(MessageI18nEnum.BUTTON_NEW_MESSAGE),
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
				createMessageBtn.setIcon(FontAwesome.PLUS);
				createMessageBtn.setEnabled(CurrentProjectVariables
						.canWrite(ProjectRolePermissionCollections.MESSAGES));

				this.messagePanelBody.addComponent(createMessageBtn);
				this.messagePanelBody.setComponentAlignment(createMessageBtn,
						Alignment.MIDDLE_RIGHT);
			}

		}

		public HasSearchHandlers<MessageSearchCriteria> getSearchHandlers() {
			return this.messageSearchPanel;
		}
	}

	private class MessageListNoItemView extends ProjectListNoItemView {
		private static final long serialVersionUID = 6711716775690122182L;

        @Override
        protected FontAwesome viewIcon() {
            return ProjectAssetsManager.getAsset(ProjectTypeConstants.MESSAGE);
        }

        @Override
        protected String viewTitle() {
            return AppContext.getMessage(MessageI18nEnum.VIEW_NO_ITEM_TITLE);
        }

        @Override
        protected String viewHint() {
            return AppContext.getMessage(MessageI18nEnum.VIEW_NO_ITEM_HINT);
        }

        @Override
        protected String actionMessage() {
            return AppContext.getMessage(MessageI18nEnum.BUTTON_NEW_MESSAGE);
        }

        @Override
        protected Button.ClickListener actionListener() {
            return new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    MessageListViewImpl.this.createAddMessageLayout();
                }
            };
        }

        @Override
        protected boolean hasPermission() {
            return CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MESSAGES);
        }
	}

	public void createAddMessageLayout() {
		this.removeAllComponents();
		topMessagePanel.createAddMessageLayout();
		this.addComponent(topMessagePanel);
	}
}
