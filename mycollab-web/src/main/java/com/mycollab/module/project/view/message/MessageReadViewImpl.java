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
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
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
public class MessageReadViewImpl extends AbstractVerticalPageView implements MessageReadView {
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

        contentWrapper = new MCssLayout().withStyleName(WebThemes.CONTENT_WRAPPER);
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

        @Override
        public AbstractComponent getLayout() {
            header.removeAllComponents();
            MVerticalLayout messageAddLayout = new MVerticalLayout().withMargin(false).withFullWidth();

            MButton deleteBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_DELETE), clickEvent -> {
                ConfirmDialogExt.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                MessageService messageService = AppContextUtil.getSpringBean(MessageService.class);
                                messageService.removeWithSession(message, UserUIContext.getUsername(), AppUI.getAccountId());
                                previewForm.fireCancelForm(message);
                            }
                        });
            }).withIcon(FontAwesome.TRASH_O).withStyleName(WebThemes.BUTTON_DANGER);
            deleteBtn.setVisible(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.MESSAGES));

            stickyCheck = new CheckBox(UserUIContext.getMessage(MessageI18nEnum.FORM_IS_STICK), message.getIsstick());
            stickyCheck.addValueChangeListener(valueChangeEvent -> {
                message.setIsstick(stickyCheck.getValue());
                message.setSaccountid(AppUI.getAccountId());
                MessageService messageService = AppContextUtil.getSpringBean(MessageService.class);
                messageService.updateWithSession(message, UserUIContext.getUsername());
            });
            stickyCheck.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MESSAGES));

            HeaderWithFontAwesome headerText = ComponentUtils.headerH2(ProjectTypeConstants.MESSAGE, message.getTitle());
            header.with(headerText, stickyCheck, deleteBtn).withAlign(headerText, Alignment.MIDDLE_LEFT)
                    .withAlign(stickyCheck, Alignment.MIDDLE_RIGHT).withAlign(deleteBtn, Alignment.MIDDLE_RIGHT).expand(headerText);

            MHorizontalLayout messageLayout = new MHorizontalLayout().withFullWidth();

            if (Boolean.TRUE.equals(message.getIsstick())) {
                messageLayout.addStyleName("important-message");
            }

            ProjectMemberBlock userBlock = new ProjectMemberBlock(message.getPosteduser(), message.getPostedUserAvatarId(),
                    message.getFullPostedUserName());

            messageLayout.addComponent(userBlock);

            MVerticalLayout rowLayout = new MVerticalLayout().withFullWidth().withStyleName(WebThemes.MESSAGE_CONTAINER);

            MHorizontalLayout messageHeader = new MHorizontalLayout().withFullWidth();
            messageHeader.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

            ELabel timePostLbl = ELabel.html(UserUIContext.getMessage(MessageI18nEnum.USER_COMMENT_ADD, message.getFullPostedUserName(),
                    UserUIContext.formatPrettyTime(message.getPosteddate())))
                    .withDescription(UserUIContext.formatDateTime(message.getPosteddate()));
            timePostLbl.setSizeUndefined();
            timePostLbl.setStyleName(UIConstants.META_INFO);

            messageHeader.with(timePostLbl).expand(timePostLbl);

            rowLayout.addComponent(messageHeader);

            SafeHtmlLabel messageContent = new SafeHtmlLabel(message.getMessage());
            rowLayout.addComponent(messageContent);

            ResourceService attachmentService = AppContextUtil.getSpringBean(ResourceService.class);
            List<Content> attachments = attachmentService.getContents(AttachmentUtils.getProjectEntityAttachmentPath(
                    AppUI.getAccountId(), message.getProjectid(), ProjectTypeConstants.MESSAGE, "" + message.getId()));
            if (CollectionUtils.isNotEmpty(attachments)) {
                HorizontalLayout attachmentField = new HorizontalLayout();
                Button attachmentIcon = new Button(null, FontAwesome.PAPERCLIP);
                attachmentIcon.addStyleName(WebThemes.BUTTON_ICON_ONLY);
                attachmentField.addComponent(attachmentIcon);

                Label lbAttachment = new Label(UserUIContext.getMessage(GenericI18Enum.FORM_ATTACHMENTS));
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
