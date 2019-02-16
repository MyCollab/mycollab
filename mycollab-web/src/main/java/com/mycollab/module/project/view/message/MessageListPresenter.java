/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.message;

import com.mycollab.core.SecureAccessException;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.project.view.ProjectGenericPresenter;
import com.mycollab.module.project.view.ProjectView;
import com.mycollab.vaadin.mvp.*;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class MessageListPresenter extends ProjectGenericPresenter<MessageListView> implements ListCommand<MessageSearchCriteria> {
    private static final long serialVersionUID = 1L;

    public MessageListPresenter() {
        super(MessageListView.class);
    }

    @Override
    protected void postInitView() {
        view.getSearchHandlers().addSearchHandler(criteria -> doSearch(criteria));
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.MESSAGES)) {
            ProjectView projectView = (ProjectView) container;
            projectView.gotoSubView(ProjectView.MESSAGE_ENTRY, view);

            doSearch((MessageSearchCriteria) data.getParams());

            ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            breadCrumb.gotoMessageList();
        } else {
            throw new SecureAccessException();
        }
    }

    @Override
    public void doSearch(MessageSearchCriteria searchCriteria) {
        view.setCriteria(searchCriteria);
    }
}
