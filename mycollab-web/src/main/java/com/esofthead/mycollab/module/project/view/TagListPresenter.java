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
package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.common.domain.Tag;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.project.view.user.ProjectDashboardContainer;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 5.0.1
 */
public class TagListPresenter extends AbstractPresenter<TagListView> {
    private static final long serialVersionUID = 1L;

    public TagListPresenter() {
        super(TagListView.class);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        ProjectDashboardContainer projectViewContainer = (ProjectDashboardContainer) container;
        projectViewContainer.removeAllComponents();
        projectViewContainer.addComponent(view.getWidget());
        Object params = data.getParams();
        if (params instanceof Tag || params == null) {
            ProjectBreadcrumb breadcrumb = ViewManager
                    .getCacheComponent(ProjectBreadcrumb.class);
            breadcrumb.gotoTagList();
            view.displayTags((Tag) params);
        } else {
            throw new MyCollabException("Do not support param type " + params);
        }
    }
}
