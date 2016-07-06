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
package com.mycollab.mobile.module.project.view.task;

import com.mycollab.core.MyCollabException;
import com.mycollab.mobile.module.project.view.parameters.TaskScreenData;
import com.mycollab.mobile.mvp.AbstractPresenter;
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
 * @since 4.5.0
 */
public class TaskPresenter extends AbstractPresenter<TaskContainer> {
    private static final long serialVersionUID = 7999611450505328038L;

    public TaskPresenter() {
        super(TaskContainer.class);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.TASKS)) {
            IPresenter<?> presenter;

            if (data instanceof TaskScreenData.Search) {
                presenter = PresenterResolver.getPresenter(TaskListPresenter.class);
            } else if (data instanceof TaskScreenData.Read) {
                presenter = PresenterResolver.getPresenter(TaskReadPresenter.class);
            } else if (data instanceof TaskScreenData.Add || data instanceof TaskScreenData.Edit) {
                presenter = PresenterOptionUtil.getPresenter(ITaskAddPresenter.class);
            } else {
                throw new MyCollabException("Do not support param: " + data);
            }

            presenter.go(container, data);
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

}
