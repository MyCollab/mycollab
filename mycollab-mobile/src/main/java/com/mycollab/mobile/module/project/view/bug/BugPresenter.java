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
package com.mycollab.mobile.module.project.view.bug;

import com.mycollab.core.MyCollabException;
import com.mycollab.mobile.module.project.view.AbstractProjectPresenter;
import com.mycollab.mobile.module.project.view.parameters.BugScreenData;
import com.mycollab.mobile.mvp.view.PresenterOptionUtil;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.vaadin.mvp.IPresenter;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class BugPresenter extends AbstractProjectPresenter<BugContainer> {
    private static final long serialVersionUID = -7398666868034973815L;

    public BugPresenter() {
        super(BugContainer.class);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.BUGS)) {
            IPresenter<?> presenter;

            if (data instanceof BugScreenData.Search) {
                presenter = PresenterResolver.getPresenter(BugListPresenter.class);
            } else if (data instanceof BugScreenData.Add || data instanceof BugScreenData.Edit) {
                presenter = PresenterOptionUtil.getPresenter(IBugAddPresenter.class);
            } else if (data instanceof BugScreenData.Read) {
                presenter = PresenterResolver.getPresenter(BugReadPresenter.class);

            } else {
                throw new MyCollabException("Do not support screen data");
            }

            presenter.go(container, data);
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

}
