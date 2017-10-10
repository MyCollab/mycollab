/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.ui.components;

import com.mycollab.common.domain.CommentWithBLOBs;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.CommentService;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.user.domain.SimpleUser;
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
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class ProjectCommentInput extends MHorizontalLayout {
    private static final long serialVersionUID = 1L;

    private RichTextArea commentArea;

    private String type;
    private String typeId;
    private Integer extraTypeId;

    ProjectCommentInput(final ReloadableComponent component, final String typeVal, Integer extraTypeIdVal) {
        this.withMargin(new MarginInfo(true, true, false, false)).withFullWidth().withHeightUndefined();

        SimpleUser currentUser = UserUIContext.getUser();
        ProjectMemberBlock userBlock = new ProjectMemberBlock(currentUser.getUsername(), currentUser.getAvatarid(),
                currentUser.getDisplayName());

        MVerticalLayout textAreaWrap = new MVerticalLayout().withFullWidth().withStyleName(WebThemes.MESSAGE_CONTAINER);
        this.with(userBlock, textAreaWrap).expand(textAreaWrap);

        type = typeVal;
        extraTypeId = extraTypeIdVal;

        commentArea = new RichTextArea();
        commentArea.setWidth("100%");
        commentArea.setHeight("200px");
        commentArea.addStyleName("comment-attachment");

        final AttachmentPanel attachments = new AttachmentPanel();
        attachments.setWidth("100%");

        final MButton newCommentBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_POST), clickEvent -> {
            CommentWithBLOBs comment = new CommentWithBLOBs();
            comment.setComment(Jsoup.clean(commentArea.getValue(), Whitelist.relaxed()));
            comment.setCreatedtime(new GregorianCalendar().getTime());
            comment.setCreateduser(UserUIContext.getUsername());
            comment.setSaccountid(AppUI.getAccountId());
            comment.setType(type);
            comment.setTypeid("" + typeId);
            comment.setExtratypeid(extraTypeId);

            final CommentService commentService = AppContextUtil.getSpringBean(CommentService.class);
            int commentId = commentService.saveWithSession(comment, UserUIContext.getUsername());

            String attachmentPath = AttachmentUtils.getCommentAttachmentPath(typeVal, AppUI.getAccountId(),
                    CurrentProjectVariables.getProjectId(), typeId, commentId);

            if (!"".equals(attachmentPath)) {
                attachments.saveContentsToRepo(attachmentPath);
            }

            // save success, clear comment area and load list
            // comments again
            commentArea.setValue("");
            component.reload();
        }).withStyleName(WebThemes.BUTTON_ACTION).withIcon(FontAwesome.SEND);

        textAreaWrap.with(new MCssLayout(commentArea, attachments), newCommentBtn).withAlign(newCommentBtn, Alignment.TOP_RIGHT);
    }

    void setTypeAndId(final String typeId) {
        this.typeId = typeId;
    }
}
