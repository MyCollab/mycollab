/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.settings;

import com.mycollab.core.MyCollabException;
import com.mycollab.module.project.view.parameters.ProjectRoleScreenData;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectRolePresenter extends AbstractPresenter<ProjectRoleContainer> {
    private static final long serialVersionUID = 1L;

    public ProjectRolePresenter() {
        super(ProjectRoleContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        AbstractPresenter<?> presenter;

        if (data instanceof ProjectRoleScreenData.Search) {
            presenter = PresenterResolver.getPresenter(ProjectRoleListPresenter.class);
        } else if (data instanceof ProjectRoleScreenData.Add) {
            presenter = PresenterResolver.getPresenter(ProjectRoleAddPresenter.class);
        } else if (data instanceof ProjectRoleScreenData.Read) {
            presenter = PresenterResolver.getPresenter(ProjectRoleReadPresenter.class);
        } else {
            throw new MyCollabException("Can not handle data " + data);
        }

        presenter.go(view, data);
    }
}
