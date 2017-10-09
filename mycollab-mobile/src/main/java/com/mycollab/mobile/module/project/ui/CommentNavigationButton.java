package com.mycollab.mobile.module.project.ui;

import com.mycollab.common.domain.criteria.CommentSearchCriteria;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.CommentService;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.addon.touchkit.ui.NavigationButton;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class CommentNavigationButton extends NavigationButton {
    private String type;
    private String typeId;

    private ProjectCommentListView commentListView;

    public CommentNavigationButton(String typeVal, String typeIdVal) {
        super(UserUIContext.getMessage(GenericI18Enum.OPT_COMMENTS_VALUE, 0));
        this.type = typeVal;
        this.typeId = typeIdVal;
        commentListView = new ProjectCommentListView(type, typeId, CurrentProjectVariables.getProjectId(), true);
        this.addClickListener(navigationButtonClickEvent -> {
            if (typeId != null) {
                getNavigationManager().navigateTo(commentListView);
                commentListView.displayCommentList();
            }
        });
    }

    public void displayTotalComments(String typeId) {
        this.typeId = typeId;
        CommentService commentService = AppContextUtil.getSpringBean(CommentService.class);
        CommentSearchCriteria searchCriteria = new CommentSearchCriteria();
        searchCriteria.setType(StringSearchField.and(type));
        searchCriteria.setTypeId(StringSearchField.and(typeId));
        this.setCaption(UserUIContext.getMessage(GenericI18Enum.OPT_COMMENTS_VALUE, commentService.getTotalCount(searchCriteria)));
    }
}
