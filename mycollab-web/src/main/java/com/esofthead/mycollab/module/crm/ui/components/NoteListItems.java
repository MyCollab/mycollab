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
package com.esofthead.mycollab.module.crm.ui.components;

import com.esofthead.mycollab.common.CommentType;
import com.esofthead.mycollab.common.MonitorTypeConstants;
import com.esofthead.mycollab.common.domain.RelayEmailNotificationWithBLOBs;
import com.esofthead.mycollab.common.domain.SimpleComment;
import com.esofthead.mycollab.common.domain.criteria.CommentSearchCriteria;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.CommentService;
import com.esofthead.mycollab.common.service.RelayEmailNotificationService;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.Note;
import com.esofthead.mycollab.module.crm.domain.SimpleNote;
import com.esofthead.mycollab.module.crm.domain.criteria.NoteSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.service.NoteService;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.ui.components.UserBlock;
import com.esofthead.mycollab.schedule.email.crm.*;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.BeanList.RowDisplayHandler;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.easyuploads.MultiFileUploadExt;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class NoteListItems extends VerticalLayout {
    private static final long serialVersionUID = 1L;

    private String type;
    private Integer typeId;
    private VerticalLayout noteListContainer;
    private BeanList<NoteService, NoteSearchCriteria, SimpleNote> noteList;
    private VerticalLayout noteWrapper;

    private final NoteService noteService;

    private Button createBtn;

    public NoteListItems( String title) {
        this(title, "", 0);
    }

    public NoteListItems( String title,  String type,
                          Integer typeId) {
        super();
        Label header = new Label(title);
        header.addStyleName("h2");
        addComponent(header);
        noteService = ApplicationContextUtil.getSpringBean(NoteService.class);
        this.type = type;
        this.typeId = typeId;

        initUI();
    }

    public void setEnableCreateButton(boolean enabled) {
        createBtn.setEnabled(enabled);
    }

    private void addCreateBtn() {
        final Component component = noteWrapper.getComponent(0);
        if (component instanceof NoteEditor) {
            noteWrapper.replaceComponent(component, createBtn);
            noteWrapper.setComponentAlignment(createBtn, Alignment.TOP_RIGHT);
        }
    }

    private void displayNotes() {
        final NoteSearchCriteria searchCriteria = new NoteSearchCriteria();
        searchCriteria.setType(new StringSearchField(SearchField.AND, type));
        searchCriteria.setTypeid(new NumberSearchField(typeId));
        noteList.setSearchCriteria(searchCriteria);
    }

    private void initUI() {
        noteWrapper = new MVerticalLayout().withMargin(new MarginInfo(true, true, false, true))
                .withWidth("100%").withStyleName("note-view");

        this.addComponent(noteWrapper);
        addStyleName("note-list");

        createBtn = new Button(
                AppContext.getMessage(CrmCommonI18nEnum.BUTTON_NEW_NOTE),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        NoteEditor addCommentEditor = new NoteEditor();
                        noteWrapper.replaceComponent(createBtn,
                                addCommentEditor);
                        noteWrapper.setComponentAlignment(addCommentEditor,
                                Alignment.TOP_LEFT);
                    }
                });

        createBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        createBtn.setIcon(FontAwesome.PLUS);
        noteWrapper.addComponent(createBtn);
        noteWrapper.setComponentAlignment(createBtn, Alignment.TOP_RIGHT);

        noteList = new BeanList<>(noteService, NoteRowDisplayHandler.class);
        noteList.setDisplayEmptyListText(false);
        noteList.setStyleName("noteList");

        noteListContainer = new VerticalLayout();
        noteWrapper.addComponent(noteListContainer);
        noteListContainer.addComponent(noteList);
    }

    public void showNotes(final String type, final int typeId) {
        this.type = type;
        this.typeId = typeId;
        displayNotes();
    }

    public static class NoteRowDisplayHandler extends
            RowDisplayHandler<SimpleNote> implements ReloadableComponent {
        private static final long serialVersionUID = 1L;

        private VerticalLayout noteContentLayout;
        private BeanList<CommentService, CommentSearchCriteria, SimpleComment> commentList;
        private CommentInput commentInput;
        private SimpleNote note;
        private Button replyBtn;

        @Override
        public void cancel() {
            if (commentInput != null) {
                final int compIndex = noteContentLayout.getComponentIndex(commentInput);
                if (compIndex >= 0) {
                    noteContentLayout.removeComponent(commentInput);
                    commentInput = null;
                    replyBtn.setVisible(true);
                }
            }
        }

        private Component constructNoteHeader(final SimpleNote note) {
            final MHorizontalLayout layout = new MHorizontalLayout().withWidth("100%").withStyleName("message");

            UserBlock userBlock = new UserBlock(note.getCreateduser(), note.getCreatedUserAvatarId(), note.getCreateUserFullName());
            layout.addComponent(userBlock);

            final CssLayout rowLayout = new CssLayout();
            rowLayout.setStyleName("message-container");
            rowLayout.setWidth("100%");

            MHorizontalLayout messageHeader = new MHorizontalLayout().withMargin(new MarginInfo(true, true, false,
                    true)).withWidth("100%");
            messageHeader.setStyleName("message-header");

            ELabel timePostLbl = new ELabel(AppContext.getMessage(
                    CrmCommonI18nEnum.EXT_ADDED_NOTED, note
                            .getCreateUserFullName(), AppContext.formatPrettyTime(note.getCreatedtime())),
                    ContentMode.HTML).withDescription(AppContext.formatDateTime(note.getCreatedtime()));
            timePostLbl.setSizeUndefined();
            timePostLbl.setStyleName("time-post");
            messageHeader.with(timePostLbl).expand(timePostLbl);
            messageHeader.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

            replyBtn = new Button("",
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            if (noteContentLayout.getComponentCount() > 0) {
                                Component component = noteContentLayout
                                        .getComponent(noteContentLayout
                                                .getComponentCount() - 1);
                                if (!(component instanceof CommentInput)) {
                                    commentInput = new CommentInput(
                                            NoteRowDisplayHandler.this,
                                            CommentType.CRM_NOTE, ""
                                            + note.getId(), null, true,
                                            false);
                                    noteContentLayout.addComponent(
                                            commentInput, noteContentLayout
                                                    .getComponentCount());
                                    replyBtn.setVisible(false);
                                }
                            }
                        }
                    });

            replyBtn.setStyleName(UIConstants.BUTTON_ICON_ONLY);
            replyBtn.setIcon(FontAwesome.REPLY);
            messageHeader.addComponent(replyBtn);

            if (AppContext.getUsername().equals(note.getCreateduser())
                    || AppContext.isAdmin()) {
                // Message delete button
                Button msgDeleteBtn = new Button("", new ClickListener() {
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
                                AppContext
                                        .getMessage(GenericI18Enum.BUTTON_YES),
                                AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                                new ConfirmDialog.Listener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void onClose(ConfirmDialog dialog) {
                                        if (dialog.isConfirmed()) {
                                            NoteService noteService = ApplicationContextUtil
                                                    .getSpringBean(NoteService.class);
                                            noteService.removeWithSession(
                                                    note.getId(),
                                                    AppContext.getUsername(),
                                                    AppContext.getAccountId());
                                            NoteRowDisplayHandler.this.owner
                                                    .removeRow(noteContentLayout);
                                        }
                                    }
                                });
                    }
                });
                msgDeleteBtn.setIcon(FontAwesome.TRASH_O);
                msgDeleteBtn.setStyleName(UIConstants.BUTTON_ICON_ONLY);
                messageHeader.addComponent(msgDeleteBtn);
            }

            rowLayout.addComponent(messageHeader);

            final SafeHtmlLabel messageContent = new SafeHtmlLabel(note.getNote());
            messageContent.setStyleName("message-body");
            rowLayout.addComponent(messageContent);

            final List<Content> attachments = note.getAttachments();
            if (!CollectionUtils.isEmpty(attachments)) {
                AttachmentDisplayComponent attachmentDisplayComponent = new AttachmentDisplayComponent(
                        attachments);
                attachmentDisplayComponent.setWidth("100%");
                attachmentDisplayComponent.addStyleName("message-footer");
                rowLayout.addComponent(attachmentDisplayComponent);
            }

            layout.with(rowLayout).expand(rowLayout);
            return layout;
        }

        private void displayComments() {
            final CommentSearchCriteria searchCriteria = new CommentSearchCriteria();
            searchCriteria.setType(new StringSearchField(CommentType.CRM_NOTE
                    .toString()));
            searchCriteria.setTypeid(new StringSearchField("" + note.getId()));
            commentList.setSearchCriteria(searchCriteria);
        }

        @Override
        public Component generateRow(final SimpleNote note, final int rowIndex) {
            this.note = note;

            VerticalLayout commentListWrapper = new VerticalLayout();
            commentListWrapper.setWidth("100%");
            commentListWrapper.setMargin(new MarginInfo(false, false, false,
                    true));
            commentListWrapper.addStyleName("comment-list-wrapper");

            noteContentLayout = new VerticalLayout();

            noteContentLayout.addComponent(constructNoteHeader(note));

            commentList = new BeanList<>(
                    ApplicationContextUtil.getSpringBean(CommentService.class),
                    CommentRowDisplayHandler.class);
            commentList.setDisplayEmptyListText(false);
            commentList.setWidth("100%");
            commentListWrapper.addComponent(commentList);
            noteContentLayout.addComponent(commentListWrapper);
            noteContentLayout.setComponentAlignment(commentListWrapper,
                    Alignment.TOP_RIGHT);
            commentList.loadItems(note.getComments());

            return noteContentLayout;
        }

        @Override
        public void reload() {
            displayComments();
            cancel();
        }
    }

    private class NoteEditor extends VerticalLayout {
        private static final long serialVersionUID = 1L;
        private final RichTextArea noteArea;

        public NoteEditor() {
            super();
            setSpacing(true);
            this.setWidth("600px");

            MVerticalLayout editBox = new MVerticalLayout();

            MHorizontalLayout commentWrap = new MHorizontalLayout().withWidth("100%");
            commentWrap.addStyleName("message");

            SimpleUser currentUser = AppContext.getUser();
            UserBlock userBlock = new UserBlock(currentUser.getUsername(), currentUser.getAvatarid(), currentUser.getDisplayName());

            commentWrap.addComponent(userBlock);
            VerticalLayout textAreaWrap = new VerticalLayout();
            textAreaWrap.setStyleName("message-container");
            textAreaWrap.setWidth("100%");
            textAreaWrap.addComponent(editBox);

            commentWrap.addComponent(textAreaWrap);
            commentWrap.setExpandRatio(textAreaWrap, 1.0f);

            final AttachmentPanel attachments = new AttachmentPanel();

            noteArea = new RichTextArea();
            noteArea.setWidth("100%");
            noteArea.setHeight("200px");

            editBox.addComponent(noteArea);

             MHorizontalLayout controls = new MHorizontalLayout().withWidth("100%");

             MultiFileUploadExt uploadExt = new MultiFileUploadExt(
                    attachments);
            uploadExt.addComponent(attachments);
            controls.with(uploadExt).withAlign(uploadExt, Alignment.TOP_LEFT).expand(uploadExt);

            final Button saveBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_POST),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            final Note note = new Note();
                            note.setCreateduser(AppContext.getUsername());
                            note.setNote(Jsoup.clean(noteArea.getValue(), Whitelist.relaxed()));
                            note.setSaccountid(AppContext.getAccountId());
                            note.setSubject("");
                            note.setType(type);
                            note.setTypeid(typeId);
                            note.setCreatedtime(new GregorianCalendar().getTime());
                            note.setLastupdatedtime(new GregorianCalendar().getTime());
                            final int noteId = noteService.saveWithSession(
                                    note, AppContext.getUsername());

                            RelayEmailNotificationWithBLOBs relayNotification = new RelayEmailNotificationWithBLOBs();
                            relayNotification.setChangeby(AppContext.getUsername());
                            relayNotification.setChangecomment(noteArea.getValue());
                            relayNotification.setSaccountid(AppContext.getAccountId());
                            relayNotification.setType(type);
                            relayNotification.setAction(MonitorTypeConstants.ADD_COMMENT_ACTION);
                            relayNotification.setTypeid("" + typeId);

                            if (type.equals(CrmTypeConstants.ACCOUNT)) {
                                relayNotification
                                        .setEmailhandlerbean(AccountRelayEmailNotificationAction.class
                                                .getName());
                            } else if (type.equals(CrmTypeConstants.CONTACT)) {
                                relayNotification
                                        .setEmailhandlerbean(ContactRelayEmailNotificationAction.class
                                                .getName());
                            } else if (type.equals(CrmTypeConstants.CAMPAIGN)) {
                                relayNotification
                                        .setEmailhandlerbean(CampaignRelayEmailNotificationAction.class
                                                .getName());
                            } else if (type.equals(CrmTypeConstants.LEAD)) {
                                relayNotification
                                        .setEmailhandlerbean(LeadRelayEmailNotificationAction.class
                                                .getName());
                            } else if (type
                                    .equals(CrmTypeConstants.OPPORTUNITY)) {
                                relayNotification
                                        .setEmailhandlerbean(OpportunityRelayEmailNotificationAction.class
                                                .getName());
                            } else if (type.equals(CrmTypeConstants.CASE)) {
                                relayNotification
                                        .setEmailhandlerbean(CaseRelayEmailNotificationAction.class
                                                .getName());
                            } else if (type.equals(CrmTypeConstants.TASK)) {
                                relayNotification
                                        .setEmailhandlerbean(TaskRelayEmailNotificationAction.class
                                                .getName());
                            } else if (type.equals(CrmTypeConstants.MEETING)) {
                                relayNotification
                                        .setEmailhandlerbean(MeetingRelayEmailNotificationAction.class
                                                .getName());
                            } else if (type.equals(CrmTypeConstants.CALL)) {
                                relayNotification
                                        .setEmailhandlerbean(CallRelayEmailNotificationAction.class
                                                .getName());
                            }
                            RelayEmailNotificationService relayEmailNotificationService = ApplicationContextUtil
                                    .getSpringBean(RelayEmailNotificationService.class);
                            relayEmailNotificationService.saveWithSession(
                                    relayNotification, AppContext.getUsername());
                            // End save relay Email
                            // --------------------------------------------------

                            String attachmentPath = AttachmentUtils
                                    .getCrmNoteAttachmentPath(
                                            AppContext.getAccountId(), noteId);
                            attachments.saveContentsToRepo(attachmentPath);
                            displayNotes();
                            addCreateBtn();
                        }
                    });
            saveBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
            saveBtn.setIcon(FontAwesome.SEND);


            final Button cancelBtn = new Button(
                    AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
                    new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            addCreateBtn();
                        }
                    });
            cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);

            controls.with(cancelBtn).withAlign(cancelBtn, Alignment.TOP_RIGHT);
            controls.with(saveBtn).withAlign(saveBtn, Alignment.TOP_RIGHT);

            editBox.addComponent(controls);
            this.addComponent(commentWrap);
        }
    }
}
