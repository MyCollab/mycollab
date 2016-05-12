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

import com.esofthead.mycollab.common.domain.criteria.CommentSearchCriteria;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.CommentService;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.addon.touchkit.ui.NavigationButton;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class CommentNavigationButton extends NavigationButton {
    private String type;
    private String typeId;

    public CommentNavigationButton(String typeVal, String typeIdVal) {
        super(AppContext.getMessage(GenericI18Enum.TAB_COMMENT, 0));
        this.type = typeVal;
        this.typeId = typeIdVal;
        this.addClickListener(new NavigationButtonClickListener() {
            @Override
            public void buttonClick(NavigationButtonClickEvent event) {
                if (typeId != null) {
                    getNavigationManager().navigateTo(new ProjectCommentListView(type, typeId,
                            CurrentProjectVariables.getProjectId(), true));
                }
            }
        });
    }

    public void displayTotalComments(String typeId) {
        this.typeId = typeId;
        CommentService commentService = AppContextUtil.getSpringBean(CommentService.class);
        CommentSearchCriteria searchCriteria = new CommentSearchCriteria();
        searchCriteria.setType(StringSearchField.and(type));
        searchCriteria.setTypeId(StringSearchField.and(typeId));
        this.setCaption(AppContext.getMessage(GenericI18Enum.TAB_COMMENT, commentService.getTotalCount(searchCriteria)));
    }
}
