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
package com.mycollab.module.project.ui.components;

import com.mycollab.common.domain.CommentWithBLOBs;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.CommentService;
import com.mycollab.module.file.AttachmentUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.ReloadableComponent;
import com.mycollab.vaadin.web.ui.AttachmentPanel;
import com.mycollab.vaadin.web.ui.WebUIConstants;
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
public class ProjectCommentInput extends MHorizontalLayout {
    private static final long serialVersionUID = 1L;

    private RichTextArea commentArea;

    private String type;
    private String typeId;
    private Integer extraTypeId;

    ProjectCommentInput(final ReloadableComponent component, final String typeVal, Integer extraTypeIdVal) {
        this.withMargin(new MarginInfo(true, true, false, false)).withFullWidth().withHeightUndefined();

        SimpleUser currentUser = AppContext.getUser();
        ProjectMemberBlock userBlock = new ProjectMemberBlock(currentUser.getUsername(), currentUser.getAvatarid(),
                currentUser.getDisplayName());

        MVerticalLayout textAreaWrap = new MVerticalLayout().withFullWidth().withStyleName(WebUIConstants.MESSAGE_CONTAINER);
        this.with(userBlock, textAreaWrap).expand(textAreaWrap);

        type = typeVal;
        extraTypeId = extraTypeIdVal;

        commentArea = new RichTextArea();
        commentArea.setWidth("100%");
        commentArea.setHeight("200px");

        final AttachmentPanel attachments = new AttachmentPanel();

        final MHorizontalLayout controlsLayout = new MHorizontalLayout().withFullWidth();
        controlsLayout.setDefaultComponentAlignment(Alignment.TOP_RIGHT);

        final MButton cancelBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR), clickEvent -> commentArea.setValue(""))
                .withStyleName(WebUIConstants.BUTTON_OPTION);

        final MButton newCommentBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_POST), clickEvent -> {
            CommentWithBLOBs comment = new CommentWithBLOBs();
            comment.setComment(Jsoup.clean(commentArea.getValue(), Whitelist.relaxed()));
            comment.setCreatedtime(new GregorianCalendar().getTime());
            comment.setCreateduser(AppContext.getUsername());
            comment.setSaccountid(AppContext.getAccountId());
            comment.setType(type);
            comment.setTypeid("" + typeId);
            comment.setExtratypeid(extraTypeId);

            final CommentService commentService = AppContextUtil.getSpringBean(CommentService.class);
            int commentId = commentService.saveWithSession(comment, AppContext.getUsername());

            String attachmentPath = AttachmentUtils.getCommentAttachmentPath(typeVal, AppContext.getAccountId(),
                    CurrentProjectVariables.getProjectId(), typeId, commentId);

            if (!"".equals(attachmentPath)) {
                attachments.saveContentsToRepo(attachmentPath);
            }

            // save success, clear comment area and load list
            // comments again
            commentArea.setValue("");
            component.reload();
        }).withStyleName(WebUIConstants.BUTTON_ACTION).withIcon(FontAwesome.SEND);

        controlsLayout.with(attachments, new MHorizontalLayout(cancelBtn, newCommentBtn)).expand(attachments)
                .withAlign(attachments, Alignment.TOP_LEFT);
        textAreaWrap.with(commentArea, controlsLayout);
    }

    void setTypeAndId(final String typeId) {
        this.typeId = typeId;
    }
}
