package com.mycollab.mobile.module.project.ui;

import com.mycollab.common.domain.SimpleComment;
import com.mycollab.common.domain.criteria.CommentSearchCriteria;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.CommentService;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.mobile.ui.MobileAttachmentUtils;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.*;
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

    private BeanList<CommentService, CommentSearchCriteria, SimpleComment> commentList;
    private String type;
    private String typeId;
    private Integer extraTypeId;
    private boolean isDisplayCommentInput;

    public ProjectCommentListView(String type, String typeId, Integer extraTypeId, boolean isDisplayCommentInput) {
        this.addStyleName("comment-list");
        this.type = type;
        this.typeId = typeId;
        this.isDisplayCommentInput = isDisplayCommentInput;
        this.extraTypeId = extraTypeId;
    }

    public void displayCommentList() {
        if (type == null || typeId == null) {
            return;
        }

        commentList = new BeanList<>(AppContextUtil.getSpringBean(CommentService.class), new CommentRowDisplayHandler());
        this.setContent(commentList);
        if (isDisplayCommentInput) {
            ProjectCommentRequestComp commentBox = new ProjectCommentRequestComp(type, typeId, extraTypeId);
            this.setToolbar(commentBox);
        }

        CommentSearchCriteria searchCriteria = new CommentSearchCriteria();
        searchCriteria.setType(StringSearchField.and(type));
        searchCriteria.setTypeId(StringSearchField.and(typeId));
        Integer numComments = commentList.setSearchCriteria(searchCriteria);
        this.setCaption(UserUIContext.getMessage(GenericI18Enum.OPT_COMMENTS_VALUE, numComments));
    }

    @Override
    protected void onBecomingVisible() {
        super.onBecomingVisible();
        displayCommentList();
    }

    @Override
    public void reload() {
        displayCommentList();
    }

    private static class CommentRowDisplayHandler implements IBeanList.RowDisplayHandler<SimpleComment> {

        @Override
        public Component generateRow(IBeanList<SimpleComment> host, SimpleComment comment, int rowIndex) {
            MHorizontalLayout commentBlock = new MHorizontalLayout().withFullWidth().withStyleName("comment-block");
            Image userAvatarImg = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(comment.getOwnerAvatarId(), 32);
            userAvatarImg.addStyleName(UIConstants.CIRCLE_BOX);
            commentBlock.addComponent(userAvatarImg);

            CssLayout rightCol = new CssLayout();
            rightCol.setWidth("100%");
            rightCol.setStyleName("right-col");

            MHorizontalLayout metadataRow = new MHorizontalLayout().withFullWidth();
            ELabel userNameLbl = new ELabel(comment.getOwnerFullName()).withStyleName(UIConstants.META_INFO,
                    UIConstants.TEXT_ELLIPSIS);
            ELabel commentTimePost = new ELabel(UserUIContext.formatPrettyTime(comment.getCreatedtime())).withStyleName
                    (UIConstants.META_INFO).withWidthUndefined();
            metadataRow.with(userNameLbl, commentTimePost).withAlign(commentTimePost, Alignment.TOP_RIGHT).expand(userNameLbl);
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

            commentBlock.with(rightCol).expand(rightCol);
            return commentBlock;
        }
    }
}
