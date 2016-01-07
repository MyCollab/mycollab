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
package com.esofthead.mycollab.mobile.module.project.ui;

import com.esofthead.mycollab.common.domain.SimpleComment;
import com.esofthead.mycollab.common.domain.criteria.CommentSearchCriteria;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.CommentService;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePageView;
import com.esofthead.mycollab.mobile.ui.MobileAttachmentUtils;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.ReloadableComponent;
import com.esofthead.mycollab.vaadin.ui.SafeHtmlLabel;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.ui.*;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class ProjectCommentListView extends AbstractMobilePageView implements ReloadableComponent {
    private static final long serialVersionUID = 1L;

    private final BeanList<CommentService, CommentSearchCriteria, SimpleComment> commentList;
    private String type;
    private String typeId;
    private Integer numComments;
    private ProjectCommentInput commentBox;

    @Deprecated
    public ProjectCommentListView(String type, Integer extraTypeId, boolean isDisplayCommentInput) {
        this(type, null, extraTypeId, isDisplayCommentInput);
    }

    public ProjectCommentListView(String type, String typeId, Integer extraTypeId, boolean isDisplayCommentInput) {
        this.addStyleName("comment-list");
        this.type = type;
        this.typeId = typeId;

        commentList = new BeanList<>(ApplicationContextUtil.getSpringBean(CommentService.class), CommentRowDisplayHandler.class);
        commentList.setDisplayEmptyListText(false);
        this.setContent(commentList);
        if (isDisplayCommentInput) {
            commentBox = new ProjectCommentInput(this, type, extraTypeId);
            this.setToolbar(commentBox);
        }
        displayCommentList();
    }

    private void displayCommentList() {
        if (type == null || typeId == null) {
            return;
        }

        CommentSearchCriteria searchCriteria = new CommentSearchCriteria();
        searchCriteria.setType(new StringSearchField(type));
        searchCriteria.setTypeid(new StringSearchField(typeId));
        numComments = commentList.setSearchCriteria(searchCriteria);
        this.setCaption(AppContext.getMessage(GenericI18Enum.TAB_COMMENT, numComments));
    }

    public int getNumComments() {
        return numComments;
    }

    @Deprecated
    public void loadComments(final String typeId) {
        this.typeId = typeId;
        if (commentBox != null) {
            commentBox.setTypeAndId(typeId);
        }
        displayCommentList();
    }

    @Override
    public void reload() {
        displayCommentList();
    }

    public static class CommentRowDisplayHandler extends BeanList.RowDisplayHandler<SimpleComment> {
        private static final long serialVersionUID = 7604097872938029830L;

        @Override
        public Component generateRow(SimpleComment comment, int rowIndex) {
            MHorizontalLayout commentBlock = new MHorizontalLayout().withSpacing(false);
            commentBlock.setStyleName("comment-block");
            Image userAvatarImg = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(comment.getOwnerAvatarId(), 32);
            userAvatarImg.setStyleName("user-avatar");
            commentBlock.addComponent(userAvatarImg);

            CssLayout rightCol = new CssLayout();
            rightCol.setWidth("100%");
            rightCol.setStyleName("right-col");

            HorizontalLayout metadataRow = new HorizontalLayout();
            metadataRow.setWidth("100%");
            Label userNameLbl = new Label(comment.getOwnerFullName());
            userNameLbl.setStyleName("user-name");
            metadataRow.addComponent(userNameLbl);
            metadataRow.setExpandRatio(userNameLbl, 1.0f);

            Label commentTimePost = new Label(AppContext.formatPrettyTime(comment.getCreatedtime()));
            commentTimePost.setStyleName("time-post");
            commentTimePost.setWidthUndefined();
            metadataRow.addComponent(commentTimePost);
            rightCol.addComponent(metadataRow);

            Label commentContent = new SafeHtmlLabel(comment.getComment());
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

            commentBlock.addComponent(rightCol);
            commentBlock.setExpandRatio(rightCol, 1.0f);
            commentBlock.setWidth("100%");
            return commentBlock;
        }

    }

    public ProjectCommentInput getCommentBox() {
        return this.commentBox;
    }
}
