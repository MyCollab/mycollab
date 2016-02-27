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
package com.esofthead.mycollab.module.project.ui.components;

import com.esofthead.mycollab.common.domain.SimpleComment;
import com.esofthead.mycollab.common.domain.criteria.CommentSearchCriteria;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.CommentService;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.ReloadableComponent;
import com.esofthead.mycollab.vaadin.web.ui.TabSheetLazyLoadComponent;
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
        withMargin(new MarginInfo(true, false, true, false));
        this.type = type;
        commentBox = new ProjectCommentInput(this, type, extraTypeId);
        this.addComponent(commentBox);

        commentList = new BeanList<>(ApplicationContextUtil.getSpringBean(CommentService.class), CommentRowDisplayHandler.class);
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
            ((TabSheetLazyLoadComponent) parentComp).getTab(this).setCaption(AppContext.getMessage(GenericI18Enum.TAB_COMMENT, numComments));
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