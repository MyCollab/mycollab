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
package com.mycollab.module.crm.ui.components;

import com.mycollab.common.domain.CommentWithBLOBs;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.CommentService;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.ui.components.UserBlock;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.web.ui.AttachmentPanel;
import com.mycollab.vaadin.ui.ReloadableComponent;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.RichTextArea;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.vaadin.easyuploads.MultiFileUploadExt;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class CrmCommentInput extends MHorizontalLayout {
    private static final long serialVersionUID = 1L;

    private final RichTextArea commentArea;
    private String type;
    private String typeId;

    CrmCommentInput(final ReloadableComponent component, final String typeVal) {
        super();
        this.withMargin(new MarginInfo(true, true, false, true)).withFullWidth().withStyleName("message");

        SimpleUser currentUser = AppContext.getUser();
        UserBlock userBlock = new UserBlock(currentUser.getUsername(), currentUser.getAvatarid(), currentUser.getDisplayName());

        MVerticalLayout textAreaWrap = new MVerticalLayout().withFullWidth()
                .withStyleName("message-container");
        this.with(userBlock, textAreaWrap).expand(textAreaWrap);

        type = typeVal;

        commentArea = new RichTextArea();
        commentArea.setWidth("100%");
        commentArea.setHeight("200px");

        final AttachmentPanel attachments = new AttachmentPanel();

        final MHorizontalLayout controlsLayout = new MHorizontalLayout().withFullWidth();
        controlsLayout.setDefaultComponentAlignment(Alignment.TOP_RIGHT);

        final MultiFileUploadExt uploadExt = new MultiFileUploadExt(attachments);
        uploadExt.addComponent(attachments);
        controlsLayout.with(uploadExt).withAlign(uploadExt, Alignment.TOP_LEFT).expand(uploadExt);

        final Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                commentArea.setValue("");
            }
        });
        cancelBtn.setStyleName(UIConstants.BUTTON_OPTION);

        final Button newCommentBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_POST), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final Button.ClickEvent event) {
                CommentWithBLOBs comment = new CommentWithBLOBs();
                comment.setComment(Jsoup.clean(commentArea.getValue(), Whitelist.relaxed()));
                comment.setCreatedtime(new GregorianCalendar().getTime());
                comment.setCreateduser(AppContext.getUsername());
                comment.setSaccountid(AppContext.getAccountId());
                comment.setType(type);
                comment.setTypeid(typeId);

                CommentService commentService = AppContextUtil.getSpringBean(CommentService.class);
                int commentId = commentService.saveWithSession(comment, AppContext.getUsername());

                String attachmentPath = AttachmentUtils.getCommentAttachmentPath(typeVal,
                        AppContext.getAccountId(), null, typeId, commentId);

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
        newCommentBtn.setStyleName(UIConstants.BUTTON_ACTION);
        newCommentBtn.setIcon(FontAwesome.SEND);
        controlsLayout.with(cancelBtn, newCommentBtn);
        textAreaWrap.with(commentArea, controlsLayout);
    }

    void setTypeAndId(final String typeId) {
        this.typeId = typeId;
    }
}
