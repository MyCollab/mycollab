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
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.Message;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.MessageI18nEnum;
import com.esofthead.mycollab.module.project.service.MessageService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.ProjectListNoItemView;
import com.esofthead.mycollab.module.project.ui.components.ProjectMemberBlock;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.events.HasEditFormHandlers;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanPagedList.RowDisplayHandler;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Text;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.easyuploads.MultiFileUploadExt;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class MessageListViewImpl extends AbstractPageView implements
        MessageListView, HasEditFormHandlers<Message> {
    private static final long serialVersionUID = 8433776359091397422L;

    private DefaultBeanPagedList<MessageService, MessageSearchCriteria, SimpleMessage> tableItem;
    private Set<EditFormHandler<Message>> editFormHandlers;
    private MessageSearchCriteria searchCriteria;
    private TopMessagePanel topMessagePanel;
    private boolean isEmpty;

    public MessageListViewImpl() {
        super();
        this.withSpacing(true).withMargin(true).withWidth("100%");

        topMessagePanel = new TopMessagePanel();
        topMessagePanel.getSearchHandlers().addSearchHandler(
                new SearchHandler<MessageSearchCriteria>() {
                    @Override
                    public void onSearch(final MessageSearchCriteria criteria) {
                        tableItem.setSearchCriteria(criteria);
                    }
                });
        tableItem = new DefaultBeanPagedList<>(
                ApplicationContextUtil.getSpringBean(MessageService.class),
                new MessageRowDisplayHandler());
        tableItem.setStyleName("message-list");
        tableItem.setControlStyle("borderlessControl");
    }

    @Override
    public void addFormHandler(final EditFormHandler<Message> handler) {
        if (editFormHandlers == null) {
            editFormHandlers = new HashSet<>();
        }
        editFormHandlers.add(handler);
    }

    private void fireSaveItem(final Message message) {
        if (editFormHandlers != null) {
            for (EditFormHandler<Message> handler : editFormHandlers) {
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
            addComponent(new MessageListNoItemView());
        } else {
            tableItem.setSearchCriteria(searchCriteria);
            addComponent(tableItem);
        }

    }

    private class MessageRowDisplayHandler implements RowDisplayHandler<SimpleMessage> {

        @Override
        public Component generateRow(AbstractBeanPagedList host, final SimpleMessage message, int rowIndex) {
            final MHorizontalLayout messageLayout = new MHorizontalLayout().withMargin(new MarginInfo(true, false,
                    true, false)).withStyleName("message").withWidth("100%");
            if (message.getIsstick() != null && message.getIsstick()) {
                messageLayout.addStyleName("important-message");
            }

            ProjectMemberBlock userBlock = new ProjectMemberBlock(message.getPosteduser(), message.getPostedUserAvatarId(),
                    message.getFullPostedUserName());
            messageLayout.addComponent(userBlock);

            CssLayout rowLayout = new CssLayout();
            rowLayout.setStyleName("message-container");
            rowLayout.setWidth("100%");

            A labelLink = new A(ProjectLinkBuilder.generateMessagePreviewFullLink(message.getProjectid(), message
                    .getId()), new Text(message.getTitle()));

            MHorizontalLayout messageHeader = new MHorizontalLayout().withMargin(new MarginInfo(true, true,
                    false, true)).withStyleName("message-header");
            VerticalLayout leftHeader = new VerticalLayout();
            leftHeader.addComponent(new ELabel(labelLink.write(), ContentMode.HTML).withStyleName("h2"));
            ELabel timePostLbl = new ELabel().prettyDateTime(message.getPosteddate());
            timePostLbl.setSizeUndefined();
            timePostLbl.setStyleName("time-post");

            Button deleteBtn = new Button("", new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    ConfirmDialogExt.show(UI.getCurrent(),
                            AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE,
                                    AppContext.getSiteName()),
                            AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                            AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                            AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                            new ConfirmDialog.Listener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void onClose(final ConfirmDialog dialog) {
                                    if (dialog.isConfirmed()) {
                                        MessageService messageService = ApplicationContextUtil
                                                .getSpringBean(MessageService.class);
                                        messageService.removeWithSession(message,
                                                AppContext.getUsername(), AppContext.getAccountId());
                                        tableItem.setSearchCriteria(searchCriteria);
                                    }
                                }
                            });
                }
            });
            deleteBtn.setIcon(FontAwesome.TRASH_O);
            deleteBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);
            deleteBtn.setEnabled(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.MESSAGES));

            MHorizontalLayout rightHeader = new MHorizontalLayout();
            rightHeader.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
            rightHeader.with(timePostLbl, deleteBtn);

            messageHeader.with(leftHeader, rightHeader).expand(leftHeader);

            rowLayout.addComponent(messageHeader);

            SafeHtmlLabel messageContent = new SafeHtmlLabel(message.getMessage());
            messageContent.setStyleName("message-body");
            rowLayout.addComponent(messageContent);

            MHorizontalLayout notification = new MHorizontalLayout().withStyleName("notification");
            notification.setSizeUndefined();
            if (message.getCommentsCount() > 0) {
                MHorizontalLayout commentNotification = new MHorizontalLayout();
                Label commentCountLbl = new Label(Integer.toString(message.getCommentsCount()));
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
            List<Content> attachments = attachmentService.getContents(AttachmentUtils
                    .getProjectEntityAttachmentPath(AppContext.getAccountId(),
                            message.getProjectid(), ProjectTypeConstants.MESSAGE, "" + message.getId()));
            if (CollectionUtils.isNotEmpty(attachments)) {
                HorizontalLayout attachmentNotification = new HorizontalLayout();
                Label attachmentCountLbl = new Label(Integer.toString(attachments.size()));
                attachmentCountLbl.setStyleName("attachment-count");
                attachmentCountLbl.setSizeUndefined();
                attachmentNotification.addComponent(attachmentCountLbl);
                Button attachmentIcon = new Button(FontAwesome.PAPERCLIP);
                attachmentIcon.addStyleName(UIConstants.BUTTON_ICON_ONLY);
                attachmentNotification.addComponent(attachmentIcon);
                notification.addComponent(attachmentNotification);
            }

            if (notification.getComponentCount() > 0) {
                MVerticalLayout messageFooter = new MVerticalLayout().withSpacing(false).withWidth("100%")
                        .withStyleName("message-footer").with(notification).withAlign(notification, Alignment.MIDDLE_RIGHT);
                rowLayout.addComponent(messageFooter);
            }

            messageLayout.with(rowLayout).expand(rowLayout);
            return messageLayout;
        }
    }

    @SuppressWarnings({"serial"})
    private static class MessageSearchPanel extends GenericSearchPanel<MessageSearchCriteria> {
        private MessageSearchCriteria messageSearchCriteria;
        private TextField nameField;

        public MessageSearchPanel() {
            createBasicSearchLayout();
        }

        @Override
        public void setTotalCountNumber(int totalCountNumber) {

        }

        private void createBasicSearchLayout() {
            final MHorizontalLayout basicSearchBody = new MHorizontalLayout();
            basicSearchBody.setSizeUndefined();

            nameField = ShortcutExtension.installShortcutAction(new TextField(),
                    new ShortcutListener("MessageTextSearch", ShortcutAction.KeyCode.ENTER, null) {
                        @Override
                        public void handleAction(Object o, Object o1) {
                            doSearch();
                        }
                    });
            nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);

            Button searchBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
            searchBtn.addClickListener(new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    doSearch();
                }
            });
            searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
            searchBtn.setIcon(FontAwesome.SEARCH);

            basicSearchBody.with(nameField, searchBtn).withAlign(nameField, Alignment.MIDDLE_LEFT);

            this.setCompositionRoot(basicSearchBody);
        }

        private void doSearch() {
            messageSearchCriteria = new MessageSearchCriteria();
            messageSearchCriteria.setProjectids(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
            messageSearchCriteria.setMessage(new StringSearchField(nameField.getValue()));
            notifySearchHandler(messageSearchCriteria);
        }
    }

    private final class TopMessagePanel extends MVerticalLayout {
        private static final long serialVersionUID = 1L;
        private MessageSearchPanel messageSearchPanel;
        private MHorizontalLayout messagePanelBody;

        public TopMessagePanel() {
            this.withWidth("100%").withStyleName("message-toppanel");
            this.messagePanelBody = new MHorizontalLayout().withSpacing(false)
                    .withStyleName("message-toppanel-body").withWidth("100%");
            this.messagePanelBody.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

            this.messageSearchPanel = new MessageSearchPanel();
            this.messageSearchPanel.setWidth("400px");
            this.addComponent(this.messagePanelBody);

            this.createBasicLayout();
        }

        private void createAddMessageLayout() {
            this.messagePanelBody.removeAllComponents();
            MVerticalLayout addMessageWrapper = new MVerticalLayout().withWidth("700px");

            final RichTextArea ckEditorTextField = new RichTextArea();
            ckEditorTextField.setWidth("100%");
            ckEditorTextField.setHeight("200px");

            final AttachmentPanel attachments = new AttachmentPanel();
            final TextField titleField = new TextField();

            MHorizontalLayout titleLayout = new MHorizontalLayout().withWidth("100%");
            Label titleLbl = new Label(AppContext.getMessage(MessageI18nEnum.FORM_TITLE));
            titleLbl.setWidthUndefined();
            titleField.setWidth("100%");
            titleField.setNullRepresentation("");
            titleField.setRequired(true);
            titleField.setRequiredError(AppContext.getMessage(MessageI18nEnum.FORM_TITLE_REQUIRED_ERROR));

            titleLayout.with(titleLbl, titleField).expand(titleField);

            addMessageWrapper.with(titleLayout, ckEditorTextField).withAlign(titleLayout, Alignment.MIDDLE_LEFT)
                    .withAlign(ckEditorTextField, Alignment.MIDDLE_CENTER).expand(ckEditorTextField);

            MHorizontalLayout controls = new MHorizontalLayout().withWidth("100%");

            MultiFileUploadExt uploadExt = new MultiFileUploadExt(attachments);
            uploadExt.addComponent(attachments);
            controls.with(uploadExt).withAlign(uploadExt, Alignment.TOP_LEFT).expand(uploadExt);

            final CheckBox chkIsStick = new CheckBox(
                    AppContext.getMessage(MessageI18nEnum.FORM_IS_STICK));
            controls.with(chkIsStick).withAlign(chkIsStick, Alignment.TOP_RIGHT);

            Button cancelBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            MessageListViewImpl.this.setCriteria(searchCriteria);
                        }
                    });
            cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
            controls.with(cancelBtn).withAlign(cancelBtn, Alignment.TOP_RIGHT);

            Button saveBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_POST),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            Message message = new Message();
                            message.setProjectid(CurrentProjectVariables.getProjectId());
                            message.setPosteddate(new GregorianCalendar().getTime());
                            if (!titleField.getValue().trim().equals("")) {
                                message.setTitle(titleField.getValue());
                                message.setMessage(ckEditorTextField.getValue());
                                message.setPosteduser(AppContext.getUsername());
                                message.setSaccountid(AppContext.getAccountId());
                                message.setIsstick(chkIsStick.getValue());
                                MessageListViewImpl.this.fireSaveItem(message);

                                String attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(
                                        AppContext.getAccountId(), message.getProjectid(),
                                        ProjectTypeConstants.MESSAGE, "" + message.getId());
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
                Button createMessageBtn = new Button(AppContext.getMessage(MessageI18nEnum.BUTTON_NEW_MESSAGE),
                        new Button.ClickListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void buttonClick(final ClickEvent event) {
                                TopMessagePanel.this.createAddMessageLayout();
                            }
                        });
                createMessageBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MESSAGES));
                createMessageBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
                createMessageBtn.setIcon(FontAwesome.PLUS);
                createMessageBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MESSAGES));

                this.messagePanelBody.addComponent(createMessageBtn);
                this.messagePanelBody.setComponentAlignment(createMessageBtn, Alignment.MIDDLE_RIGHT);
            }

        }

        public HasSearchHandlers<MessageSearchCriteria> getSearchHandlers() {
            return this.messageSearchPanel;
        }
    }

    public void createAddMessageLayout() {
        removeAllComponents();
        topMessagePanel.createAddMessageLayout();
        addComponent(topMessagePanel);
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
}
