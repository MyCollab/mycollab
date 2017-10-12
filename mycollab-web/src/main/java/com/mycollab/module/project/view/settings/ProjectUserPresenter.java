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
package com.mycollab.module.project.view.settings;

import com.mycollab.module.project.view.parameters.ProjectMemberScreenData;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ProjectUserPresenter extends AbstractPresenter<ProjectUserContainer> {
    private static final long serialVersionUID = 1L;

    public ProjectUserPresenter() {
        super(ProjectUserContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        AbstractPresenter<?> presenter;

        if (data instanceof ProjectMemberScreenData.Add) {
            presenter = PresenterResolver.getPresenter(ProjectMemberEditPresenter.class);
        } else if (data instanceof ProjectMemberScreenData.InviteProjectMembers) {
            presenter = PresenterResolver.getPresenter(ProjectMemberInvitePresenter.class);
        } else if (data instanceof ProjectMemberScreenData.InviteProjectMembers) {
            presenter = PresenterResolver.getPresenter(ProjectMemberInvitePresenter.class);
        } else if (data instanceof ProjectMemberScreenData.Read) {
            presenter = PresenterResolver.getPresenter(ProjectMemberReadPresenter.class);
        } else {
            presenter = PresenterResolver.getPresenter(ProjectMemberListPresenter.class);
        }

        presenter.go(view, data);
    }
}
