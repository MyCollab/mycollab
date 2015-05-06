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

import com.esofthead.mycollab.common.domain.CommentWithBLOBs;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.CommentService;
import com.esofthead.mycollab.module.file.AttachmentUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.ui.components.UserBlock;
import com.esofthead.mycollab.schedule.email.SendingRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AttachmentPanel;
import com.esofthead.mycollab.vaadin.ui.ReloadableComponent;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.RichTextArea;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.vaadin.easyuploads.MultiFileUploadExt;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.GregorianCalendar;

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CrmCommentInput extends MHorizontalLayout {
    private static final long serialVersionUID = 1L;

    private final RichTextArea commentArea;
    private String type;
    private String typeId;

    CrmCommentInput(final ReloadableComponent component, final String typeVal,
                    final Class<? extends SendingRelayEmailNotificationAction> emailHandler) {
        super();
        this.withMargin(new MarginInfo(false, true, false, false)).withWidth("100%").withStyleName("message");

        SimpleUser currentUser = AppContext.getUser();
        UserBlock userBlock = new UserBlock(currentUser.getUsername(), currentUser.getAvatarid(), currentUser.getDisplayName());

        MVerticalLayout textAreaWrap = new MVerticalLayout().withWidth("100%")
                .withStyleName("message-container");
        this.with(userBlock, textAreaWrap).expand(textAreaWrap);

        type = typeVal;

        commentArea = new RichTextArea();
        commentArea.setWidth("100%");
        commentArea.setHeight("200px");

        final AttachmentPanel attachments = new AttachmentPanel();

        final MHorizontalLayout controlsLayout = new MHorizontalLayout().withWidth("100%");
        controlsLayout.setDefaultComponentAlignment(Alignment.TOP_RIGHT);

        final MultiFileUploadExt uploadExt = new MultiFileUploadExt(attachments);
        uploadExt.addComponent(attachments);
        controlsLayout.with(uploadExt).withAlign(uploadExt, Alignment.TOP_LEFT).expand(uploadExt);

        final Button cancelBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent event) {
                        commentArea.setValue("");
                    }
                });
        cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);

        final Button newCommentBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_POST),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        CommentWithBLOBs comment = new CommentWithBLOBs();
                        comment.setComment(Jsoup.clean(commentArea.getValue(),
                                Whitelist.relaxed()));
                        comment.setCreatedtime(new GregorianCalendar()
                                .getTime());
                        comment.setCreateduser(AppContext.getUsername());
                        comment.setSaccountid(AppContext.getAccountId());
                        comment.setType(type.toString());
                        comment.setTypeid(typeId);

                        final CommentService commentService = ApplicationContextUtil
                                .getSpringBean(CommentService.class);
                        int commentId = commentService.saveWithSession(comment,
                                AppContext.getUsername(), emailHandler);

                        String attachmentPath = AttachmentUtils
                                .getCommentAttachmentPath(typeVal,
                                        AppContext.getAccountId(),
                                        null,
                                        typeId, commentId);

                        if (!"".equals(attachmentPath)) {
                            attachments.saveContentsToRepo(attachmentPath);
                        }

                        // save success, clear comment area and load list
                        // comments again
                        commentArea.setValue("");
                        attachments.removeAllAttachmentsDisplay();
                        component.reload();
                    }
                });
        newCommentBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        newCommentBtn.setIcon(FontAwesome.SEND);
        controlsLayout.with(cancelBtn, newCommentBtn);
        textAreaWrap.with(commentArea, controlsLayout);
    }

    void setTypeAndId(final String typeId) {
        this.typeId = typeId;
    }
}
