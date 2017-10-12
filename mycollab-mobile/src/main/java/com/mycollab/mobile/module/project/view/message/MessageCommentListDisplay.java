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
package com.mycollab.mobile.module.project.view.message;

import com.mycollab.common.domain.SimpleComment;
import com.mycollab.common.domain.criteria.CommentSearchCriteria;
import com.mycollab.common.service.CommentService;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.mobile.module.project.ui.ProjectCommentRequestComp;
import com.mycollab.mobile.ui.MobileAttachmentUtils;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.*;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class MessageCommentListDisplay extends VerticalLayout implements ReloadableComponent {
    private static final long serialVersionUID = 1L;

    private final BeanList<CommentService, CommentSearchCriteria, SimpleComment> commentList;
    private String type;
    private String typeId;
    private ProjectCommentRequestComp commentBox;

    public MessageCommentListDisplay(final String type, String typeId, final Integer extraTypeId, final boolean
            isDisplayCommentInput) {
        this.setStyleName("comment-list");
        this.setMargin(new MarginInfo(true, false, false, false));
        this.type = type;
        this.typeId = typeId;
        if (isDisplayCommentInput) {
            commentBox = new ProjectCommentRequestComp(type, typeId, extraTypeId);
        }

        commentList = new BeanList<>(AppContextUtil.getSpringBean(CommentService.class), new CommentRowDisplayHandler());
        commentList.setDisplayEmptyListText(false);
        this.addComponent(commentList);
        displayCommentList();
    }

    private int displayCommentList() {
        if (type == null || typeId == null) {
            return 0;
        }

        final CommentSearchCriteria searchCriteria = new CommentSearchCriteria();
        searchCriteria.setType(StringSearchField.and(type));
        searchCriteria.setTypeId(StringSearchField.and(typeId));
        return commentList.setSearchCriteria(searchCriteria);
    }

    public int getNumComments() {
        final CommentSearchCriteria searchCriteria = new CommentSearchCriteria();
        searchCriteria.setType(StringSearchField.and(type));
        searchCriteria.setTypeId(StringSearchField.and(typeId));
        CommentService commentService = AppContextUtil.getSpringBean(CommentService.class);
        return commentService.getTotalCount(searchCriteria);
    }

    @Override
    public void reload() {
        displayCommentList();
    }

    private static class CommentRowDisplayHandler implements IBeanList.RowDisplayHandler<SimpleComment> {

        @Override
        public Component generateRow(IBeanList<SimpleComment> host, SimpleComment comment, int rowIndex) {
            MHorizontalLayout commentBlock = new MHorizontalLayout().withSpacing(true).withFullWidth();
            commentBlock.setStyleName("comment-block");
            Image userAvatarImg = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(comment.getOwnerAvatarId(), 32);
            userAvatarImg.addStyleName(UIConstants.CIRCLE_BOX);
            commentBlock.addComponent(userAvatarImg);

            CssLayout rightCol = new CssLayout();
            rightCol.setStyleName("right-col");

            MHorizontalLayout metadataRow = new MHorizontalLayout().withFullWidth();
            ELabel userNameLbl = new ELabel(comment.getOwnerFullName()).withStyleName(UIConstants.META_INFO);
            CssLayout userNameWrap = new CssLayout(userNameLbl);

            ELabel commentTimePost = new ELabel(UserUIContext.formatPrettyTime(comment.getCreatedtime())).withStyleName
                    (UIConstants.META_INFO).withWidthUndefined();
            metadataRow.with(userNameWrap, commentTimePost).withAlign(commentTimePost, Alignment.TOP_RIGHT).expand
                    (userNameWrap);
            rightCol.addComponent(metadataRow);

            SafeHtmlLabel commentContent = new SafeHtmlLabel(comment.getComment());
            commentContent.setStyleName("comment-content");
            rightCol.addComponent(commentContent);

            List<Content> attachments = comment.getAttachments();
            if (!CollectionUtils.isEmpty(attachments)) {
                CssLayout attachmentPanel = new CssLayout();
                attachmentPanel.setStyleName("attachment-panel");
                attachmentPanel.setWidth("100%");

                for (Content attachment : attachments) {
                    attachmentPanel.addComponent(MobileAttachmentUtils.renderAttachmentRow(attachment));
                }
                rightCol.addComponent(attachmentPanel);
            }

            commentBlock.with(rightCol).expand(rightCol);
            return commentBlock;
        }

    }

    public ProjectCommentRequestComp getCommentBox() {
        return this.commentBox;
    }
}
