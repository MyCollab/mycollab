package com.mycollab.module.project.ui.components;

import com.mycollab.common.domain.SimpleComment;
import com.mycollab.common.domain.criteria.CommentSearchCriteria;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.CommentService;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.BeanList;
import com.mycollab.vaadin.ui.ReloadableComponent;
import com.mycollab.vaadin.web.ui.TabSheetLazyLoadComponent;
import com.vaadin.shared.ui.MarginInfo;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CommentDisplay extends MVerticalLayout implements ReloadableComponent {
    private static final long serialVersionUID = 1L;

    private BeanList<CommentService, CommentSearchCriteria, SimpleComment> commentList;
    private String type;
    private String typeId;
    private ProjectCommentInput commentBox;

    public CommentDisplay(String type, Integer extraTypeId) {
        withMargin(new MarginInfo(true, false, true, false)).withFullWidth();
        this.type = type;
        commentBox = new ProjectCommentInput(this, type, extraTypeId);
        commentBox.setWidth("100%");
        this.addComponent(commentBox);

        commentList = new BeanList<>(AppContextUtil.getSpringBean(CommentService.class), new CommentRowDisplayHandler());
        commentList.setDisplayEmptyListText(false);
        this.addComponent(commentList);

        displayCommentList();
    }

    private void displayCommentList() {
        if (type == null || typeId == null) {
            return;
        }

        CommentSearchCriteria searchCriteria = new CommentSearchCriteria();
        searchCriteria.setType(StringSearchField.and(type));
        searchCriteria.setTypeId(StringSearchField.and(typeId));
        int numComments = commentList.setSearchCriteria(searchCriteria);

        Object parentComp = this.getParent();
        if (parentComp instanceof TabSheetLazyLoadComponent) {
            ((TabSheetLazyLoadComponent) parentComp).getTab(this).setCaption(UserUIContext.getMessage(GenericI18Enum.OPT_COMMENTS_VALUE, numComments));
        }
    }

    public void loadComments(String typeId) {
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
}