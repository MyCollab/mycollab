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
package com.mycollab.module.project.view.user;

import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public class ProjectSearchItemPresenter extends AbstractPresenter<ProjectSearchItemsView> {

    public ProjectSearchItemPresenter() {
        super(ProjectSearchItemsView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        ProjectDashboardContainer projectViewContainer = (ProjectDashboardContainer) container;
        projectViewContainer.setContent(view);
        String params = (String) data.getParams();
        view.displayResults(params);

        ProjectBreadcrumb breadcrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
        breadcrumb.gotoSearchProjectItems();
    }
}
