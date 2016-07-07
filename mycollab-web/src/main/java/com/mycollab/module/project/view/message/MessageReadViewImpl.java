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
package com.mycollab.module.project.view.message;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleMessage;
import com.mycollab.module.project.i18n.MessageI18nEnum;
import com.mycollab.module.project.service.MessageService;
import com.mycollab.module.project.ui.components.CommentDisplay;
import com.mycollab.module.project.ui.components.ComponentUtils;
import com.mycollab.module.project.ui.components.ProjectAttachmentDisplayComponentFactory;
import com.mycollab.module.project.ui.components.ProjectMemberBlock;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.AbstractPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MCssLayout;
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
    private MCssLayout contentWrapper;
    private MHorizontalLayout header;
    private CommentDisplay commentDisplay;
    private CheckBox stickyCheck;

    public MessageReadViewImpl() {
        super();

        header = new MHorizontalLayout().withMargin(true).withStyleName("hdr-view").withFullWidth();
        previewForm = new AdvancedPreviewBeanForm<>();

        contentWrapper = new MCssLayout().withStyleName(UIConstants.CONTENT_WRAPPER);
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

    class FormLayoutFactory extends AbstractFormLayoutFactory {
        private static final long serialVersionUID = 1L;

        @Override
        public ComponentContainer getLayout() {
            header.removeAllComponents();
            MVerticalLayout messageAddLayout = new MVerticalLayout().withMargin(false).withFullWidth();

            MButton deleteBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_DELETE), clickEvent -> {
                ConfirmDialogExt.show(UI.getCurrent(),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        new ConfirmDialog.Listener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void onClose(final ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    MessageService messageService = AppContextUtil.getSpringBean(MessageService.class);
                                    messageService.removeWithSession(message, AppContext.getUsername(), AppContext.getAccountId());
                                    previewForm.fireCancelForm(message);
                                }
                            }
                        });
            }).withIcon(FontAwesome.TRASH_O).withStyleName(UIConstants.BUTTON_DANGER);
            deleteBtn.setVisible(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.MESSAGES));

            stickyCheck = new CheckBox(AppContext.getMessage(MessageI18nEnum.FORM_IS_STICK), message.getIsstick());
            stickyCheck.addValueChangeListener(valueChangeEvent -> {
                message.setIsstick(stickyCheck.getValue());
                message.setSaccountid(AppContext.getAccountId());
                MessageService messageService = AppContextUtil.getSpringBean(MessageService.class);
                messageService.updateWithSession(message, AppContext.getUsername());
            });
            stickyCheck.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MESSAGES));

            HeaderWithFontAwesome headerText = ComponentUtils.headerH3(ProjectTypeConstants.MESSAGE, message.getTitle());
            header.with(headerText, stickyCheck, deleteBtn).withAlign(headerText, Alignment.MIDDLE_LEFT)
                    .withAlign(stickyCheck, Alignment.MIDDLE_RIGHT).withAlign(deleteBtn, Alignment.MIDDLE_RIGHT).expand(headerText);

            MHorizontalLayout messageLayout = new MHorizontalLayout().withStyleName("message").withFullWidth();

            if (message.getIsstick() != null && message.getIsstick()) {
                messageLayout.addStyleName("important-message");
            }

            ProjectMemberBlock userBlock = new ProjectMemberBlock(message.getPosteduser(), message.getPostedUserAvatarId(),
                    message.getFullPostedUserName());

            messageLayout.addComponent(userBlock);

            MVerticalLayout rowLayout = new MVerticalLayout().withMargin(true).withFullWidth().withStyleName("message-container");

            MHorizontalLayout messageHeader = new MHorizontalLayout().withFullWidth();
            messageHeader.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

            ELabel timePostLbl = ELabel.html(AppContext.getMessage(MessageI18nEnum.USER_COMMENT_ADD, message.getFullPostedUserName(),
                    AppContext.formatPrettyTime(message.getPosteddate())))
                    .withDescription(AppContext.formatDateTime(message.getPosteddate()));
            timePostLbl.setSizeUndefined();
            timePostLbl.setStyleName(UIConstants.META_INFO);

            messageHeader.with(timePostLbl).expand(timePostLbl);

            rowLayout.addComponent(messageHeader);

            SafeHtmlLabel messageContent = new SafeHtmlLabel(message.getMessage());
            rowLayout.addComponent(messageContent);

            ResourceService attachmentService = AppContextUtil.getSpringBean(ResourceService.class);
            List<Content> attachments = attachmentService.getContents(AttachmentUtils.getProjectEntityAttachmentPath(
                    AppContext.getAccountId(), message.getProjectid(), ProjectTypeConstants.MESSAGE, "" + message.getId()));
            if (CollectionUtils.isNotEmpty(attachments)) {
                HorizontalLayout attachmentField = new HorizontalLayout();
                Button attachmentIcon = new Button(null, FontAwesome.PAPERCLIP);
                attachmentIcon.addStyleName(UIConstants.BUTTON_ICON_ONLY);
                attachmentField.addComponent(attachmentIcon);

                Label lbAttachment = new Label(AppContext.getMessage(GenericI18Enum.FORM_ATTACHMENTS));
                attachmentField.addComponent(lbAttachment);

                Component attachmentDisplayComp = ProjectAttachmentDisplayComponentFactory
                        .getAttachmentDisplayComponent(message.getProjectid(), ProjectTypeConstants.MESSAGE, message.getId());

                MVerticalLayout messageFooter = new MVerticalLayout().withFullWidth()
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

        CommentDisplay createCommentPanel() {
            CommentDisplay commentDisplay = new CommentDisplay(ProjectTypeConstants.MESSAGE, CurrentProjectVariables.getProjectId());
            commentDisplay.loadComments("" + message.getId());
            return commentDisplay;
        }

        @Override
        protected Component onAttachField(Object propertyId, Field<?> field) {
            return null;
        }
    }
}
