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
package com.esofthead.mycollab.mobile.module.project.view.message;

import com.esofthead.mycollab.common.domain.SimpleComment;
import com.esofthead.mycollab.common.domain.criteria.CommentSearchCriteria;
import com.esofthead.mycollab.common.service.CommentService;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.mobile.module.project.ui.ProjectCommentRequestComp;
import com.esofthead.mycollab.mobile.ui.MobileAttachmentUtils;
import com.esofthead.mycollab.mobile.ui.UIConstants;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
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
    private Integer numComments;
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

        commentList = new BeanList<>(ApplicationContextUtil.getSpringBean(CommentService.class), CommentRowDisplayHandler.class);
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
        searchCriteria.setTypeid(StringSearchField.and(typeId));
        numComments = commentList.setSearchCriteria(searchCriteria);
        return numComments;
    }

    public int getNumComments() {
        final CommentSearchCriteria searchCriteria = new CommentSearchCriteria();
        searchCriteria.setType(StringSearchField.and(type));
        searchCriteria.setTypeid(StringSearchField.and(typeId));
        CommentService commentService = ApplicationContextUtil.getSpringBean(CommentService.class);
        return commentService.getTotalCount(searchCriteria);
    }

    @Override
    public void reload() {
        displayCommentList();
    }

    public static class CommentRowDisplayHandler extends BeanList.RowDisplayHandler<SimpleComment> {
        private static final long serialVersionUID = 7604097872938029830L;

        @Override
        public Component generateRow(SimpleComment comment, int rowIndex) {
            MHorizontalLayout commentBlock = new MHorizontalLayout().withSpacing(false).withFullWidth();
            commentBlock.setStyleName("comment-block");
            Image userAvatarImg = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(comment.getOwnerAvatarId(), 32);
            userAvatarImg.setStyleName("user-avatar");
            commentBlock.addComponent(userAvatarImg);

            CssLayout rightCol = new CssLayout();
            rightCol.setStyleName("right-col");

            MHorizontalLayout metadataRow = new MHorizontalLayout().withFullWidth();
            ELabel userNameLbl = new ELabel(comment.getOwnerFullName()).withStyleName(UIConstants.META_INFO);
            CssLayout userNameWrap = new CssLayout(userNameLbl);

            ELabel commentTimePost = new ELabel(AppContext.formatPrettyTime(comment.getCreatedtime())).withStyleName
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
