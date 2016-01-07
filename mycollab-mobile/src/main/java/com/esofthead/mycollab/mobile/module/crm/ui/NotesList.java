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
package com.esofthead.mycollab.mobile.module.crm.ui;

import com.esofthead.mycollab.common.MonitorTypeConstants;
import com.esofthead.mycollab.common.domain.RelayEmailNotificationWithBLOBs;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.RelayEmailNotificationService;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePageView;
import com.esofthead.mycollab.module.crm.domain.Note;
import com.esofthead.mycollab.module.crm.domain.SimpleNote;
import com.esofthead.mycollab.module.crm.domain.criteria.NoteSearchCriteria;
import com.esofthead.mycollab.module.crm.service.NoteService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.BeanList.RowDisplayHandler;
import com.esofthead.mycollab.vaadin.ui.SafeHtmlLabel;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class NotesList extends AbstractMobilePageView {
    private static final long serialVersionUID = -9063602627063299844L;

    private String type;
    private Integer typeid;
    private VerticalLayout noteListContainer;
    private BeanList<NoteService, NoteSearchCriteria, SimpleNote> noteList;

    private final NoteService noteService;

    public NotesList(final String title) {
        this(title, "", 0);
    }

    public NotesList(final String title, final String type, final Integer typeid) {
        super();
        this.setCaption(title);
        this.setWidth("100%");

        noteService = ApplicationContextUtil.getSpringBean(NoteService.class);
        this.type = type;
        this.typeid = typeid;

        initUI();
    }

    private void displayNotes() {
        noteListContainer.removeAllComponents();
        noteListContainer.addComponent(noteList);

        final NoteSearchCriteria searchCriteria = new NoteSearchCriteria();
        searchCriteria.setType(new StringSearchField(SearchField.AND, type));
        searchCriteria.setTypeid(new NumberSearchField(typeid));
        noteList.setSearchCriteria(searchCriteria);
    }

    private void initUI() {
        noteList = new BeanList<>(noteService, NoteRowDisplayHandler.class);
        noteList.setDisplayEmptyListText(false);
        noteList.setStyleName("noteList");

        noteListContainer = new VerticalLayout();
        this.setContent(noteListContainer);
        displayNotes();

        HorizontalLayout commentBox = new HorizontalLayout();
        commentBox.setSizeFull();
        commentBox.setStyleName("comment-box");
        commentBox.setSpacing(true);
        commentBox.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        final TextArea noteInput = new TextArea();
        noteInput.setInputPrompt(AppContext.getMessage(GenericI18Enum.M_NOTE_INPUT_PROMPT));
        noteInput.setSizeFull();
        commentBox.addComponent(noteInput);
        commentBox.setExpandRatio(noteInput, 1.0f);

        Button postBtn = new Button(AppContext.getMessage(GenericI18Enum.M_BUTTON_SEND));
        postBtn.setStyleName("submit-btn");
        postBtn.setWidthUndefined();
        postBtn.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = -5095455325725786794L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                final Note note = new Note();
                note.setCreateduser(AppContext.getUsername());
                note.setNote(noteInput.getValue());
                note.setSaccountid(AppContext.getAccountId());
                note.setSubject("");
                note.setType(type);
                note.setTypeid(typeid);
                note.setCreatedtime(new GregorianCalendar().getTime());
                note.setLastupdatedtime(new GregorianCalendar().getTime());
                noteService.saveWithSession(note, AppContext.getUsername());

                // Save Relay Email -- having time must refact to
                // Aop
                // ------------------------------------------------------
                RelayEmailNotificationWithBLOBs relayNotification = new RelayEmailNotificationWithBLOBs();
                relayNotification.setChangeby(AppContext.getUsername());
                relayNotification.setChangecomment(noteInput.getValue());
                relayNotification.setSaccountid(AppContext.getAccountId());
                relayNotification.setType(type);
                relayNotification.setAction(MonitorTypeConstants.ADD_COMMENT_ACTION);
                relayNotification.setTypeid("" + typeid);
                RelayEmailNotificationService relayEmailNotificationService = ApplicationContextUtil.getSpringBean(RelayEmailNotificationService.class);
                relayEmailNotificationService.saveWithSession(relayNotification, AppContext.getUsername());
                noteInput.setValue("");
                displayNotes();
            }
        });
        commentBox.addComponent(postBtn);

        this.setToolbar(commentBox);

    }

    public void showNotes(final String type, final int typeid) {
        this.type = type;
        this.typeid = typeid;
        displayNotes();
    }

    public static class NoteRowDisplayHandler extends RowDisplayHandler<SimpleNote> {
        private static final long serialVersionUID = 1L;

        @Override
        public Component generateRow(final SimpleNote note, final int rowIndex) {
            MHorizontalLayout layout = new MHorizontalLayout().withWidth("100%").withStyleName("message").withMargin(true);
            layout.addComponent(UserAvatarControlFactory
                    .createUserAvatarEmbeddedButton(note.getCreatedUserAvatarId(), 48));

            MVerticalLayout rowLayout = new MVerticalLayout().withMargin(false).withWidth("100%").withStyleName("message-container");
            rowLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

            MHorizontalLayout messageHeader = new MHorizontalLayout().withStyleName("message-header").withWidth("100%");
            Label username = new Label(note.getCreateUserFullName());
            username.setStyleName("user-name");
            username.setWidth("100%");


            Label timePostLbl = new Label(AppContext.formatPrettyTime(note.getCreatedtime()));
            timePostLbl.setSizeUndefined();
            timePostLbl.setStyleName("time-post");
            messageHeader.with(username, timePostLbl).expand(username);

            rowLayout.addComponent(messageHeader);
            SafeHtmlLabel messageContent = new SafeHtmlLabel(note.getNote());
            messageContent.setStyleName("message-body");
            rowLayout.with(messageContent).expand(messageContent);

            layout.with(rowLayout).expand(rowLayout);
            return layout;
        }
    }
}
