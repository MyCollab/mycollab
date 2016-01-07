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
package com.esofthead.mycollab.mobile.module.project.view.settings;

import com.esofthead.mycollab.mobile.module.project.view.parameters.ProjectMemberScreenData;
import com.esofthead.mycollab.mobile.mvp.AbstractPresenter;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePresenter;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class ProjectUserPresenter extends AbstractMobilePresenter<ProjectUserContainer> {
    private static final long serialVersionUID = 1L;

    public ProjectUserPresenter() {
        super(ProjectUserContainer.class);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        AbstractPresenter<?> presenter;

        if (data instanceof ProjectMemberScreenData.InviteProjectMembers) {
            presenter = PresenterResolver.getPresenter(ProjectMemberInvitePresenter.class);
        } else if (data instanceof ProjectMemberScreenData.Read) {
            presenter = PresenterResolver.getPresenter(ProjectMemberReadPresenter.class);
        } else if (data instanceof ProjectMemberScreenData.Edit) {
            presenter = PresenterResolver.getPresenter(ProjectMemberEditPresenter.class);
        } else {
            presenter = PresenterResolver.getPresenter(ProjectMemberListPresenter.class);
        }

        presenter.go(container, data);
    }
}
