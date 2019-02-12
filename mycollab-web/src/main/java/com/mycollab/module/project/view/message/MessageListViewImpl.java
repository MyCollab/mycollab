/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.message;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Text;
import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.form.view.LayoutType;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.Message;
import com.mycollab.module.project.domain.SimpleMessage;
import com.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.mycollab.module.project.i18n.MessageI18nEnum;
import com.mycollab.module.project.service.MessageService;
import com.mycollab.module.project.ui.components.ComponentUtils;
import com.mycollab.module.project.ui.components.ProjectMemberBlock;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasSearchHandlers;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.*;
import com.mycollab.vaadin.web.ui.*;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class MessageListViewImpl extends AbstractVerticalPageView implements MessageListView {
    private static final long serialVersionUID = 8433776359091397422L;

    private MVerticalLayout bodyLayout;
    private DefaultBeanPagedList<MessageService, MessageSearchCriteria, SimpleMessage> messageList;
    private MessageSearchPanel searchPanel;
    private MessageSearchCriteria searchCriteria;
    private boolean isAddingMessage = false;

    public MessageListViewImpl() {
        this.withSpacing(true).withMargin(true).withFullWidth();
        searchPanel = new MessageSearchPanel();
        messageList = new DefaultBeanPagedList<>(AppContextUtil.getSpringBean(MessageService.class), new MessageRowDisplayHandler());
        bodyLayout = new MVerticalLayout(messageList).withSpacing(false).withMargin(false);
    }

    @Override
    public void setCriteria(final MessageSearchCriteria criteria) {
        this.removeAllComponents();
        this.searchCriteria = criteria;

        messageList.setSearchCriteria(searchCriteria);
        with(searchPanel, bodyLayout).expand(bodyLayout);
    }

    private class MessageRowDisplayHandler implements IBeanList.RowDisplayHandler<SimpleMessage> {
        @Override
        public Component generateRow(IBeanList<SimpleMessage> host, final SimpleMessage message, int rowIndex) {
            final MHorizontalLayout messageLayout = new MHorizontalLayout().withMargin(new MarginInfo(true, false,
                    true, false)).withFullWidth();
            if (Boolean.TRUE.equals(message.getIsstick())) {
                messageLayout.addStyleName("important-message");
            }

            ProjectMemberBlock userBlock = new ProjectMemberBlock(message.getCreateduser(), message.getPostedUserAvatarId(),
                    message.getFullPostedUserName());
            messageLayout.addComponent(userBlock);

            MVerticalLayout rowLayout = new MVerticalLayout().withFullWidth().withStyleName(WebThemes.MESSAGE_CONTAINER);

            A labelLink = new A(ProjectLinkGenerator.generateMessagePreviewLink(message.getProjectid(), message.getId()),
                    new Text(message.getTitle()));

            MHorizontalLayout messageHeader = new MHorizontalLayout().withMargin(false);
            messageHeader.setDefaultComponentAlignment(Alignment.TOP_LEFT);

            CssLayout leftHeader = new CssLayout();
            leftHeader.addComponent(ELabel.h3(labelLink.write()));
            ELabel timePostLbl = new ELabel().prettyDateTime(message.getCreatedtime()).withStyleName(WebThemes.META_INFO);

            MButton deleteBtn = new MButton("", clickEvent -> ConfirmDialogExt.show(UI.getCurrent(),
                    UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                    UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                    UserUIContext.getMessage(GenericI18Enum.ACTION_YES),
                    UserUIContext.getMessage(GenericI18Enum.ACTION_NO),
                    confirmDialog -> {
                        if (confirmDialog.isConfirmed()) {
                            MessageService messageService = AppContextUtil.getSpringBean(MessageService.class);
                            messageService.removeWithSession(message, UserUIContext.getUsername(), AppUI.getAccountId());
                            messageList.setSearchCriteria(searchCriteria);
                        }
                    })).withIcon(VaadinIcons.TRASH).withStyleName(WebThemes.BUTTON_ICON_ONLY);
            deleteBtn.setVisible(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.MESSAGES));

            MHorizontalLayout rightHeader = new MHorizontalLayout(timePostLbl, deleteBtn).alignAll(Alignment.MIDDLE_RIGHT);
            messageHeader.with(leftHeader, rightHeader).expand(leftHeader);

            rowLayout.addComponent(messageHeader);

            SafeHtmlLabel messageContent = new SafeHtmlLabel(message.getMessage());
            rowLayout.addComponent(messageContent);

            MHorizontalLayout notification = new MHorizontalLayout().withStyleName("notification").withUndefinedSize();
            if (message.getCommentsCount() > 0) {
                MHorizontalLayout commentNotification = new MHorizontalLayout();
                Label commentCountLbl = ELabel.html(String.format("%s %s", Integer.toString(message.getCommentsCount()), VaadinIcons.COMMENTS.getHtml()));
                commentCountLbl.setSizeUndefined();
                commentNotification.addComponent(commentCountLbl);
                notification.addComponent(commentNotification);
            }
            ResourceService attachmentService = AppContextUtil.getSpringBean(ResourceService.class);
            List<Content> attachments = attachmentService.getContents(AttachmentUtils
                    .getProjectEntityAttachmentPath(AppUI.getAccountId(),
                            message.getProjectid(), ProjectTypeConstants.MESSAGE, "" + message.getId()));
            if (CollectionUtils.isNotEmpty(attachments)) {
                HorizontalLayout attachmentNotification = new HorizontalLayout();
                Label attachmentCountLbl = new Label(Integer.toString(attachments.size()));
                attachmentCountLbl.setSizeUndefined();
                attachmentNotification.addComponent(attachmentCountLbl);
                Button attachmentIcon = new Button(VaadinIcons.PAPERCLIP);
                attachmentIcon.addStyleName(WebThemes.BUTTON_ICON_ONLY);
                attachmentNotification.addComponent(attachmentIcon);
                notification.addComponent(attachmentNotification);
            }

            if (notification.getComponentCount() > 0) {
                MVerticalLayout messageFooter = new MVerticalLayout(notification).withSpacing(false).withFullWidth().withAlign(notification, Alignment.MIDDLE_RIGHT);
                rowLayout.addComponent(messageFooter);
            }

            messageLayout.with(rowLayout).expand(rowLayout);
            return messageLayout;
        }
    }

    private class MessageSearchPanel extends DefaultGenericSearchPanel<MessageSearchCriteria> {
        private TextField nameField;

        @Override
        protected HeaderWithIcon buildSearchTitle() {
            return ComponentUtils.headerH2(ProjectTypeConstants.MESSAGE, UserUIContext.getMessage(MessageI18nEnum.LIST));
        }

        @Override
        protected Component buildExtraControls() {
            return new MButton(UserUIContext.getMessage(MessageI18nEnum.NEW),
                    clickEvent -> {
                        if (!isAddingMessage) createAddMessageLayout();
                    })
                    .withIcon(VaadinIcons.PLUS).withStyleName(WebThemes.BUTTON_ACTION)
                    .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MESSAGES));
        }

        @Override
        protected SearchLayout<MessageSearchCriteria> createBasicSearchLayout() {
            return new BasicSearchLayout<MessageSearchCriteria>(MessageSearchPanel.this) {
                @Override
                public ComponentContainer constructBody() {
                    nameField = new MTextField().withPlaceholder(UserUIContext.getMessage(GenericI18Enum.ACTION_QUERY_BY_TEXT))
                            .withWidth(WebUIConstants.DEFAULT_CONTROL_WIDTH);

                    MButton searchBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SEARCH), clickEvent -> callSearchAction())
                            .withStyleName(WebThemes.BUTTON_ACTION).withIcon(VaadinIcons.SEARCH)
                            .withClickShortcut(ShortcutAction.KeyCode.ENTER);
                    return new MHorizontalLayout(nameField, searchBtn).withMargin(true).withAlign(nameField, Alignment.MIDDLE_LEFT);
                }

                @Override
                protected MessageSearchCriteria fillUpSearchCriteria() {
                    MessageSearchCriteria criteria = new MessageSearchCriteria();
                    criteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                    criteria.setMessage(StringSearchField.and(nameField.getValue()));
                    return criteria;
                }
            };
        }
    }

    private void createAddMessageLayout() {
        isAddingMessage = true;
        MVerticalLayout newMessageLayout = new MVerticalLayout().withWidth("800px");

        GridFormLayoutHelper gridFormLayoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(LayoutType.ONE_COLUMN);
        TextField titleField = new MTextField().withFullWidth().withRequiredIndicatorVisible(true);
        gridFormLayoutHelper.addComponent(titleField, UserUIContext.getMessage(MessageI18nEnum.FORM_TITLE), 0, 0);

        RichTextArea descField = new RichTextArea();
        descField.setWidth("100%");
        descField.setHeight("200px");
        gridFormLayoutHelper.addComponent(descField, UserUIContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 1);
        newMessageLayout.with(gridFormLayoutHelper.getLayout());

        AttachmentPanel attachmentPanel = new AttachmentPanel();
        CheckBox chkIsStick = new CheckBox(UserUIContext.getMessage(MessageI18nEnum.FORM_IS_STICK));

        MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
                clickEvent -> {
                    bodyLayout.removeComponent(newMessageLayout);
                    isAddingMessage = false;
                })
                .withStyleName(WebThemes.BUTTON_OPTION);

        MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_POST), clickEvent -> {
            Message message = new Message();
            message.setProjectid(CurrentProjectVariables.getProjectId());
            if (!titleField.getValue().trim().equals("")) {
                message.setTitle(titleField.getValue());
                message.setMessage(descField.getValue());
                message.setCreateduser(UserUIContext.getUsername());
                message.setSaccountid(AppUI.getAccountId());
                message.setIsstick(chkIsStick.getValue());
                MessageService messageService = AppContextUtil.getSpringBean(MessageService.class);
                messageService.saveWithSession(message, UserUIContext.getUsername());
                bodyLayout.removeComponent(newMessageLayout);
                isAddingMessage = false;

                searchPanel.notifySearchHandler(searchCriteria);


                String attachmentPath = AttachmentUtils.getProjectEntityAttachmentPath(
                        AppUI.getAccountId(), message.getProjectid(),
                        ProjectTypeConstants.MESSAGE, "" + message.getId());
                attachmentPanel.saveContentsToRepo(attachmentPath);
            } else {
                titleField.addStyleName("errorField");
                NotificationUtil.showErrorNotification(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL,
                        UserUIContext.getMessage(MessageI18nEnum.FORM_TITLE)));
            }
        }).withIcon(VaadinIcons.CLIPBOARD).withStyleName(WebThemes.BUTTON_ACTION);

        MHorizontalLayout controlLayout = new MHorizontalLayout(chkIsStick, cancelBtn, saveBtn).alignAll(Alignment.MIDDLE_CENTER);
        newMessageLayout.with(attachmentPanel, controlLayout).withAlign(controlLayout, Alignment.MIDDLE_RIGHT);

        bodyLayout.addComponent(newMessageLayout, 0);
    }

    @Override
    public HasSearchHandlers<MessageSearchCriteria> getSearchHandlers() {
        return this.searchPanel;
    }

}
