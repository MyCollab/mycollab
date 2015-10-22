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
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.i18n.MessageI18nEnum;
import com.esofthead.mycollab.module.project.service.MessageService;
import com.esofthead.mycollab.module.project.ui.components.CommentDisplay;
import com.esofthead.mycollab.module.project.ui.components.ProjectAttachmentDisplayComponentFactory;
import com.esofthead.mycollab.module.project.ui.components.ProjectMemberBlock;
import com.esofthead.mycollab.module.project.ui.components.ProjectViewHeader;
import com.esofthead.mycollab.schedule.email.project.MessageRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class MessageReadViewImpl extends AbstractPageView implements MessageReadView {
    private static final long serialVersionUID = 1L;

    private AdvancedPreviewBeanForm<SimpleMessage> previewForm;
    private SimpleMessage message;
    private CssLayout contentWrapper;
    private MHorizontalLayout header;
    private CommentDisplay commentDisplay;
    private CheckBox stickyCheck;

    public MessageReadViewImpl() {
        super();

        header = new MHorizontalLayout().withMargin(true).withStyleName("hdr-view").withWidth("100%");
        previewForm = new AdvancedPreviewBeanForm<>();

        contentWrapper = new CssLayout();
        contentWrapper.setStyleName("content-wrapper");
        contentWrapper.addComponent(previewForm);
        contentWrapper.setWidth("900px");
        with(header, contentWrapper).expand(contentWrapper);
    }

    @Override
    public HasPreviewFormHandlers<SimpleMessage> getPreviewFormHandlers() {
        return previewForm;
    }

    @Override
    public void previewItem(SimpleMessage item) {
        this.message = item;
        previewForm.setFormLayoutFactory(new FormLayoutFactory());
        previewForm.setBeanFormFieldFactory(new AbstractBeanFieldGroupViewFieldFactory<SimpleMessage>(previewForm) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Field<?> onCreateField(Object propertyId) {
                return null;
            }
        });
        previewForm.setBean(item);
    }

    @Override
    public SimpleMessage getItem() {
        return message;
    }

    class FormLayoutFactory implements IFormLayoutFactory {
        private static final long serialVersionUID = 1L;

        @Override
        public ComponentContainer getLayout() {
            header.removeAllComponents();
            MVerticalLayout messageAddLayout = new MVerticalLayout().withMargin(false).withWidth("100%");

            Button deleteBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_DELETE), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    ConfirmDialogExt.show(UI.getCurrent(),
                            AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                            AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                            AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                            AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                            new ConfirmDialog.Listener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void onClose(
                                        final ConfirmDialog dialog) {
                                    if (dialog.isConfirmed()) {
                                        MessageService messageService = ApplicationContextUtil.getSpringBean(MessageService.class);
                                        messageService.removeWithSession(message, AppContext.getUsername(), AppContext.getAccountId());
                                        previewForm.fireCancelForm(message);
                                    }
                                }
                            });
                }
            });
            deleteBtn.setIcon(FontAwesome.TRASH_O);
            deleteBtn.addStyleName(UIConstants.THEME_RED_LINK);
            deleteBtn.setEnabled(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.MESSAGES));

            stickyCheck = new CheckBox(AppContext.getMessage(MessageI18nEnum.FORM_IS_STICK), message.getIsstick());
            stickyCheck.addValueChangeListener(new ValueChangeListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void valueChange(ValueChangeEvent event) {
                    if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MESSAGES)) {
                        message.setIsstick(stickyCheck.getValue());
                        message.setSaccountid(AppContext.getAccountId());
                        MessageService messageService = ApplicationContextUtil.getSpringBean(MessageService.class);
                        messageService.updateWithSession(message, AppContext.getUsername());
                    } else {
                        NotificationUtil.showMessagePermissionAlert();
                    }

                }
            });

            ProjectViewHeader headerText = new ProjectViewHeader(ProjectTypeConstants.MESSAGE, message.getTitle());
            header.with(headerText, stickyCheck, deleteBtn).withAlign(headerText, Alignment.MIDDLE_LEFT)
                    .withAlign(stickyCheck, Alignment.MIDDLE_RIGHT).withAlign(deleteBtn, Alignment.MIDDLE_RIGHT).expand(headerText);

            MHorizontalLayout messageLayout = new MHorizontalLayout().withStyleName("message").withWidth("100%");

            if (message.getIsstick() != null && message.getIsstick()) {
                messageLayout.addStyleName("important-message");
            }

            ProjectMemberBlock userBlock = new ProjectMemberBlock(message.getPosteduser(), message.getPostedUserAvatarId(),
                    message.getFullPostedUserName());

            messageLayout.addComponent(userBlock);

            MVerticalLayout rowLayout = new MVerticalLayout().withMargin(true).withWidth("100%").withStyleName("message-container");

            MHorizontalLayout messageHeader = new MHorizontalLayout().withStyleName("message-header").withWidth("100%");
            messageHeader.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

            ELabel timePostLbl = new ELabel(AppContext.getMessage(
                    MessageI18nEnum.USER_COMMENT_ADD, message.getFullPostedUserName(),
                    AppContext.formatPrettyTime(message.getPosteddate())), ContentMode.HTML).withDescription
                    (AppContext.formatDateTime(message.getPosteddate()));
            timePostLbl.setSizeUndefined();
            timePostLbl.setStyleName("time-post");

            messageHeader.with(timePostLbl).expand(timePostLbl);

            rowLayout.addComponent(messageHeader);

            SafeHtmlLabel messageContent = new SafeHtmlLabel(message.getMessage());
            messageContent.setStyleName("message-body");
            rowLayout.addComponent(messageContent);

            ResourceService attachmentService = ApplicationContextUtil.getSpringBean(ResourceService.class);
            List<Content> attachments = attachmentService.getContents(AttachmentUtils.getProjectEntityAttachmentPath(
                    AppContext.getAccountId(), message.getProjectid(), ProjectTypeConstants.MESSAGE, "" + message.getId()));
            if (CollectionUtils.isNotEmpty(attachments)) {
                HorizontalLayout attachmentField = new HorizontalLayout();
                Button attachmentIcon = new Button(null, FontAwesome.PAPERCLIP);
                attachmentIcon.addStyleName(UIConstants.BUTTON_ICON_ONLY);
                attachmentField.addComponent(attachmentIcon);

                Label lbAttachment = new Label(AppContext.getMessage(MessageI18nEnum.FORM_ATTACHMENT_FIELD));
                attachmentField.addComponent(lbAttachment);

                Component attachmentDisplayComp = ProjectAttachmentDisplayComponentFactory
                        .getAttachmentDisplayComponent(message.getProjectid(), ProjectTypeConstants.MESSAGE, message.getId());

                MVerticalLayout messageFooter = new MVerticalLayout().withWidth("100%").withStyleName("message-footer")
                        .with(attachmentField, attachmentDisplayComp);

                rowLayout.addComponent(messageFooter);
            }

            messageLayout.with(rowLayout).expand(rowLayout);
            messageAddLayout.addComponent(messageLayout);

            if (commentDisplay != null && commentDisplay.getParent() == contentWrapper) {
                contentWrapper.removeComponent(commentDisplay);
            }
            commentDisplay = createCommentPanel();
            contentWrapper.addComponent(commentDisplay);

            return messageAddLayout;
        }

        protected CommentDisplay createCommentPanel() {
            CommentDisplay commentDisplay = new CommentDisplay(ProjectTypeConstants.MESSAGE,
                    CurrentProjectVariables.getProjectId(), MessageRelayEmailNotificationAction.class);
            commentDisplay.loadComments("" + message.getId());
            return commentDisplay;
        }

        @Override
        public void attachField(Object propertyId, Field<?> field) {

        }
    }
}
