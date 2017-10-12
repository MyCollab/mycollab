/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.ui.components;

import com.mycollab.common.domain.CommentWithBLOBs;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.CommentService;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.ui.components.UserBlock;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ReloadableComponent;
import com.mycollab.vaadin.web.ui.AttachmentPanel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.RichTextArea;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.vaadin.viritin.button.MButton;
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

    CrmCommentInput(ReloadableComponent component, String typeVal) {
        this.withMargin(new MarginInfo(true, true, false, false)).withFullWidth();

        SimpleUser currentUser = UserUIContext.getUser();
        UserBlock userBlock = new UserBlock(currentUser.getUsername(), currentUser.getAvatarid(), currentUser.getDisplayName());

        MVerticalLayout textAreaWrap = new MVerticalLayout().withFullWidth().withStyleName(WebThemes.MESSAGE_CONTAINER);
        this.with(userBlock, textAreaWrap).expand(textAreaWrap);

        type = typeVal;

        commentArea = new RichTextArea();
        commentArea.setWidth("100%");
        commentArea.setHeight("200px");

        final AttachmentPanel attachments = new AttachmentPanel();

        MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> commentArea.setValue(""))
                .withStyleName(WebThemes.BUTTON_OPTION);

        MButton newCommentBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_POST), clickEvent -> {
            CommentWithBLOBs comment = new CommentWithBLOBs();
            comment.setComment(Jsoup.clean(commentArea.getValue(), Whitelist.relaxed()));
            comment.setCreatedtime(new GregorianCalendar().getTime());
            comment.setCreateduser(UserUIContext.getUsername());
            comment.setSaccountid(AppUI.getAccountId());
            comment.setType(type);
            comment.setTypeid(typeId);

            CommentService commentService = AppContextUtil.getSpringBean(CommentService.class);
            int commentId = commentService.saveWithSession(comment, UserUIContext.getUsername());

            String attachmentPath = AttachmentUtils.getCommentAttachmentPath(typeVal,
                    AppUI.getAccountId(), null, typeId, commentId);

            if (!"".equals(attachmentPath)) {
                attachments.saveContentsToRepo(attachmentPath);
            }

            // save success, clear comment area and load list
            // comments again
            commentArea.setValue("");
            component.reload();
        }).withIcon(FontAwesome.SEND).withStyleName(WebThemes.BUTTON_ACTION);
        final MHorizontalLayout controlsLayout = new MHorizontalLayout(cancelBtn, newCommentBtn);
        textAreaWrap.with(commentArea, attachments, controlsLayout).withAlign(controlsLayout, Alignment.TOP_RIGHT);
    }

    void setTypeAndId(final String typeId) {
        this.typeId = typeId;
    }
}
